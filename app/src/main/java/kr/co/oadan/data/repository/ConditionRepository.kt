package kr.co.oadan.data.repository

import kotlinx.coroutines.flow.Flow
import kr.co.oadan.data.local.ConditionLogDao
import kr.co.oadan.data.model.ConditionLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConditionRepository(private val conditionLogDao: ConditionLogDao) {

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    val latestWeightLog: Flow<ConditionLog?> = conditionLogDao.getLatestWeightLog()

    fun getTodayLogs(): Flow<List<ConditionLog>> {
        return conditionLogDao.getLogsForDate(getTodayDateString())
    }

    fun getAllLogs(): Flow<List<ConditionLog>> = conditionLogDao.getAllConditionLogs()

    suspend fun getAllLogsOnce(): List<ConditionLog> = conditionLogDao.getAllConditionLogsOnce()

    suspend fun recordCondition(
        weightKg: Float? = null,
        skeletalMuscleKg: Float? = null,
        bodyFatPercent: Float? = null,
        conditionTag: String? = null,
        memo: String? = null
    ): Long {
        val log = ConditionLog(
            timestampUtc = System.currentTimeMillis(),
            date = getTodayDateString(),
            weightKg = weightKg,
            skeletalMuscleKg = skeletalMuscleKg,
            bodyFatPercent = bodyFatPercent,
            conditionTag = conditionTag,
            memo = memo
        )
        return conditionLogDao.insert(log)
    }
}
