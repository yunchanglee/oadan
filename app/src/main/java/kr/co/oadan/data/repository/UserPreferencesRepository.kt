package kr.co.oadan.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "oadan_preferences")

data class UserPreferences(
    val isOnboardingCompleted: Boolean = false,
    val defaultProtocol: String = "16:8",
    val targetFastingHours: Float = 16f,
    val dailyWaterGoalMl: Int = 2000,
    val useDayOfWeekSchedule: Boolean = false,
    val notificationFastingStart: Boolean = true,
    val notificationFastingEnd: Boolean = true,
    val notificationStageChange: Boolean = true,
    val streakSaverCount: Int = 2,
    val disclaimerAccepted: Boolean = false
)

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
        val DEFAULT_PROTOCOL = stringPreferencesKey("default_protocol")
        val TARGET_FASTING_HOURS = floatPreferencesKey("target_fasting_hours")
        val DAILY_WATER_GOAL_ML = intPreferencesKey("daily_water_goal_ml")
        val USE_DAY_OF_WEEK_SCHEDULE = booleanPreferencesKey("use_day_of_week_schedule")
        val NOTIFY_FASTING_START = booleanPreferencesKey("notify_fasting_start")
        val NOTIFY_FASTING_END = booleanPreferencesKey("notify_fasting_end")
        val NOTIFY_STAGE_CHANGE = booleanPreferencesKey("notify_stage_change")
        val STREAK_SAVER_COUNT = intPreferencesKey("streak_saver_count")
        val DISCLAIMER_ACCEPTED = booleanPreferencesKey("disclaimer_accepted")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            isOnboardingCompleted = prefs[PreferencesKeys.IS_ONBOARDING_COMPLETED] ?: false,
            defaultProtocol = prefs[PreferencesKeys.DEFAULT_PROTOCOL] ?: "16:8",
            targetFastingHours = prefs[PreferencesKeys.TARGET_FASTING_HOURS] ?: 16f,
            dailyWaterGoalMl = prefs[PreferencesKeys.DAILY_WATER_GOAL_ML] ?: 2000,
            useDayOfWeekSchedule = prefs[PreferencesKeys.USE_DAY_OF_WEEK_SCHEDULE] ?: false,
            notificationFastingStart = prefs[PreferencesKeys.NOTIFY_FASTING_START] ?: true,
            notificationFastingEnd = prefs[PreferencesKeys.NOTIFY_FASTING_END] ?: true,
            notificationStageChange = prefs[PreferencesKeys.NOTIFY_STAGE_CHANGE] ?: true,
            streakSaverCount = prefs[PreferencesKeys.STREAK_SAVER_COUNT] ?: 2,
            disclaimerAccepted = prefs[PreferencesKeys.DISCLAIMER_ACCEPTED] ?: false
        )
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.IS_ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setProtocol(protocol: String, targetHours: Float) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.DEFAULT_PROTOCOL] = protocol
            prefs[PreferencesKeys.TARGET_FASTING_HOURS] = targetHours
        }
    }

    suspend fun setUseDayOfWeekSchedule(use: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.USE_DAY_OF_WEEK_SCHEDULE] = use
        }
    }

    suspend fun setDailyWaterGoal(goalMl: Int) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.DAILY_WATER_GOAL_ML] = goalMl
        }
    }

    suspend fun setNotifications(start: Boolean, end: Boolean, stage: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.NOTIFY_FASTING_START] = start
            prefs[PreferencesKeys.NOTIFY_FASTING_END] = end
            prefs[PreferencesKeys.NOTIFY_STAGE_CHANGE] = stage
        }
    }

    suspend fun setDisclaimerAccepted(accepted: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.DISCLAIMER_ACCEPTED] = accepted
        }
    }

    suspend fun consumeStreakSaver(): Boolean {
        var success = false
        context.dataStore.edit { prefs ->
            val current = prefs[PreferencesKeys.STREAK_SAVER_COUNT] ?: 2
            if (current > 0) {
                prefs[PreferencesKeys.STREAK_SAVER_COUNT] = current - 1
                success = true
            }
        }
        return success
    }

    suspend fun resetAll() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
