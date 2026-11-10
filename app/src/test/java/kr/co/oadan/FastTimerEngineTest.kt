package kr.co.oadan

import kr.co.oadan.domain.FastTimerEngine
import kr.co.oadan.domain.MetabolicStage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FastTimerEngineTest {

    @Test
    fun testMetabolicStagesTransition() {
        assertEquals(MetabolicStage.STAGE_1, MetabolicStage.fromElapsedHours(0f))
        assertEquals(MetabolicStage.STAGE_1, MetabolicStage.fromElapsedHours(3.9f))
        assertEquals(MetabolicStage.STAGE_2, MetabolicStage.fromElapsedHours(4.0f))
        assertEquals(MetabolicStage.STAGE_2, MetabolicStage.fromElapsedHours(7.9f))
        assertEquals(MetabolicStage.STAGE_3, MetabolicStage.fromElapsedHours(8.0f))
        assertEquals(MetabolicStage.STAGE_3, MetabolicStage.fromElapsedHours(11.9f))
        assertEquals(MetabolicStage.STAGE_4, MetabolicStage.fromElapsedHours(12.0f))
        assertEquals(MetabolicStage.STAGE_4, MetabolicStage.fromElapsedHours(15.9f))
        assertEquals(MetabolicStage.STAGE_5, MetabolicStage.fromElapsedHours(16.0f))
        assertEquals(MetabolicStage.STAGE_5, MetabolicStage.fromElapsedHours(24.0f))
    }

    @Test
    fun testTimerStateCalculation_Halfway() {
        val startUtc = 1000000000000L
        val targetHours = 16f
        val durationMillis = (targetHours * 3600f * 1000f).toLong()
        val targetEndUtc = startUtc + durationMillis

        // 8시간 경과 (50% 진행)
        val currentUtc = startUtc + (8f * 3600f * 1000f).toLong()

        val state = FastTimerEngine.calculateState(
            startTimeUtc = startUtc,
            targetEndTimeUtc = targetEndUtc,
            currentTimeUtc = currentUtc
        )

        assertEquals(0.5f, state.progressRatio, 0.001f)
        assertFalse(state.isGoalReached)
        assertFalse(state.isOver72Hours)
        assertEquals(MetabolicStage.STAGE_3, state.currentStage)
        assertEquals("08:00:00", state.formattedElapsed)
        assertEquals("8시간 0분", state.formattedRemaining)
    }

    @Test
    fun testTimerStateCalculation_GoalReachedAndBonus() {
        val startUtc = 1000000000000L
        val targetHours = 16f
        val durationMillis = (targetHours * 3600f * 1000f).toLong()
        val targetEndUtc = startUtc + durationMillis

        // 18시간 경과 (목표 16시간 달성 후 2시간 초과)
        val currentUtc = startUtc + (18f * 3600f * 1000f).toLong()

        val state = FastTimerEngine.calculateState(
            startTimeUtc = startUtc,
            targetEndTimeUtc = targetEndUtc,
            currentTimeUtc = currentUtc
        )

        assertTrue(state.isGoalReached)
        assertTrue(state.progressRatio > 1.0f)
        assertEquals(MetabolicStage.STAGE_5, state.currentStage)
        assertEquals("18:00:00", state.formattedElapsed)
        assertEquals("목표 달성!", state.formattedRemaining)
    }

    @Test
    fun testTimerStateCalculation_Over72HoursDetection() {
        val startUtc = 1000000000000L
        val targetHours = 16f
        val targetEndUtc = startUtc + (targetHours * 3600f * 1000f).toLong()

        // 73시간 경과
        val currentUtc = startUtc + (73f * 3600f * 1000f).toLong()

        val state = FastTimerEngine.calculateState(
            startTimeUtc = startUtc,
            targetEndTimeUtc = targetEndUtc,
            currentTimeUtc = currentUtc
        )

        assertTrue(state.isOver72Hours)
    }
}
