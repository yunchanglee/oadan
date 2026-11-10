package kr.co.oadan.domain

import androidx.compose.ui.graphics.Color
import kr.co.oadan.ui.theme.ScoreLeaf
import kr.co.oadan.ui.theme.ScoreOasis
import kr.co.oadan.ui.theme.ScoreSprout

enum class OrganicGrade(
    val title: String,
    val emoji: String,
    val color: Color,
    val minScore: Int,
    val maxScore: Int
) {
    SPROUT("새싹", "🌱", ScoreSprout, 0, 49),
    LEAF("잎새", "🌿", ScoreLeaf, 50, 79),
    OASIS("오아시스", "🌳", ScoreOasis, 80, 100);

    companion object {
        fun fromScore(score: Int): OrganicGrade {
            return when {
                score >= 80 -> OASIS
                score >= 50 -> LEAF
                else -> SPROUT
            }
        }
    }
}

data class OrganicEvaluation(
    val score: Int,
    val grade: OrganicGrade,
    val feedbackMessage: String
)

object OrganicScoreCalculator {

    fun calculate(
        checkVegFirst: Boolean,
        checkNoProcessed: Boolean,
        checkSlow20m: Boolean,
        checkNoSweetDrink: Boolean,
        checkSeasonal: Boolean
    ): OrganicEvaluation {
        var score = 0
        if (checkVegFirst) score += 20
        if (checkNoProcessed) score += 20
        if (checkSlow20m) score += 20
        if (checkNoSweetDrink) score += 20
        if (checkSeasonal) score += 20

        val grade = OrganicGrade.fromScore(score)

        val feedback = when {
            checkVegFirst && score >= 80 -> "채소를 먼저 드셨네요! 혈당이 천천히 오를 거예요 🌿"
            checkSlow20m && score >= 80 -> "20분 이상 천천히 드셔 위의 부담을 덜어주었어요 💧"
            checkNoProcessed && score >= 80 -> "가공식품 없는 깨끗한 자연식으로 오아시스를 채웠어요 🌳"
            score >= 80 -> "완벽한 첫 끼예요! 몸이 자연의 영양을 온전히 흡수하고 있어요 ✨"
            checkVegFirst -> "식이섬유를 먼저 채워주어 혈당 스파이크를 잘 방어했어요 🌱"
            checkSlow20m -> "천천히 꼭꼭 씹어 소화기를 편안하게 깨워주었어요 🌿"
            checkNoSweetDrink -> "단 음료를 멀리하여 맑은 수분으로 속을 비웠어요 💧"
            score >= 50 -> "몸을 아끼는 건강한 선택을 하셨네요. 다음 끼니도 천천히 함께해요 🌿"
            else -> "공복을 마치고 첫 끼를 채운 것만으로도 소중한 시작이에요 🌱"
        }

        return OrganicEvaluation(
            score = score,
            grade = grade,
            feedbackMessage = feedback
        )
    }
}
