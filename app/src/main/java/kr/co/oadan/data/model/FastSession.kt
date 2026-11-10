package kr.co.oadan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fast_sessions")
data class FastSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTimeUtc: Long,
    val targetEndTimeUtc: Long,
    val actualEndTimeUtc: Long? = null,
    val protocol: String = "16:8",
    val targetHours: Float = 16f,
    val fastedHours: Float? = null,
    val isCompleted: Boolean = false,
    val isPartiallyCompleted: Boolean = false,
    val date: String, // YYYY-MM-DD
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
