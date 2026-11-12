package kr.co.oadan.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kr.co.oadan.data.model.ConditionLog

@Dao
interface ConditionLogDao {
    @Query("SELECT * FROM condition_logs WHERE date = :date ORDER BY timestampUtc DESC")
    fun getLogsForDate(date: String): Flow<List<ConditionLog>>

    @Query("SELECT * FROM condition_logs WHERE weightKg IS NOT NULL ORDER BY timestampUtc DESC LIMIT 1")
    fun getLatestWeightLog(): Flow<ConditionLog?>

    @Query("SELECT * FROM condition_logs ORDER BY timestampUtc DESC")
    fun getAllConditionLogs(): Flow<List<ConditionLog>>

    @Query("SELECT * FROM condition_logs ORDER BY timestampUtc DESC")
    suspend fun getAllConditionLogsOnce(): List<ConditionLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: ConditionLog): Long

    @Query("DELETE FROM condition_logs WHERE date = :date")
    suspend fun deleteLogsForDate(date: String)

    @Query("DELETE FROM condition_logs")
    suspend fun deleteAll()
}
