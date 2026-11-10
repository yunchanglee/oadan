package kr.co.oadan

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import kr.co.oadan.data.local.ConditionLogDao
import kr.co.oadan.data.local.FastSessionDao
import kr.co.oadan.data.local.InitialRecipes
import kr.co.oadan.data.local.MealLogDao
import kr.co.oadan.data.local.RecipeDao
import kr.co.oadan.data.local.WaterLogDao
import kr.co.oadan.data.model.ConditionLog
import kr.co.oadan.data.model.FastSession
import kr.co.oadan.data.model.MealLog
import kr.co.oadan.data.model.Recipe
import kr.co.oadan.data.model.WaterLog
import kr.co.oadan.data.repository.ConditionRepository
import kr.co.oadan.data.repository.FastingRepository
import kr.co.oadan.data.repository.MealRepository
import kr.co.oadan.data.repository.OadanBackupPayload
import kr.co.oadan.data.repository.WaterRepository
import kr.co.oadan.domain.CertCardData
import kr.co.oadan.domain.FastTimerEngine
import kr.co.oadan.domain.MetabolicStage
import kr.co.oadan.domain.OrganicGrade
import kr.co.oadan.domain.OrganicScoreCalculator
import kr.co.oadan.ui.screens.PRESET_PLANS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.gson.GsonBuilder

/**
 * 03_OADAN_테스트계획서.md 기반 마스터 검증 테스트 스위트
 * (TC-FR01~07, TC-OA01~03, TC-NFR-01~07, TC-FREE-01, UX 라이팅 금지어 점검)
 */
class OadanTestPlanSuiteTest {

    // --- In-Memory Fake DAOs for Integration Testing ---
    private class FakeFastSessionDao : FastSessionDao {
        private val items = mutableListOf<FastSession>()
        private val flow = MutableStateFlow<List<FastSession>>(emptyList())
        private var nextId = 1L

        private fun sync() {
            flow.value = items.sortedByDescending { it.startTimeUtc }
        }

        override fun getActiveSession(): Flow<FastSession?> =
            flow.map { list -> list.firstOrNull { it.actualEndTimeUtc == null } }

        override suspend fun getActiveSessionOnce(): FastSession? =
            items.sortedByDescending { it.startTimeUtc }.firstOrNull { it.actualEndTimeUtc == null }

        override fun getRecentSessions(limit: Int): Flow<List<FastSession>> =
            flow.map { it.take(limit) }

        override fun getAllSessions(): Flow<List<FastSession>> = flow

        override suspend fun getAllSessionsOnce(): List<FastSession> =
            items.sortedByDescending { it.startTimeUtc }

        override suspend fun getSessionById(id: Long): FastSession? =
            items.firstOrNull { it.id == id }

        override suspend fun insert(session: FastSession): Long {
            val id = if (session.id == 0L) nextId++ else session.id
            items.removeAll { it.id == id }
            items.add(session.copy(id = id))
            sync()
            return id
        }

        override suspend fun update(session: FastSession) {
            val idx = items.indexOfFirst { it.id == session.id }
            if (idx >= 0) {
                items[idx] = session
                sync()
            }
        }

        override suspend fun delete(session: FastSession) {
            items.removeAll { it.id == session.id }
            sync()
        }

        override suspend fun deleteAll() {
            items.clear()
            sync()
        }
    }

    private class FakeWaterLogDao : WaterLogDao {
        private val items = mutableListOf<WaterLog>()
        private val flow = MutableStateFlow<List<WaterLog>>(emptyList())
        private var nextId = 1L

        private fun sync() {
            flow.value = items.toList()
        }

        override fun getLogsForDate(date: String): Flow<List<WaterLog>> =
            flow.map { list -> list.filter { it.date == date }.sortedBy { it.timestampUtc } }

        override fun getTotalWaterForDate(date: String): Flow<Int?> =
            flow.map { list -> list.filter { it.date == date }.sumOf { it.amountMl } }

        override fun getAllWaterLogs(): Flow<List<WaterLog>> = flow

        override suspend fun getAllWaterLogsOnce(): List<WaterLog> = items.toList()

        override suspend fun insert(log: WaterLog): Long {
            val id = if (log.id == 0L) nextId++ else log.id
            items.removeAll { it.id == id }
            items.add(log.copy(id = id))
            sync()
            return id
        }

        override suspend fun delete(log: WaterLog) {
            items.removeAll { it.id == log.id }
            sync()
        }

