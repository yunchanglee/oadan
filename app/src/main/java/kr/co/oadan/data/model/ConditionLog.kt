package kr.co.oadan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "condition_logs")
data class ConditionLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestampUtc: Long = System.currentTimeMillis(),
    val date: String, // YYYY-MM-DD
    val weightKg: Float? = null,
    val skeletalMuscleKg: Float? = null,
    val bodyFatPercent: Float? = null,
    val conditionTag: String? = null, // 😊 상쾌, 😐 보통, 😣 배고픔, 🤕 두통, 💫 어지러움
    val memo: String? = null
)
