package kr.co.oadan

import kr.co.oadan.domain.OrganicGrade
import kr.co.oadan.domain.OrganicScoreCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OrganicScoreCalculatorTest {

    @Test
    fun testAllChecksPassed_OasisGrade() {
        val result = OrganicScoreCalculator.calculate(
            checkVegFirst = true,
            checkNoProcessed = true,
            checkSlow20m = true,
            checkNoSweetDrink = true,
            checkSeasonal = true
        )

        assertEquals(100, result.score)
        assertEquals(OrganicGrade.OASIS, result.grade)
        assertTrue(result.feedbackMessage.isNotEmpty())
        assertFalse(result.feedbackMessage.contains("나쁜"))
        assertFalse(result.feedbackMessage.contains("실패"))
    }

    @Test
    fun testPartialChecks_LeafGrade() {
        val result = OrganicScoreCalculator.calculate(
            checkVegFirst = true,
            checkNoProcessed = true,
            checkSlow20m = true,
            checkNoSweetDrink = false,
            checkSeasonal = false
        )

        assertEquals(60, result.score)
        assertEquals(OrganicGrade.LEAF, result.grade)
    }

    @Test
    fun testLowScore_SproutGrade() {
        val result = OrganicScoreCalculator.calculate(
            checkVegFirst = false,
            checkNoProcessed = false,
            checkSlow20m = true,
            checkNoSweetDrink = false,
            checkSeasonal = false
        )

        assertEquals(20, result.score)
        assertEquals(OrganicGrade.SPROUT, result.grade)
        assertTrue(result.feedbackMessage.contains("천천히") || result.feedbackMessage.contains("시작"))
    }
}