        override suspend fun deleteAll() {
            items.clear()
            sync()
        }
    }

    private class FakeConditionLogDao : ConditionLogDao {
        private val items = mutableListOf<ConditionLog>()
        private val flow = MutableStateFlow<List<ConditionLog>>(emptyList())
        private var nextId = 1L

        private fun sync() {
            flow.value = items.sortedByDescending { it.timestampUtc }
        }

        override fun getLogsForDate(date: String): Flow<List<ConditionLog>> =
            flow.map { list -> list.filter { it.date == date } }

        override fun getLatestWeightLog(): Flow<ConditionLog?> =
            flow.map { list -> list.firstOrNull { it.weightKg != null } }

        override fun getAllConditionLogs(): Flow<List<ConditionLog>> = flow

        override suspend fun getAllConditionLogsOnce(): List<ConditionLog> = items.toList()

        override suspend fun insert(log: ConditionLog): Long {
            val id = if (log.id == 0L) nextId++ else log.id
            items.removeAll { it.id == id }
            items.add(log.copy(id = id))
            sync()
            return id
        }

        override suspend fun deleteAll() {
            items.clear()
            sync()
        }
    }

    private class FakeMealLogDao : MealLogDao {
        private val items = mutableListOf<MealLog>()
        private val flow = MutableStateFlow<List<MealLog>>(emptyList())
        private var nextId = 1L

        private fun sync() {
            flow.value = items.sortedByDescending { it.timestampUtc }
        }

        override fun getMealLogsForDate(date: String): Flow<List<MealLog>> =
            flow.map { list -> list.filter { it.date == date } }

        override fun getAllMealLogs(): Flow<List<MealLog>> = flow

        override suspend fun getAllMealLogsOnce(): List<MealLog> = items.toList()

        override suspend fun getMealLogById(id: Long): MealLog? = items.firstOrNull { it.id == id }

        override suspend fun getMealLogBySessionId(sessionId: Long): MealLog? =
            items.firstOrNull { it.fastSessionId == sessionId }

        override suspend fun insert(log: MealLog): Long {
            val id = if (log.id == 0L) nextId++ else log.id
            items.removeAll { it.id == id }
            items.add(log.copy(id = id))
            sync()
            return id
        }

        override suspend fun deleteAll() {
            items.clear()
            sync()
        }
    }

    private class FakeRecipeDao : RecipeDao {
        private val items = mutableListOf<Recipe>()
        private val flow = MutableStateFlow<List<Recipe>>(emptyList())

        private fun sync() {
            flow.value = items.toList()
        }

        override fun getAllRecipes(): Flow<List<Recipe>> = flow

        override fun getRecipesByStage(stage: String): Flow<List<Recipe>> =
            flow.map { list -> list.filter { it.stage == stage } }

        override suspend fun getRecipeById(id: String): Recipe? = items.firstOrNull { it.id == id }

        override suspend fun insertAll(recipes: List<Recipe>) {
            recipes.forEach { r ->
                items.removeAll { it.id == r.id }
                items.add(r)
            }
            sync()
        }

        override suspend fun update(recipe: Recipe) {
            val idx = items.indexOfFirst { it.id == recipe.id }
            if (idx >= 0) {
                items[idx] = recipe
                sync()
            }
        }

        override suspend fun count(): Int = items.size
    }

    // =========================================================================
    // A. TC-FR01: 온보딩 및 플랜 설정
    // =========================================================================
    @Test
    fun `TC-FR01-01 & 02 프리셋 플랜 및 추천 보식 강도 매칭 검증`() {
        assertEquals(4, PRESET_PLANS.size)
        val plan14 = PRESET_PLANS.first { it.name == "14:10" }
        val plan16 = PRESET_PLANS.first { it.name == "16:8" }
        val plan18 = PRESET_PLANS.first { it.name == "18:6" }
        val plan20 = PRESET_PLANS.first { it.name == "20:4" }

        assertEquals(14f, plan14.targetHours, 0.01f)
        assertEquals("일반 보식", plan14.recommendedBreakFastStage)

        assertEquals(16f, plan16.targetHours, 0.01f)
        assertEquals("부드러운 보식", plan16.recommendedBreakFastStage)

        assertEquals(18f, plan18.targetHours, 0.01f)
        assertEquals("순한 보식", plan18.recommendedBreakFastStage)

        assertEquals(20f, plan20.targetHours, 0.01f)
        assertEquals("순한 보식", plan20.recommendedBreakFastStage)
    }

