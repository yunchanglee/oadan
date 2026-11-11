package kr.co.oadan.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kr.co.oadan.data.local.OadanDatabase
import kr.co.oadan.data.model.FastSession
import kr.co.oadan.data.model.NotificationItem
import kr.co.oadan.data.model.NotificationType
import kr.co.oadan.data.model.Recipe
import kr.co.oadan.data.repository.BackupRepository
import kr.co.oadan.data.repository.ConditionRepository
import kr.co.oadan.data.repository.FastingRepository
import kr.co.oadan.data.repository.MealRepository
import kr.co.oadan.data.repository.NotificationRepository
import kr.co.oadan.data.repository.UserPreferences
import kr.co.oadan.data.repository.UserPreferencesRepository
import kr.co.oadan.data.repository.WaterRepository
import kr.co.oadan.domain.OrganicEvaluation
import kr.co.oadan.service.FastingNotificationManager
import kr.co.oadan.widget.FastingAppWidgetProvider

class OadanViewModel(application: Application) : AndroidViewModel(application) {

    private val database = OadanDatabase.getDatabase(application)
    private val fastingRepository = FastingRepository(database.fastSessionDao())
    private val waterRepository = WaterRepository(database.waterLogDao())
    private val conditionRepository = ConditionRepository(database.conditionLogDao())
    private val mealRepository = MealRepository(database.mealLogDao(), database.recipeDao())
    private val preferencesRepository = UserPreferencesRepository(application)
    private val notificationRepository = NotificationRepository(application)
    private val backupRepository = BackupRepository(
        database.fastSessionDao(),
        database.waterLogDao(),
        database.conditionLogDao(),
        database.mealLogDao(),
        preferencesRepository,
        application.cacheDir
    )
    private val notificationManager = FastingNotificationManager(application)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mealRepository.ensureInitialRecipes()
        }
    }

    val activeSession: StateFlow<FastSession?> = fastingRepository.activeSession
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val preferences: StateFlow<UserPreferences> = preferencesRepository.userPreferencesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferences())

    val todayWaterMl: StateFlow<Int> = waterRepository.getTodayTotalWaterMl()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val recipes: StateFlow<List<Recipe>> = mealRepository.getAllRecipes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentSessions: StateFlow<List<FastSession>> = fastingRepository.getRecentSessions(30)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSessions: StateFlow<List<FastSession>> = fastingRepository.getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val conditionLogs: StateFlow<List<kr.co.oadan.data.model.ConditionLog>> = conditionRepository.getAllLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allWaterLogs: StateFlow<List<kr.co.oadan.data.model.WaterLog>> = waterRepository.getAllLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val streakDays: StateFlow<Int> = fastingRepository.calculateStreak()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val latestWeight: StateFlow<Float?> = conditionRepository.latestWeightLog
        .map { it?.weightKg }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val notifications: StateFlow<List<NotificationItem>> = notificationRepository.notifications

    val unreadNotificationCount: StateFlow<Int> = notificationRepository.notifications
        .map { list -> list.count { !it.isRead } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun markNotificationAsRead(id: String) {
        notificationRepository.markAsRead(id)
    }

    fun markAllNotificationsAsRead() {
        notificationRepository.markAllAsRead()
    }

    fun deleteNotification(id: String) {
        notificationRepository.deleteNotification(id)
    }

    fun clearAllNotifications() {
        notificationRepository.clearAll()
    }

    fun addNotification(type: NotificationType, title: String, message: String, routeTab: String? = null) {
        notificationRepository.addNotification(
            NotificationItem(
                type = type,
                title = title,
                message = message,
                routeTab = routeTab
            )
        )
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            preferencesRepository.setOnboardingCompleted(true)
            preferencesRepository.setDisclaimerAccepted(true)
        }
    }

    fun startFasting(protocol: String, targetHours: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            val now = System.currentTimeMillis()
            val targetEndTime = now + (targetHours * 3600f * 1000f).toLong()
            fastingRepository.startFasting(protocol, targetHours)
            preferencesRepository.setProtocol(protocol, targetHours)

            // 알림 목록에 공복 시작 추가
            notificationRepository.addNotification(
                NotificationItem(
                    type = NotificationType.FASTING_START,
                    title = "🌿 $protocol 공복 시작",
                    message = "오늘의 공복이 시작되었어요. 편안한 쉼과 비움의 시간이 되길 응원해요.",
                    routeTab = "HOME"
                )
            )

            // 알람 예약 (완료, 중간 응원, 초과 달성 응원)
            scheduleFastingAlarms(now, targetEndTime, targetHours)

            // 홈 위젯 업데이트
            FastingAppWidgetProvider.updateAllWidgets(getApplication())
        }
    }

    fun endFasting(isPartial: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            fastingRepository.endFasting(isPartial = isPartial)
            notificationManager.dismissOngoingNotification()
            notificationManager.cancelAlarms(listOf(101, 102, 103, 104))

            // 알림 목록에 공복 완료/마무리 추가
            val title = if (isPartial) "🥣 공복 마무리" else "🎉 공복 완료 달성"
            val msg = if (isPartial) {
                "공복을 부분 달성으로 마무리했어요. 부드러운 보식으로 속을 차분히 달래주세요."
            } else {
                "목표 공복을 무사히 완료했어요! 이제 영양 가득한 오가닉 한 끼(보식)를 확인해 보세요."
            }
            notificationRepository.addNotification(
                NotificationItem(
                    type = if (isPartial) NotificationType.SYSTEM else NotificationType.GOAL_REACHED,
                    title = title,
                    message = msg,
                    routeTab = "BREAK_FAST"
                )
            )

            FastingAppWidgetProvider.updateAllWidgets(getApplication())
        }
    }

    fun updateSessionTimes(sessionId: Long, newStart: Long, newTargetEnd: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            fastingRepository.updateSessionTimes(sessionId, newStart, newTargetEnd)
            val durationHours = ((newTargetEnd - newStart) / (3600f * 1000f)).coerceAtLeast(1f)
            scheduleFastingAlarms(newStart, newTargetEnd, durationHours)
            FastingAppWidgetProvider.updateAllWidgets(getApplication())
        }
    }

    fun updateTargetHours(newTargetHours: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            fastingRepository.updateActiveSessionTarget(newTargetHours)
            val active = fastingRepository.getActiveSessionOnce()
            if (active != null) {
                scheduleFastingAlarms(active.startTimeUtc, active.targetEndTimeUtc, newTargetHours)
            }
            FastingAppWidgetProvider.updateAllWidgets(getApplication())
        }
    }

    private fun scheduleFastingAlarms(startTime: Long, targetEndTime: Long, targetHours: Float) {
        val now = System.currentTimeMillis()
        notificationManager.cancelAlarms(listOf(101, 102, 103, 104))

        // 1. 목표 완료 알람
        if (targetEndTime > now) {
            notificationManager.scheduleExactAlarm(
                targetEndTime,
                FastingNotificationManager.ACTION_COMPLETION_ALERT,
                101
            )
        }

        // 2. 공복 진행 중 격려 알람 (중간 지점)
        val midTime = startTime + (targetHours * 0.5f * 3600f * 1000f).toLong()
        if (midTime > now && midTime < targetEndTime) {
            notificationManager.scheduleExactAlarm(
                midTime,
                FastingNotificationManager.ACTION_MID_FAST_CHEER,
                102
            )
        }

        // 3. 목표 초과 달성 1시간 후 응원 알람
        val overachieveTime1 = targetEndTime + 3600000L
        if (overachieveTime1 > now) {
            notificationManager.scheduleExactAlarm(
                overachieveTime1,
                FastingNotificationManager.ACTION_OVERACHIEVE_CHEER,
                103
            )
        }

        // 4. 목표 초과 달성 3시간 후 응원 알람
        val overachieveTime2 = targetEndTime + 3 * 3600000L
        if (overachieveTime2 > now) {
            notificationManager.scheduleExactAlarm(
                overachieveTime2,
                FastingNotificationManager.ACTION_OVERACHIEVE_CHEER,
                104
            )
        }
    }

    fun addWater(amountMl: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            waterRepository.addWater(amountMl)
            notificationRepository.addNotification(
                NotificationItem(
                    type = NotificationType.WATER,
                    title = "💧 수분 +${amountMl}ml 기록",
                    message = "수분을 보충했어요. 공복 중 충분한 수분 섭취는 대사를 원활하게 돕습니다.",
                    routeTab = "HOME"
                )
            )
        }
    }

    fun saveConditionRecord(
        weightKg: Float?,
        muscleKg: Float?,
        fatPercent: Float?,
        conditionTag: String?,
        memo: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            conditionRepository.recordCondition(
                weightKg = weightKg,
                skeletalMuscleKg = muscleKg,
                bodyFatPercent = fatPercent,
                conditionTag = conditionTag,
                memo = memo
            )
        }
    }

    fun saveUserBodyProfile(heightCm: Float, gender: String) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.setUserBodyProfile(heightCm, gender)
        }
    }

    fun saveMealRecord(
        menuName: String,
        checkVegFirst: Boolean,
        checkNoProcessed: Boolean,
        checkSlow20m: Boolean,
        checkNoSweetDrink: Boolean,
        checkSeasonal: Boolean,
        notes: String,
        onSaved: (Pair<Long, OrganicEvaluation>) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val active = fastingRepository.getActiveSessionOnce()
            val result = mealRepository.saveMealLog(
                menuName = menuName,
                fastSessionId = active?.id,
                checkVegFirst = checkVegFirst,
                checkNoProcessed = checkNoProcessed,
                checkSlow20m = checkSlow20m,
                checkNoSweetDrink = checkNoSweetDrink,
                checkSeasonal = checkSeasonal,
                notes = notes
            )
            onSaved(result)
        }
    }

    fun toggleRecipeBookmark(recipeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mealRepository.toggleBookmark(recipeId)
        }
    }

    fun updateNotifications(start: Boolean, end: Boolean, stage: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setNotifications(start, end, stage)
        }
    }

    fun updateCheerNotification(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setCheerNotification(enabled)
        }
    }

    fun setDailyWaterGoal(goalMl: Int) {
        viewModelScope.launch {
            preferencesRepository.setDailyWaterGoal(goalMl)
        }
    }

    fun setUseDayOfWeekSchedule(use: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setUseDayOfWeekSchedule(use)
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            preferencesRepository.setThemeMode(mode)
        }
    }

    suspend fun exportJson(): String {
        return backupRepository.exportDataAsJson()
    }

    suspend fun importJson(json: String): Boolean {
        return backupRepository.importDataFromJson(json)
    }

    fun clearAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            backupRepository.clearAllData()
            notificationManager.dismissOngoingNotification()
            FastingAppWidgetProvider.updateAllWidgets(getApplication())
        }
    }
}
