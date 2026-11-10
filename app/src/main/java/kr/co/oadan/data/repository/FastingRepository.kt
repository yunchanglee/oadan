package kr.co.oadan.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.co.oadan.data.local.FastSessionDao
import kr.co.oadan.data.model.FastSession
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FastingRepository(private val fastSessionDao: FastSessionDao) {

    val activeSession: Flow<FastSession?> = fastSessionDao.getActiveSession()

    suspend fun getActiveSessionOnce(): FastSession? = fastSessionDao.getActiveSessionOnce()

    fun getRecentSessions(limit: Int = 10): Flow<List<FastSession>> =
        fastSessionDao.getRecentSessions(limit)

    fun getAllSessions(): Flow<List<FastSession>> = fastSessionDao.getAllSessions()

    suspend fun getAllSessionsOnce(): List<FastSession> = fastSessionDao.getAllSessionsOnce()

    suspend fun startFasting(
        protocol: String,
        targetHours: Float,
        customStartTime: Long? = null
    ): Long {
        // 이미 진행 중인 단식이 있다면 종료하거나 새 단식으로 덮어씀
        val active = fastSessionDao.getActiveSessionOnce()
        if (active != null) {
            val now = System.currentTimeMillis()
            val elapsedHours = (now - active.startTimeUtc) / (3600f * 1000f)
            fastSessionDao.update(
                active.copy(
                    actualEndTimeUtc = now,
                    fastedHours = elapsedHours,
                    isCompleted = elapsedHours >= active.targetHours
                )
            )
        }

        val startTime = customStartTime ?: System.currentTimeMillis()
        val targetEndTime = startTime + (targetHours * 3600f * 1000f).toLong()
        val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(startTime))

        val newSession = FastSession(
            startTimeUtc = startTime,
            targetEndTimeUtc = targetEndTime,
            protocol = protocol,
            targetHours = targetHours,
            date = dateStr
        )
        return fastSessionDao.insert(newSession)
    }

    suspend fun endFasting(
        actualEndTime: Long? = null,
        isPartial: Boolean = false,
        notes: String? = null
    ): FastSession? {
        val active = fastSessionDao.getActiveSessionOnce() ?: return null
        val endTime = actualEndTime ?: System.currentTimeMillis()
        val elapsedHours = (endTime - active.startTimeUtc) / (3600f * 1000f)
        val isCompleted = !isPartial && (elapsedHours >= active.targetHours)

        val updated = active.copy(
            actualEndTimeUtc = endTime,
            fastedHours = elapsedHours,
            isCompleted = isCompleted,
            isPartiallyCompleted = isPartial,
            notes = notes ?: active.notes
        )
        fastSessionDao.update(updated)
        return updated
    }

    suspend fun updateSessionTimes(
        sessionId: Long,
        newStartTimeUtc: Long,
        newTargetEndTimeUtc: Long,
        newActualEndTimeUtc: Long? = null
    ) {
        val session = fastSessionDao.getSessionById(sessionId) ?: return
        val fastedHours = if (newActualEndTimeUtc != null) {
            (newActualEndTimeUtc - newStartTimeUtc) / (3600f * 1000f)
        } else session.fastedHours

        val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(newStartTimeUtc))

        val updated = session.copy(
            startTimeUtc = newStartTimeUtc,
            targetEndTimeUtc = newTargetEndTimeUtc,
            actualEndTimeUtc = newActualEndTimeUtc ?: session.actualEndTimeUtc,
            fastedHours = fastedHours,
            date = dateStr,
            isCompleted = if (fastedHours != null) fastedHours >= session.targetHours else session.isCompleted
        )
        fastSessionDao.update(updated)
    }

    suspend fun updateActiveSessionTarget(newTargetHours: Float) {
        val active = fastSessionDao.getActiveSessionOnce() ?: return
        val newTargetEndTime = active.startTimeUtc + (newTargetHours * 3600f * 1000f).toLong()
        fastSessionDao.update(
            active.copy(
                targetHours = newTargetHours,
                targetEndTimeUtc = newTargetEndTime
            )
        )
    }

    fun calculateStreak(): Flow<Int> = fastSessionDao.getAllSessions().map { sessions ->
        // 완료된 세션 기반 연속 달성일 계산
        val completedDates = sessions
            .filter { it.isCompleted && it.actualEndTimeUtc != null }
            .map { it.date }
            .distinct()
            .sortedDescending()

        if (completedDates.isEmpty()) return@map 0

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayStr = sdf.format(Date())
        val yesterdayStr = sdf.format(Date(System.currentTimeMillis() - 86400000L))

        // 오늘 또는 어제부터 연속되는지 확인
        var streak = 0
        var checkTime = if (completedDates.contains(todayStr)) {
            System.currentTimeMillis()
        } else if (completedDates.contains(yesterdayStr)) {
            System.currentTimeMillis() - 86400000L
        } else {
            return@map 0
        }

        while (true) {
            val dateToCheck = sdf.format(Date(checkTime))
            if (completedDates.contains(dateToCheck)) {
                streak++
                checkTime -= 86400000L
            } else {
                break
            }
        }
        streak
    }
}
