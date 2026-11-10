package kr.co.oadan.data.repository

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kr.co.oadan.data.local.ConditionLogDao
import kr.co.oadan.data.local.FastSessionDao
import kr.co.oadan.data.local.MealLogDao
import kr.co.oadan.data.local.WaterLogDao
import kr.co.oadan.data.model.ConditionLog
import kr.co.oadan.data.model.FastSession
import kr.co.oadan.data.model.MealLog
import kr.co.oadan.data.model.WaterLog

data class OadanBackupPayload(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val fastSessions: List<FastSession> = emptyList(),
    val waterLogs: List<WaterLog> = emptyList(),
    val conditionLogs: List<ConditionLog> = emptyList(),
    val mealLogs: List<MealLog> = emptyList()
)

class BackupRepository(
    private val fastSessionDao: FastSessionDao,
    private val waterLogDao: WaterLogDao,
    private val conditionLogDao: ConditionLogDao,
    private val mealLogDao: MealLogDao,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    suspend fun exportDataAsJson(): String {
        val payload = OadanBackupPayload(
            fastSessions = fastSessionDao.getAllSessionsOnce(),
            waterLogs = waterLogDao.getAllWaterLogsOnce(),
            conditionLogs = conditionLogDao.getAllConditionLogsOnce(),
            mealLogs = mealLogDao.getAllMealLogsOnce()
        )
        return gson.toJson(payload)
    }

    suspend fun importDataFromJson(jsonStr: String): Boolean {
        return try {
            val payload = gson.fromJson(jsonStr, OadanBackupPayload::class.java) ?: return false
            payload.fastSessions.forEach { fastSessionDao.insert(it) }
            payload.waterLogs.forEach { waterLogDao.insert(it) }
            payload.conditionLogs.forEach { conditionLogDao.insert(it) }
            payload.mealLogs.forEach { mealLogDao.insert(it) }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun clearAllData() {
        fastSessionDao.deleteAll()
        waterLogDao.deleteAll()
        conditionLogDao.deleteAll()
        mealLogDao.deleteAll()
        userPreferencesRepository.resetAll()
    }
}