    // =========================================================================
    // B. TC-FR02: 오아시스 타이머 및 시간 보정
    // =========================================================================
    @Test
    fun `TC-FR02-01 실시간 경과율 0_25_50_75_100퍼센트 상태 계산 검증`() {
        val start = 1_700_000_000_000L
        val target = start + 16L * 3600_000L

        val s0 = FastTimerEngine.calculateState(start, target, start)
        assertEquals(0.0f, s0.progressRatio, 0.001f)
        assertEquals("00:00:00", s0.formattedElapsed)
        assertEquals(MetabolicStage.STAGE_1, s0.currentStage)

        val s25 = FastTimerEngine.calculateState(start, target, start + 4L * 3600_000L)
        assertEquals(0.25f, s25.progressRatio, 0.001f)
        assertEquals(MetabolicStage.STAGE_2, s25.currentStage)

        val s50 = FastTimerEngine.calculateState(start, target, start + 8L * 3600_000L)
        assertEquals(0.50f, s50.progressRatio, 0.001f)
        assertEquals(MetabolicStage.STAGE_3, s50.currentStage)

        val s75 = FastTimerEngine.calculateState(start, target, start + 12L * 3600_000L)
        assertEquals(0.75f, s75.progressRatio, 0.001f)
        assertEquals(MetabolicStage.STAGE_4, s75.currentStage)

        val s100 = FastTimerEngine.calculateState(start, target, target)
        assertEquals(1.0f, s100.progressRatio, 0.001f)
        assertTrue(s100.isGoalReached)
        assertEquals(MetabolicStage.STAGE_5, s100.currentStage)
        assertNull(s100.nextStage)
    }

    @Test
    fun `TC-FR02-02 시작 및 종료 시간 수동 소급 수정 시 재계산 검증`() = runTest {
        val dao = FakeFastSessionDao()
        val repo = FastingRepository(dao)

        val now = System.currentTimeMillis()
        val id = repo.startFasting("16:8", 16f, customStartTime = now - 2 * 3600_000L)

        // 시작 시간을 4시간 전으로 소급 수정
        val newStart = now - 4 * 3600_000L
        val newTargetEnd = newStart + 16 * 3600_000L
        repo.updateSessionTimes(id, newStart, newTargetEnd)

        val updated = repo.getActiveSessionOnce()
        assertNotNull(updated)
        assertEquals(newStart, updated!!.startTimeUtc)
        assertEquals(newTargetEnd, updated.targetEndTimeUtc)

        val state = FastTimerEngine.calculateState(updated.startTimeUtc, updated.targetEndTimeUtc, now)
        assertEquals(MetabolicStage.STAGE_2, state.currentStage)
    }

    @Test
    fun `TC-FR02-04 72시간 경계값 정확성 검증`() {
        val start = 1_700_000_000_000L
        val target = start + 16L * 3600_000L

        val justUnder72h = FastTimerEngine.calculateState(start, target, start + 72L * 3600_000L - 1000L)
        assertFalse(justUnder72h.isOver72Hours)

        val exact72h = FastTimerEngine.calculateState(start, target, start + 72L * 3600_000L)
        assertTrue(exact72h.isOver72Hours)
    }

