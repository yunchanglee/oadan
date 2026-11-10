package kr.co.oadan.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kr.co.oadan.data.model.MealLog

@Dao
interface MealLogDao {
    @Query("SELECT * FROM meal_logs WHERE date = :date ORDER BY timestampUtc DESC")
    fun getMealLogsForDate(date: String): Flow<List<MealLog>>

    @Query("SELECT * FROM meal_logs ORDER BY timestampUtc DESC")
    fun getAllMealLogs(): Flow<List<MealLog>>

    @Query("SELECT * FROM meal_logs ORDER BY timestampUtc DESC")
    suspend fun getAllMealLogsOnce(): List<MealLog>

    @Query("SELECT * FROM meal_logs WHERE id = :id LIMIT 1")
    suspend fun getMealLogById(id: Long): MealLog?

    @Query("SELECT * FROM meal_logs WHERE fastSessionId = :sessionId LIMIT 1")
    suspend fun getMealLogBySessionId(sessionId: Long): MealLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: MealLog): Long

    @Query("DELETE FROM meal_logs")
    suspend fun deleteAll()
}
