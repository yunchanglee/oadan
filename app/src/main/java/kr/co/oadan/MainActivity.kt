package kr.co.oadan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kr.co.oadan.data.model.Recipe
import kr.co.oadan.ui.OadanViewModel
import kr.co.oadan.ui.components.OadanBottomNavigation
import kr.co.oadan.ui.components.OadanTab
import kr.co.oadan.ui.screens.BreakFastHomeScreen
import kr.co.oadan.ui.screens.BreakFastRecordScreen
import kr.co.oadan.ui.screens.CertCardShareScreen
import kr.co.oadan.ui.screens.FastingCompleteScreen
import kr.co.oadan.ui.screens.HomeScreen
import kr.co.oadan.ui.screens.MySettingsScreen
import kr.co.oadan.ui.screens.OnboardingScreen
import kr.co.oadan.ui.screens.PlanSelectScreen
import kr.co.oadan.ui.screens.RecipeDetailScreen
import kr.co.oadan.ui.screens.RecordScreen
import kr.co.oadan.ui.screens.ReportScreen
import kr.co.oadan.ui.theme.OadanTheme

enum class AppSubScreen {
    NONE,
    PLAN_SELECT,
    FASTING_COMPLETE,
    RECIPE_DETAIL,
    BREAK_FAST_RECORD,
    CERT_CARD_SHARE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OadanTheme {
                OadanApp()
            }
        }
    }
}

