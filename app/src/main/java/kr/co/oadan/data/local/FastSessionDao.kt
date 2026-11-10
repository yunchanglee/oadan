package kr.co.oadan.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kr.co.oadan.data.model.FastSession

@Dao
interface FastSessionDao {
    @Query("SELECT * FROM fast_sessions WHERE actualEndTimeUtc IS NULL ORDER BY startTimeUtc DESC LIMIT 1")
    fun getActiveSession(): Flow<FastSession?>

    @Query("SELECT * FROM fast_sessions WHERE actualEndTimeUtc IS NULL ORDER BY startTimeUtc DESC LIMIT 1")
    suspend fun getActiveSessionOnce(): FastSession?

    @Query("SELECT * FROM fast_sessions ORDER BY startTimeUtc DESC LIMIT :limit")
    fun getRecentSessions(limit: Int = 10): Flow<List<FastSession>>

    @Query("SELECT * FROM fast_sessions ORDER BY startTimeUtc DESC")
    fun getAllSessions(): Flow<List<FastSession>>

    @Query("SELECT * FROM fast_sessions ORDER BY startTimeUtc DESC")
    suspend fun getAllSessionsOnce(): List<FastSession>

    @Query("SELECT * FROM fast_sessions WHERE id = :id LIMIT 1")
    suspend fun getSessionById(id: Long): FastSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: FastSession): Long

    @Update
    suspend fun update(session: FastSession)

    @Delete
    suspend fun delete(session: FastSession)

    @Query("DELETE FROM fast_sessions")
    suspend fun deleteAll()
}
