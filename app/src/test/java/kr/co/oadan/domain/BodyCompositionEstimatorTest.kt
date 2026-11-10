package kr.co.oadan.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BodyCompositionEstimatorTest {

    @Test
    fun testEstimateStandardMale() {
        // 키 175cm, 체중 70kg, 남성, 30세
        val result = BodyCompositionEstimator.estimate(
            heightCm = 175f,
            weightKg = 70f,
            gender = Gender.MALE,
            age = 30
        )

        assertNotNull(result)
        result!!

        // BMI = 70 / 1.75^2 = 22.86 -> 22.9
        assertEquals(22.9f, result.bmi, 0.1f)

        // Deurenberg 성인 남성 체지방률: 약 18.1%
        assertTrue("체지방률이 정상 남성 범위(15~22%) 내여야 함", result.bodyFatPercent in 15.0f..22.0f)

        // 골격근량: 약 31kg (제지방량 약 57kg * 0.54)
        assertTrue("골격근량이 정상 남성 범위(28~34kg) 내여야 함", result.skeletalMuscleKg in 28.0f..34.0f)

        // 제지방량 + 체지방량 합산이 체중과 일치해야 함
        assertEquals(70f, result.leanBodyMassKg + result.fatMassKg, 0.5f)
    }

    @Test
    fun testEstimateStandardFemale() {
        // 키 160cm, 체중 52kg, 여성, 30세
        val result = BodyCompositionEstimator.estimate(
            heightCm = 160f,
            weightKg = 52f,
            gender = Gender.FEMALE,
            age = 30
        )

        assertNotNull(result)
        result!!

        // BMI = 52 / 1.6^2 = 20.31 -> 20.3
        assertEquals(20.3f, result.bmi, 0.1f)

        // Deurenberg 성인 여성 체지방률: 약 25.9%
        assertTrue("여성 체지방률이 정상 범위(22~28%) 내여야 함", result.bodyFatPercent in 22.0f..28.0f)

        // 골격근량: 약 18.9kg (제지방량 약 38.5kg * 0.49)
        assertTrue("여성 골격근량이 정상 범위(16~22kg) 내여야 함", result.skeletalMuscleKg in 16.0f..22.0f)
    }

    @Test
    fun testEstimateExtremeInputsProtection() {
        // 유효하지 않은 키/체중 (0cm, 비현실적인 값 등)
        assertNull(BodyCompositionEstimator.estimate(heightCm = 0f, weightKg = 70f))
        assertNull(BodyCompositionEstimator.estimate(heightCm = 170f, weightKg = 10f))
        assertNull(BodyCompositionEstimator.estimate(heightCm = 300f, weightKg = 70f))
        assertNull(BodyCompositionEstimator.estimate(heightCm = 170f, weightKg = 500f))
    }

    @Test
    fun testGenderParsing() {
        assertEquals(Gender.MALE, Gender.fromString("male"))
        assertEquals(Gender.MALE, Gender.fromString("MALE"))
        assertEquals(Gender.FEMALE, Gender.fromString("female"))
        assertEquals(Gender.FEMALE, Gender.fromString("WOMAN"))
        assertEquals(Gender.FEMALE, Gender.fromString("F"))
        assertEquals(Gender.MALE, Gender.fromString(null))
    }
}
