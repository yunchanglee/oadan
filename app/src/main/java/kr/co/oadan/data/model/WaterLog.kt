package kr.co.oadan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_logs")
data class WaterLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestampUtc: Long = System.currentTimeMillis(),
    val amountMl: Int = 250,
    val date: String // YYYY-MM-DD
)
