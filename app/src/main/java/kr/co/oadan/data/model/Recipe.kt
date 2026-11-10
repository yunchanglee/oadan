package kr.co.oadan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey
    val id: String,
    val title: String,
    val subtitle: String,
    val stage: String, // 일반 보식, 부드러운 보식, 순한 보식, 회복 보식
    val emoji: String,
    val prepTimeMinutes: Int,
    val glycemicFriendly: String, // 낮음, 보통, 높음
    val tags: List<String>,
    val eatOrderGuide: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val seasonInfo: String? = null,
    val isBookmarked: Boolean = false
)
