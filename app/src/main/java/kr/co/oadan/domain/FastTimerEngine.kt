package kr.co.oadan.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class FastingTimerState(
    val elapsedMillis: Long,
    val remainingMillis: Long,
    val progressRatio: Float, // 0.0 ~ 1.0 (또는 1.0 초과 시 초과 달성)
    val isGoalReached: Boolean,
    val isOver72Hours: Boolean,
    val currentStage: MetabolicStage,
    val nextStage: MetabolicStage?,
    val nextStageRemainingMillis: Long,
    val formattedElapsed: String, // "14:24:08"
    val formattedRemaining: String, // "1시간 35분" 또는 "1h 35m"
    val formattedTargetHours: String, // "16:00"
    val startTimeFormatted: String, // "어제 21:30"
    val targetEndTimeFormatted: String // "오늘 13:30"
)

object FastTimerEngine {

    fun calculateState(
        startTimeUtc: Long,
        targetEndTimeUtc: Long,
        currentTimeUtc: Long = System.currentTimeMillis()
    ): FastingTimerState {
        val elapsedMillis = (currentTimeUtc - startTimeUtc).coerceAtLeast(0L)
        val totalDurationMillis = (targetEndTimeUtc - startTimeUtc).coerceAtLeast(1L)
        val remainingMillis = (targetEndTimeUtc - currentTimeUtc).coerceAtLeast(0L)

        val progressRatio = (elapsedMillis.toDouble() / totalDurationMillis.toDouble()).toFloat()
        val isGoalReached = elapsedMillis >= totalDurationMillis

        val elapsedHours = elapsedMillis / (1000f * 60f * 60f)
        val isOver72Hours = elapsedHours >= 72f

        val currentStage = MetabolicStage.fromElapsedHours(elapsedHours)

        val nextStage = when (currentStage) {
            MetabolicStage.STAGE_1 -> MetabolicStage.STAGE_2
            MetabolicStage.STAGE_2 -> MetabolicStage.STAGE_3
            MetabolicStage.STAGE_3 -> MetabolicStage.STAGE_4
            MetabolicStage.STAGE_4 -> MetabolicStage.STAGE_5
            MetabolicStage.STAGE_5 -> null
        }

        val nextStageRemainingMillis = if (nextStage != null) {
            val nextStageTargetMillis = (nextStage.startHours * 3600f * 1000f).toLong()
            (nextStageTargetMillis - elapsedMillis).coerceAtLeast(0L)
        } else {
            0L
        }

        return FastingTimerState(
            elapsedMillis = elapsedMillis,
            remainingMillis = remainingMillis,
            progressRatio = progressRatio,
            isGoalReached = isGoalReached,
            isOver72Hours = isOver72Hours,
            currentStage = currentStage,
            nextStage = nextStage,
            nextStageRemainingMillis = nextStageRemainingMillis,
            formattedElapsed = formatElapsedHms(elapsedMillis),
            formattedRemaining = formatRemainingKorean(remainingMillis),
            formattedTargetHours = formatTargetHours(totalDurationMillis),
            startTimeFormatted = formatRelativeDate(startTimeUtc, currentTimeUtc),
            targetEndTimeFormatted = formatRelativeDate(targetEndTimeUtc, currentTimeUtc)
        )
    }

    fun formatElapsedHms(millis: Long): String {
        val totalSeconds = millis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun formatRemainingKorean(millis: Long): String {
        if (millis <= 0) return "목표 달성!"
        val totalMinutes = millis / (1000 * 60)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (hours > 0) {
            "${hours}시간 ${minutes}분"
        } else {
            "${minutes}분"
        }
    }

    fun formatRemainingHmsCompact(millis: Long): String {
        val totalSeconds = (millis / 1000).coerceAtLeast(0)
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
    }

    fun formatTargetHours(totalMillis: Long): String {
        val hours = totalMillis / (1000 * 3600)
        val minutes = (totalMillis % (1000 * 3600)) / (1000 * 60)
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
    }

    fun formatRelativeDate(timestampUtc: Long, referenceTimeUtc: Long = System.currentTimeMillis()): String {
        val dateSdf = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        val timeSdf = SimpleDateFormat("HH:mm", Locale.KOREA)

        val targetDateStr = dateSdf.format(Date(timestampUtc))
        val refDateStr = dateSdf.format(Date(referenceTimeUtc))
        val timeStr = timeSdf.format(Date(timestampUtc))

        val diffDays = (timestampUtc - referenceTimeUtc) / (1000 * 60 * 60 * 24)

        val prefix = when {
            targetDateStr == refDateStr -> "오늘"
            diffDays == -1L || (refDateStr.toInt() - targetDateStr.toInt() == 1) -> "어제"
            diffDays == 1L || (targetDateStr.toInt() - refDateStr.toInt() == 1) -> "내일"
            else -> SimpleDateFormat("M월 d일", Locale.KOREA).format(Date(timestampUtc))
        }
        return "$prefix $timeStr"
    }

    fun formatFullHoursMinutes(millis: Long): String {
        val totalMinutes = millis / (1000 * 60)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "${hours}h ${minutes}m"
    }

    fun formatTodayDateKorean(): String {
        val sdf = SimpleDateFormat("M월 d일 EEEE", Locale.KOREA)
        return sdf.format(Date())
    }
}
