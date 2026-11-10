package kr.co.oadan.domain

import kotlin.math.round

enum class Gender(val displayName: String) {
    MALE("남성"),
    FEMALE("여성");

    companion object {
        fun fromString(value: String?): Gender {
            return when (value?.uppercase()) {
                "FEMALE", "WOMAN", "F" -> FEMALE
                else -> MALE
            }
        }
    }
}

/**
 * 키(cm)와 체중(kg)으로부터 추정된 체성분 결과
 */
data class BodyCompositionEstimate(
    val bmi: Float,
    val bodyFatPercent: Float,
    val skeletalMuscleKg: Float,
    val leanBodyMassKg: Float,
    val fatMassKg: Float
)

/**
 * 보통 사람들이 인바디 측정기 없이 골격근량과 체지방률을 모를 때,
 * 체중(kg)과 신장(cm), 성별, 나이를 기반으로 과학적인 생체 수치를 추정하는 유틸리티.
 *
 * - 체지방률 추정: Deurenberg 성인 체지방률 공식 (Deurenberg et al., British Journal of Nutrition)
 *   Body Fat % = 1.20 * BMI + 0.23 * Age - 10.8 * sex - 5.4 (남성: sex=1, 여성: sex=0)
 * - 골격근량 추정: 제지방량(LBM) 기반 골격근 비율 생체 모델 (Janssen et al., Heymsfield et al.)
 *   LBM = Weight * (1 - BodyFat% / 100)
 *   골격근량(SMM) = LBM * (남성: 0.54, 여성: 0.49)
 */
object BodyCompositionEstimator {

    fun estimate(
        heightCm: Float,
        weightKg: Float,
        gender: Gender = Gender.MALE,
        age: Int = 30
    ): BodyCompositionEstimate? {
        if (heightCm < 80f || heightCm > 240f || weightKg < 25f || weightKg > 280f) {
            return null
        }

        val heightM = heightCm / 100f
        val bmi = weightKg / (heightM * heightM)

        val sexCoeff = if (gender == Gender.MALE) 1 else 0
        val clampedAge = age.coerceIn(18, 90)
        val rawFatPercent = 1.20f * bmi + 0.23f * clampedAge - 10.8f * sexCoeff - 5.4f
        val bodyFatPercent = rawFatPercent.coerceIn(5.0f, 55.0f)

        val fatMassKg = weightKg * (bodyFatPercent / 100f)
        val leanBodyMassKg = (weightKg - fatMassKg).coerceAtLeast(10f)

        val muscleRatio = if (gender == Gender.MALE) 0.54f else 0.49f
        val skeletalMuscleKg = (leanBodyMassKg * muscleRatio).coerceAtLeast(5f)

        return BodyCompositionEstimate(
            bmi = round(bmi * 10f) / 10f,
            bodyFatPercent = round(bodyFatPercent * 10f) / 10f,
            skeletalMuscleKg = round(skeletalMuscleKg * 10f) / 10f,
            leanBodyMassKg = round(leanBodyMassKg * 10f) / 10f,
            fatMassKg = round(fatMassKg * 10f) / 10f
        )
    }
}
