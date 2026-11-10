package kr.co.oadan.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.oadan.data.local.OadanDatabase
import kr.co.oadan.data.repository.FastingRepository

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val database = OadanDatabase.getDatabase(context)
            val fastingRepository = FastingRepository(database.fastSessionDao())
            val notificationManager = FastingNotificationManager(context)

            CoroutineScope(Dispatchers.IO).launch {
                val active = fastingRepository.getActiveSessionOnce() ?: return@launch
                val now = System.currentTimeMillis()
                if (active.targetEndTimeUtc > now) {
                    notificationManager.scheduleExactAlarm(
                        active.targetEndTimeUtc,
                        FastingNotificationManager.ACTION_COMPLETION_ALERT,
                        101
                    )
                }
            }
        }
    }
}