    // =========================================================================
    // C. TC-FR03: 생체 대사 단계 타임라인 정각 경계값
    // =========================================================================
    @Test
    fun `TC-FR03-01 4_8_12_16시간 초 단위 경계 전환 및 과학적 근거 포함 검증`() {
        val start = 1_700_000_000_000L
        val target = start + 16L * 3600_000L

        // 3h 59m 59s -> STAGE_1, 4h 00m 00s -> STAGE_2
        val before4h = FastTimerEngine.calculateState(start, target, start + (4 * 3600 - 1) * 1000L)
        val at4h = FastTimerEngine.calculateState(start, target, start + 4 * 3600 * 1000L)
        assertEquals(MetabolicStage.STAGE_1, before4h.currentStage)
        assertEquals(1000L, before4h.nextStageRemainingMillis)
        assertEquals(MetabolicStage.STAGE_2, at4h.currentStage)

        // 7h 59m 59s -> STAGE_2, 8h 00m 00s -> STAGE_3
        val before8h = FastTimerEngine.calculateState(start, target, start + (8 * 3600 - 1) * 1000L)
        val at8h = FastTimerEngine.calculateState(start, target, start + 8 * 3600 * 1000L)
        assertEquals(MetabolicStage.STAGE_2, before8h.currentStage)
        assertEquals(MetabolicStage.STAGE_3, at8h.currentStage)

        // 11h 59m 59s -> STAGE_3, 12h 00m 00s -> STAGE_4
        val before12h = FastTimerEngine.calculateState(start, target, start + (12 * 3600 - 1) * 1000L)
        val at12h = FastTimerEngine.calculateState(start, target, start + 12 * 3600 * 1000L)
        assertEquals(MetabolicStage.STAGE_3, before12h.currentStage)
        assertEquals(MetabolicStage.STAGE_4, at12h.currentStage)

        // 15h 59m 59s -> STAGE_4, 16h 00m 00s -> STAGE_5
        val before16h = FastTimerEngine.calculateState(start, target, start + (16 * 3600 - 1) * 1000L)
        val at16h = FastTimerEngine.calculateState(start, target, start + 16 * 3600 * 1000L)
        assertEquals(MetabolicStage.STAGE_4, before16h.currentStage)
        assertEquals(MetabolicStage.STAGE_5, at16h.currentStage)

        // 모든 단계에 과학적 근거와 비어 있지 않은 설명 존재 확인 (TC-FR03-02)
        MetabolicStage.values().forEach { stage ->
            assertTrue(stage.description.isNotBlank())
            assertTrue(stage.scientificBasis.isNotBlank())
        }
    }

    // =========================================================================
    // D. TC-FR04: 수분·체중·컨디션 멀티 트래킹
    // =========================================================================
    @Test
    fun `TC-FR04-01 & 03 수분 누적 합산 및 체중_컨디션 기록 검증`() = runTest {
        val waterDao = FakeWaterLogDao()
        val waterRepo = WaterRepository(waterDao)

        assertEquals(0, waterRepo.getTodayTotalWaterMl().first())
        waterRepo.addWater(250)
        waterRepo.addWater(500)
        waterRepo.addWater(150)
        assertEquals(900, waterRepo.getTodayTotalWaterMl().first())

        val condDao = FakeConditionLogDao()
        val condRepo = ConditionRepository(condDao)
        condRepo.recordCondition(
            weightKg = 64.2f,
            skeletalMuscleKg = 26.5f,
            bodyFatPercent = 21.0f,
            conditionTag = "😊 상쾌",
            memo = "공복 컨디션 좋음"
        )
        val latest = condRepo.latestWeightLog.first()
        assertNotNull(latest)
        assertEquals(64.2f, latest!!.weightKg!!, 0.01f)
        assertEquals("😊 상쾌", latest.conditionTag)
    }

    // =========================================================================
    // E. TC-FR05: 중도 포기 방어 및 '부분 달성' 처리
    // =========================================================================
    @Test
    fun `TC-FR05-02 중도 종료 시 실패가 아닌 부분 달성 기록 및 목표 단축 검증`() = runTest {
        val dao = FakeFastSessionDao()
        val repo = FastingRepository(dao)

        val now = System.currentTimeMillis()
        // 16시간 목표 중 14시간 경과
        repo.startFasting("16:8", 16f, customStartTime = now - 14 * 3600_000L)

        // 선택지 C: 부분 달성으로 마무리
        val endedPartial = repo.endFasting(actualEndTime = now, isPartial = true)
        assertNotNull(endedPartial)
        assertFalse(endedPartial!!.isCompleted)
        assertTrue(endedPartial.isPartiallyCompleted)
        assertEquals(14f, endedPartial.fastedHours!!, 0.1f)

        // 선택지 B: 목표 시간을 14시간으로 줄인 뒤 종료 -> 정상 달성(isCompleted = true)
        repo.startFasting("16:8", 16f, customStartTime = now - 14 * 3600_000L)
        repo.updateActiveSessionTarget(14f)
        val endedReduced = repo.endFasting(actualEndTime = now, isPartial = false)
        assertNotNull(endedReduced)
        assertTrue(endedReduced!!.isCompleted)
        assertFalse(endedReduced.isPartiallyCompleted)
    }

