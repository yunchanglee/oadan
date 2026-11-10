package kr.co.oadan.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.oadan.data.local.OadanDatabase
import kr.co.oadan.data.repository.FastingRepository
import kr.co.oadan.domain.EncouragementMessageProvider

class FastingAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        val database = OadanDatabase.getDatabase(context)
        val fastingRepository = FastingRepository(database.fastSessionDao())
        val notificationManager = FastingNotificationManager(context)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val active = fastingRepository.getActiveSessionOnce() ?: return@launch
                val now = System.currentTimeMillis()
                val elapsedHours = ((now - active.startTimeUtc) / (3600f * 1000f)).toInt()

                when (intent.action) {
                    FastingNotificationManager.ACTION_COMPLETION_ALERT -> {
                        notificationManager.showCompletionAlert(active.targetHours)
                    }
                    FastingNotificationManager.ACTION_MID_FAST_CHEER -> {
                        val cheer = EncouragementMessageProvider.getMidFastMessage(elapsedHours)
                        notificationManager.showEncouragementAlert(
                            title = cheer.title,
                            message = cheer.message,
                            isOverachieved = false
                        )
                    }
                    FastingNotificationManager.ACTION_OVERACHIEVE_CHEER -> {
                        val overachievedHours = ((now - active.targetEndTimeUtc) / (3600f * 1000f)).toInt().coerceAtLeast(1)
                        val cheer = EncouragementMessageProvider.getOverachievedMessage(overachievedHours)
                        notificationManager.showEncouragementAlert(
                            title = cheer.title,
                            message = cheer.message,
                            isOverachieved = true
                        )
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
