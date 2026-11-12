package kr.co.oadan.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kr.co.oadan.data.model.WaterLog

@Dao
interface WaterLogDao {
    @Query("SELECT * FROM water_logs WHERE date = :date ORDER BY timestampUtc ASC")
    fun getLogsForDate(date: String): Flow<List<WaterLog>>

    @Query("SELECT SUM(amountMl) FROM water_logs WHERE date = :date")
    fun getTotalWaterForDate(date: String): Flow<Int?>

    @Query("SELECT * FROM water_logs ORDER BY timestampUtc DESC")
    fun getAllWaterLogs(): Flow<List<WaterLog>>

    @Query("SELECT * FROM water_logs ORDER BY timestampUtc DESC")
    suspend fun getAllWaterLogsOnce(): List<WaterLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: WaterLog): Long

    @Delete
    suspend fun delete(log: WaterLog)

    @Query("DELETE FROM water_logs WHERE date = :date")
    suspend fun deleteLogsForDate(date: String)

    @Query("DELETE FROM water_logs")
    suspend fun deleteAll()
}