    // =========================================================================
    // F. TC-OA01~03: 오가닉 보식 추천 · 기록 · 인증 카드
    // =========================================================================
    @Test
    fun `TC-OA01-01 단식 시간별 4단계 보식 추천 로직 매칭 검증`() = runTest {
        val mealRepo = MealRepository(FakeMealLogDao(), FakeRecipeDao())
        mealRepo.ensureInitialRecipes()

        assertEquals("일반 보식", mealRepo.getRecommendedStage(13f))
        assertEquals("부드러운 보식", mealRepo.getRecommendedStage(14f))
        assertEquals("부드러운 보식", mealRepo.getRecommendedStage(16f))
        assertEquals("순한 보식", mealRepo.getRecommendedStage(18f))
        assertEquals("순한 보식", mealRepo.getRecommendedStage(20f))
        assertEquals("회복 보식", mealRepo.getRecommendedStage(24f))
        assertEquals("회복 보식", mealRepo.getRecommendedStage(26f))

        // 해당 시간대 추천 시 권장 단계 레시피가 최상단에 정렬되는지 확인
        val listFor26h = mealRepo.getRecipesForHours(26f).first()
        assertEquals("회복 보식", listFor26h.first().stage)
    }

    @Test
    fun `TC-OA02-01 오가닉 스코어 전 조합(32가지) 계산 및 부정적 피드백 문구 0건 검증`() {
        val forbiddenWords = listOf("나쁜", "실패", "경고", "과다", "위험", "최악", "부족", "금지")
        val booleans = listOf(false, true)

        for (b1 in booleans) for (b2 in booleans) for (b3 in booleans) for (b4 in booleans) for (b5 in booleans) {
            val eval = OrganicScoreCalculator.calculate(b1, b2, b3, b4, b5)
            val expectedGrade = when {
                eval.score >= 80 -> OrganicGrade.OASIS
                eval.score >= 50 -> OrganicGrade.LEAF
                else -> OrganicGrade.SPROUT
            }
            assertEquals(expectedGrade, eval.grade)
            assertTrue(eval.feedbackMessage.isNotBlank())
            forbiddenWords.forEach { bad ->
                assertFalse(
                    "부정적 단어 '$bad' 포함됨: ${eval.feedbackMessage}",
                    eval.feedbackMessage.contains(bad)
                )
            }
        }
    }

    @Test
    fun `TC-OA03-01 인증 카드 데이터 기본 프라이버시(체중 비노출) 검증`() {
        val defaultCard = CertCardData(
            fastedTimeString = "16h 12m",
            stage = MetabolicStage.STAGE_5,
            organicGrade = "오아시스",
            streakDays = 7
        )
        // 체중 변화 표시는 반드시 기본 OFF여야 함
        assertFalse(defaultCard.showWeightChange)
        assertTrue(defaultCard.showOrganicScore)
    }

    // =========================================================================
    // G. TC-FR06 & TC-FR07: 스트릭 계산 및 장시간 위젯 진행률
    // =========================================================================
    @Test
    fun `TC-FR06-01 연속 달성일(Streak) 계산 검증`() = runTest {
        val dao = FakeFastSessionDao()
        val repo = FastingRepository(dao)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val now = System.currentTimeMillis()

        // 오늘, 어제, 그제 3일 연속 달성 세션 입력
        for (dayOffset in 0..2) {
            val start = now - dayOffset * 86_400_000L - 16 * 3600_000L
            val end = start + 16 * 3600_000L
            dao.insert(
                FastSession(
                    startTimeUtc = start,
                    targetEndTimeUtc = end,
                    actualEndTimeUtc = end,
                    targetHours = 16f,
                    fastedHours = 16f,
                    isCompleted = true,
                    date = sdf.format(Date(start))
                )
            )
        }

        val streak = repo.calculateStreak().first()
        assertEquals(3, streak)
    }

    @Test
    fun `TC-FR07-01 장시간 프로토콜(20_4, OMAD, 48h)에서 16시간 경과 시 100퍼센트 조기 고정 방지 검증`() {
        val start = 1_700_000_000_000L
        val elapsed16h = start + 16L * 3600_000L

        // 48시간 단식 중 16시간 경과 -> 33.3% (100% 고정 금지)
        val state48h = FastTimerEngine.calculateState(
            startTimeUtc = start,
            targetEndTimeUtc = start + 48L * 3600_000L,
            currentTimeUtc = elapsed16h
        )
        assertEquals(16f / 48f, state48h.progressRatio, 0.001f)
        assertFalse(state48h.isGoalReached)

        // 20:4 단식 중 16시간 경과 -> 80.0%
        val state20h = FastTimerEngine.calculateState(
            startTimeUtc = start,
            targetEndTimeUtc = start + 20L * 3600_000L,
            currentTimeUtc = elapsed16h
        )
        assertEquals(0.80f, state20h.progressRatio, 0.001f)
        assertFalse(state20h.isGoalReached)
    }

