package kr.co.oadan.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import kr.co.oadan.MainActivity
import kr.co.oadan.R
import kr.co.oadan.domain.FastTimerEngine
import kr.co.oadan.domain.MetabolicStage

class FastingNotificationManager(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        const val CHANNEL_FASTING_ALERTS = "channel_fasting_alerts"
        const val CHANNEL_ONGOING = "channel_fasting_ongoing"
        const val ONGOING_NOTIFICATION_ID = 1001
        const val ALERT_NOTIFICATION_ID = 2001

        const val ACTION_ADD_WATER_250 = "kr.co.oadan.ACTION_ADD_WATER_250"
        const val ACTION_STOP_FASTING = "kr.co.oadan.ACTION_STOP_FASTING"
        const val ACTION_STAGE_ALERT = "kr.co.oadan.ACTION_STAGE_ALERT"
        const val ACTION_COMPLETION_ALERT = "kr.co.oadan.ACTION_COMPLETION_ALERT"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 단식 이벤트 알림 채널
            val alertChannel = NotificationChannel(
                CHANNEL_FASTING_ALERTS,
                context.getString(R.string.channel_fasting_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.channel_fasting_desc)
                enableVibration(true)
            }

            // 실시간 상단 고정 알림 채널
            val ongoingChannel = NotificationChannel(
                CHANNEL_ONGOING,
                context.getString(R.string.channel_ongoing_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = context.getString(R.string.channel_ongoing_desc)
                setShowBadge(false)
            }

            notificationManager.createNotificationChannel(alertChannel)
            notificationManager.createNotificationChannel(ongoingChannel)
        }
    }

    fun showOngoingNotification(
        stage: MetabolicStage,
        elapsedMillis: Long,
        remainingMillis: Long,
        progressPercent: Int
    ) {
        val appIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val appPendingIntent = PendingIntent.getActivity(
            context,
            0,
            appIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 수분 +250ml 액션
        val waterIntent = Intent(context, FastingActionReceiver::class.java).apply {
            action = ACTION_ADD_WATER_250
        }
        val waterPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            waterIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 공복 마치기 액션
        val stopIntent = Intent(context, FastingActionReceiver::class.java).apply {
            action = ACTION_STOP_FASTING
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val elapsedStr = FastTimerEngine.formatFullHoursMinutes(elapsedMillis)
        val remainingStr = if (remainingMillis > 0) {
            "목표까지 ${FastTimerEngine.formatRemainingKorean(remainingMillis)}"
        } else {
            "🎉 목표를 달성했어요! 편하게 보식을 시작하세요"
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ONGOING)
            .setSmallIcon(R.drawable.ic_oadan_symbol)
            .setContentTitle("${stage.emoji} ${stage.stageName} 단계 · $elapsedStr")
            .setContentText(remainingStr)
            .setProgress(100, progressPercent.coerceIn(0, 100), false)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(appPendingIntent)
            .addAction(R.drawable.ic_oadan_symbol, "수분 +250ml", waterPendingIntent)
            .addAction(R.drawable.ic_oadan_symbol, "공복 마치기", stopPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setColor(0xFF16796B.toInt())
            .build()

        notificationManager.notify(ONGOING_NOTIFICATION_ID, notification)
    }

    fun dismissOngoingNotification() {
        notificationManager.cancel(ONGOING_NOTIFICATION_ID)
    }

    fun showCompletionAlert(targetHours: Float) {
        val appIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val appPendingIntent = PendingIntent.getActivity(
            context,
            0,
            appIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_FASTING_ALERTS)
            .setSmallIcon(R.drawable.ic_oadan_symbol)
            .setContentTitle("🎉 목표한 ${targetHours.toInt()}시간 공복을 달성했어요!")
            .setContentText("오늘의 오아시스에 도착했어요. 이제 몸을 깨우는 부드러운 보식을 확인해 보세요.")
            .setContentIntent(appPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(0xFFE8875B.toInt())
            .build()

        notificationManager.notify(ALERT_NOTIFICATION_ID, notification)
    }

    fun scheduleExactAlarm(triggerAtMillis: Long, action: String, requestCode: Int) {
        val intent = Intent(context, FastingAlarmReceiver::class.java).apply {
            this.action = action
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    fun cancelAlarms(requestCodes: List<Int>) {
        requestCodes.forEach { code ->
            val intent = Intent(context, FastingAlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                code,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
            }
        }
    }
}