@Composable
fun OadanApp(viewModel: OadanViewModel = viewModel()) {
    val preferences by viewModel.preferences.collectAsState()
    val activeSession by viewModel.activeSession.collectAsState()
    val todayWaterMl by viewModel.todayWaterMl.collectAsState()
    val recipes by viewModel.recipes.collectAsState()
    val recentSessions by viewModel.recentSessions.collectAsState()
    val allSessions by viewModel.allSessions.collectAsState()
    val conditionLogs by viewModel.conditionLogs.collectAsState()
    val allWaterLogs by viewModel.allWaterLogs.collectAsState()
    val streakDays by viewModel.streakDays.collectAsState()
    val latestWeight by viewModel.latestWeight.collectAsState()

    var selectedTab by remember { mutableStateOf(OadanTab.HOME) }
    var subScreen by remember { mutableStateOf(AppSubScreen.NONE) }
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }
    var completedFastedHours by remember { mutableStateOf(16f) }
    var recordedOrganicGrade by remember { mutableStateOf("오아시스") }

    val scope = rememberCoroutineScope()

    // 1. 최초 온보딩 플로우 (NFR-05, NFR-06)
    if (!preferences.isOnboardingCompleted) {
        OnboardingScreen(
            onFinishOnboarding = {
                viewModel.completeOnboarding()
                subScreen = AppSubScreen.PLAN_SELECT
            },
            onImportBackup = {
                viewModel.completeOnboarding()
                selectedTab = OadanTab.MY
            }
        )
        return
    }

    // 2. 전체 서브 화면 오버레이 라우팅
    when (subScreen) {
        AppSubScreen.PLAN_SELECT -> {
            PlanSelectScreen(
                currentProtocol = preferences.defaultProtocol,
                onPlanSelected = { protocol, targetHours, useSchedule ->
                    viewModel.startFasting(protocol, targetHours)
                    subScreen = AppSubScreen.NONE
                    selectedTab = OadanTab.HOME
                },
                onBack = { subScreen = AppSubScreen.NONE }
            )
            return
        }

        AppSubScreen.FASTING_COMPLETE -> {
            FastingCompleteScreen(
                fastedHours = completedFastedHours,
                streakDays = streakDays,
                waterAmountMl = todayWaterMl,
                onNavigateToBreakFast = {
                    subScreen = AppSubScreen.NONE
                    selectedTab = OadanTab.BREAK_FAST
                },
                onDismissToHome = {
                    subScreen = AppSubScreen.NONE
                    selectedTab = OadanTab.HOME
                }
            )
            return
        }

        AppSubScreen.RECIPE_DETAIL -> {
            selectedRecipe?.let { recipe ->
                RecipeDetailScreen(
                    recipe = recipe,
                    onBack = { subScreen = AppSubScreen.NONE },
                    onToggleBookmark = { viewModel.toggleRecipeBookmark(it) },
                    onRecordWithThisMenu = {
                        subScreen = AppSubScreen.BREAK_FAST_RECORD
                    }
                )
            }
            return
        }

        AppSubScreen.BREAK_FAST_RECORD -> {
            BreakFastRecordScreen(
                initialMenuName = selectedRecipe?.title ?: "두부 채소 비빔 볼",
                lastFastedHours = completedFastedHours,
                onBack = { subScreen = AppSubScreen.NONE },
                onSaveAndCreateCertCard = { menu, veg, noProc, slow, noSweet, seasonal, notes ->
                    viewModel.saveMealRecord(
                        menuName = menu,
                        checkVegFirst = veg,
                        checkNoProcessed = noProc,
                        checkSlow20m = slow,
                        checkNoSweetDrink = noSweet,
                        checkSeasonal = seasonal,
                        notes = notes
                    ) { (_, eval) ->
                        recordedOrganicGrade = eval.grade.title
                        subScreen = AppSubScreen.CERT_CARD_SHARE
                    }
                }
            )
            return
        }

        AppSubScreen.CERT_CARD_SHARE -> {
            CertCardShareScreen(
                fastedHours = completedFastedHours,
                organicGrade = recordedOrganicGrade,
                streakDays = streakDays,
                onBack = { subScreen = AppSubScreen.NONE }
            )
            return
        }

        AppSubScreen.NONE -> {
            // 메인 탭 화면 Scaffold
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                bottomBar = {
                    OadanBottomNavigation(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it },
                        showBreakFastBadge = activeSession == null && completedFastedHours > 0
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (selectedTab) {
                        OadanTab.HOME -> {
                            HomeScreen(
                                activeSession = activeSession,
                                currentWaterMl = todayWaterMl,
                                dailyWaterGoalMl = preferences.dailyWaterGoalMl,
                                onStartFasting = {
                                    subScreen = AppSubScreen.PLAN_SELECT
                                },
                                onEndFasting = { isPartial ->
                                    val elapsedHours = activeSession?.let {
                                        (System.currentTimeMillis() - it.startTimeUtc) / (3600f * 1000f)
                                    } ?: 16f
                                    completedFastedHours = elapsedHours
                                    viewModel.endFasting(isPartial = isPartial)
                                },
                                onUpdateSessionTimes = { sessionId, newStart, newEnd ->
                                    viewModel.updateSessionTimes(sessionId, newStart, newEnd)
                                },
                                onUpdateTargetHours = { newHours ->
                                    viewModel.updateTargetHours(newHours)
                                },
                                onAddWater = { amount ->
                                    viewModel.addWater(amount)
                                },
                                onNavigateToComplete = { hours ->
                                    completedFastedHours = hours
                                    subScreen = AppSubScreen.FASTING_COMPLETE
                                },
                                onNavigateToPlanSelect = {
                                    subScreen = AppSubScreen.PLAN_SELECT
                                }
                            )
                        }

                        OadanTab.BREAK_FAST -> {
                            BreakFastHomeScreen(
                                recipes = recipes,
                                recommendedStage = when {
                                    completedFastedHours < 14f -> "일반 보식"
                                    completedFastedHours < 18f -> "부드러운 보식"
                                    completedFastedHours < 24f -> "순한 보식"
                                    else -> "회복 보식"
                                },
                                lastFastedHours = completedFastedHours,
                                onSelectRecipe = { recipe ->
                                    selectedRecipe = recipe
                                    subScreen = AppSubScreen.RECIPE_DETAIL
                                },
                                onToggleBookmark = { id ->
                                    viewModel.toggleRecipeBookmark(id)
                                },
                                onNavigateToRecord = {
                                    subScreen = AppSubScreen.BREAK_FAST_RECORD
                                }
                            )
                        }

                        OadanTab.RECORD -> {
                            RecordScreen(
                                lastWeightKg = latestWeight,
                                recentLogs = conditionLogs,
                                onSaveRecord = { w, m, f, tag, memo ->
                                    viewModel.saveConditionRecord(w, m, f, tag, memo)
                                },
                                onStopFastingFromSafety = {
                                    viewModel.endFasting(isPartial = true)
                                    selectedTab = OadanTab.HOME
                                }
                            )
                        }

                        OadanTab.REPORT -> {
                            ReportScreen(
                                sessions = allSessions,
                                conditionLogs = conditionLogs,
                                waterLogs = allWaterLogs,
                                streakDays = streakDays,
                                saverCount = preferences.streakSaverCount
                            )
                        }

                        OadanTab.MY -> {
                            MySettingsScreen(
                                preferences = preferences,
                                onUpdateNotifications = { s, e, stg ->
                                    viewModel.updateNotifications(s, e, stg)
                                },
                                onNavigateToPlanSelect = {
                                    subScreen = AppSubScreen.PLAN_SELECT
                                },
                                onUpdateWaterGoal = { newGoal ->
                                    viewModel.setDailyWaterGoal(newGoal)
                                },
                                onUpdateSchedule = { useSchedule ->
                                    viewModel.setUseDayOfWeekSchedule(useSchedule)
                                },
                                onExportJson = {
                                    viewModel.exportJson()
                                },
                                onImportJson = { json ->
                                    viewModel.importJson(json)
                                },
                                onClearAllData = {
                                    viewModel.clearAllData()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