    // =========================================================================
    // H. TC-NFR & TC-FREE: 비기능 요구사항 및 전면 무료/무광고 정적 검증
    // =========================================================================
    @Test
    fun `TC-NFR-01 & 03 UTC 타임스탬프 기반 상태 계산으로 타임존 무관 오차 0초 검증`() {
        val startUtc = 1_700_000_000_000L
        val targetEndUtc = startUtc + 16L * 3600_000L
        val after5hUtc = startUtc + 5L * 3600_000L

        val originalTz = java.util.TimeZone.getDefault()
        try {
            java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("Asia/Seoul"))
            val stateSeoul = FastTimerEngine.calculateState(startUtc, targetEndUtc, after5hUtc)

            java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("America/New_York"))
            val stateNy = FastTimerEngine.calculateState(startUtc, targetEndUtc, after5hUtc)

            assertEquals(stateSeoul.elapsedMillis, stateNy.elapsedMillis)
            assertEquals(stateSeoul.remainingMillis, stateNy.remainingMillis)
            assertEquals(stateSeoul.formattedElapsed, stateNy.formattedElapsed)
            assertEquals("05:00:00", stateNy.formattedElapsed)
        } finally {
            java.util.TimeZone.setDefault(originalTz)
        }
    }

    @Test
    fun `TC-NFR-05 JSON 백업 직렬화 및 복원 정합성 100퍼센트 검증`() = runTest {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val original = OadanBackupPayload(
            fastSessions = listOf(
                FastSession(
                    id = 1,
                    startTimeUtc = 1000L,
                    targetEndTimeUtc = 58600000L,
                    actualEndTimeUtc = 58600000L,
                    protocol = "16:8",
                    targetHours = 16f,
                    fastedHours = 16f,
                    isCompleted = true,
                    date = "2026-11-10"
                )
            ),
            waterLogs = listOf(WaterLog(id = 1, timestampUtc = 2000L, amountMl = 250, date = "2026-11-10")),
            conditionLogs = listOf(ConditionLog(id = 1, timestampUtc = 3000L, date = "2026-11-10", weightKg = 62.5f, conditionTag = "😊 상쾌")),
            mealLogs = listOf(MealLog(id = 1, fastSessionId = 1, date = "2026-11-10", menuName = "두부 채소 비빔 볼", organicScore = 80, organicGrade = "오아시스"))
        )

        val json = gson.toJson(original)
        val restored = gson.fromJson(json, OadanBackupPayload::class.java)

        assertEquals(original.fastSessions, restored.fastSessions)
        assertEquals(original.waterLogs, restored.waterLogs)
        assertEquals(original.conditionLogs, restored.conditionLogs)
        assertEquals(original.mealLogs, restored.mealLogs)
    }

    @Test
    fun `TC-FREE-01 & UX 라이팅 금지어 정적 코드 전수 검사`() {
        val projectRoot = File("..").canonicalFile
        val buildGradle = File(projectRoot, "app/build.gradle.kts").readText()
        val manifest = File(projectRoot, "app/src/main/AndroidManifest.xml").readText()

        // 1. 결제/광고 SDK 및 INTERNET 권한 미포함 검증 (Local-First & 전면 무료/무광고)
        val forbiddenSdks = listOf("com.android.billingclient", "play-services-ads", "admob", "facebook-android-sdk")
        forbiddenSdks.forEach { sdk ->
            assertFalse("금지된 결제/광고 SDK 발견: $sdk", buildGradle.contains(sdk, ignoreCase = true))
        }
        assertFalse("불필요한 INTERNET 권한이 포함됨", manifest.contains("android.permission.INTERNET"))

        // 2. 앱 소스 코드 내 UX 라이팅 금지어("단식 실패", "나쁜 음식", "칼로리 과다", "절대 먹지 마세요") 0건 검증
        val srcDir = File(projectRoot, "app/src/main")
        val forbiddenPhrases = listOf("단식 실패", "나쁜 음식", "나쁜 식단", "칼로리 과다", "절대 먹지 마세요")
        srcDir.walkTopDown().filter { it.isFile && (it.extension == "kt" || it.extension == "xml") }.forEach { file ->
            val text = file.readText()
            forbiddenPhrases.forEach { phrase ->
                assertFalse("파일 ${file.name} 에 UX 라이팅 금지어 '$phrase' 포함됨", text.contains(phrase))
            }
        }
    }
}
