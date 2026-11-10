package kr.co.oadan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_logs")
data class MealLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fastSessionId: Long? = null,
    val timestampUtc: Long = System.currentTimeMillis(),
    val date: String, // YYYY-MM-DD
    val menuName: String,
    val photoUri: String? = null,
    val checkVegFirst: Boolean = false,
    val checkNoProcessed: Boolean = false,
    val checkSlow20m: Boolean = false,
    val checkNoSweetDrink: Boolean = false,
    val checkSeasonal: Boolean = false,
    val organicScore: Int = 0,
    val organicGrade: String = "새싹", // 새싹, 잎새, 오아시스
    val feedbackMessage: String = "",
    val notes: String? = null
)
