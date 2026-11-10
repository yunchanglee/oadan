package kr.co.oadan.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.oadan.data.local.OadanDatabase
import kr.co.oadan.data.repository.FastingRepository
import kr.co.oadan.data.repository.WaterRepository

class FastingActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val database = OadanDatabase.getDatabase(context)
        val fastingRepository = FastingRepository(database.fastSessionDao())
        val waterRepository = WaterRepository(database.waterLogDao())
        val notificationManager = FastingNotificationManager(context)

        CoroutineScope(Dispatchers.IO).launch {
            when (intent.action) {
                FastingNotificationManager.ACTION_ADD_WATER_250 -> {
                    waterRepository.addWater(250)
                }
                FastingNotificationManager.ACTION_STOP_FASTING -> {
                    fastingRepository.endFasting()
                    notificationManager.dismissOngoingNotification()
                }
            }
        }
    }
}
