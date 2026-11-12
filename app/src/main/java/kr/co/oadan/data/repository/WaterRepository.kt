package kr.co.oadan.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.co.oadan.data.local.WaterLogDao
import kr.co.oadan.data.model.WaterLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WaterRepository(private val waterLogDao: WaterLogDao) {

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun getTodayWaterLogs(): Flow<List<WaterLog>> {
        return waterLogDao.getLogsForDate(getTodayDateString())
    }

    fun getTodayTotalWaterMl(): Flow<Int> {
        return waterLogDao.getTotalWaterForDate(getTodayDateString()).map { it ?: 0 }
    }

    suspend fun addWater(amountMl: Int = 250, date: String? = null): Long {
        val log = WaterLog(
            timestampUtc = System.currentTimeMillis(),
            amountMl = amountMl,
            date = date ?: getTodayDateString()
        )
        return waterLogDao.insert(log)
    }

    suspend fun setWaterForDate(date: String, totalMl: Int) {
        waterLogDao.deleteLogsForDate(date)
        if (totalMl > 0) {
            val log = WaterLog(
                timestampUtc = System.currentTimeMillis(),
                amountMl = totalMl,
                date = date
            )
            waterLogDao.insert(log)
        }
    }

    fun getAllLogs(): Flow<List<WaterLog>> = waterLogDao.getAllWaterLogs()

    suspend fun getAllLogsOnce(): List<WaterLog> = waterLogDao.getAllWaterLogsOnce()
}
