package kr.co.oadan.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.co.oadan.data.model.NotificationItem
import kr.co.oadan.data.model.NotificationType
import org.json.JSONArray
import org.json.JSONObject

class NotificationRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        val jsonStr = prefs.getString(KEY_NOTIFICATIONS, null)
        val list = mutableListOf<NotificationItem>()

        if (!jsonStr.isNullOrEmpty()) {
            try {
                val jsonArray = JSONArray(jsonStr)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    list.add(
                        NotificationItem(
                            id = obj.getString("id"),
                            type = NotificationType.valueOf(obj.getString("type")),
                            title = obj.getString("title"),
                            message = obj.getString("message"),
                            timestamp = obj.getLong("timestamp"),
                            isRead = obj.optBoolean("isRead", false),
                            routeTab = if (obj.has("routeTab") && !obj.isNull("routeTab")) obj.getString("routeTab") else null
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // 최초 설치 시 기본 안내 알림 2건 제공
        if (list.isEmpty() && !prefs.getBoolean(KEY_INITIAL_SEEDED, false)) {
            val now = System.currentTimeMillis()
            list.add(
                NotificationItem(
                    id = "welcome_1",
                    type = NotificationType.WELCOME,
                    title = "🌿 오아단에 오신 것을 환영해요",
                    message = "공복은 참는 시간이 아니라 나를 쉬게 하는 시간입니다. 오아단과 함께 편안한 쉼을 시작해 보세요.",
                    timestamp = now - 60_000L,
                    isRead = false,
                    routeTab = "HOME"
                )
            )
            list.add(
                NotificationItem(
                    id = "welcome_2",
                    type = NotificationType.SYSTEM,
                    title = "🔔 알림 안내",
                    message = "단식 시작, 대사 단계 변화, 수분 섭취 및 목표 달성 소식을 이곳 알림함에서 확인하실 수 있어요.",
                    timestamp = now,
                    isRead = false,
                    routeTab = null
                )
            )
            prefs.edit().putBoolean(KEY_INITIAL_SEEDED, true).apply()
            saveToPrefs(list)
        }

        _notifications.value = list.sortedByDescending { it.timestamp }
    }

    private fun saveToPrefs(list: List<NotificationItem>) {
        try {
            val jsonArray = JSONArray()
            list.forEach { item ->
                val obj = JSONObject().apply {
                    put("id", item.id)
                    put("type", item.type.name)
                    put("title", item.title)
                    put("message", item.message)
                    put("timestamp", item.timestamp)
                    put("isRead", item.isRead)
                    put("routeTab", item.routeTab ?: JSONObject.NULL)
                }
                jsonArray.put(obj)
            }
            prefs.edit().putString(KEY_NOTIFICATIONS, jsonArray.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addNotification(item: NotificationItem) {
        val current = _notifications.value.toMutableList()
        current.add(0, item)
        // 최대 100개까지만 유지
        val trimmed = if (current.size > 100) current.take(100) else current
        _notifications.value = trimmed
        saveToPrefs(trimmed)
    }

    fun markAsRead(id: String) {
        val current = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
        _notifications.value = current
        saveToPrefs(current)
    }

    fun markAllAsRead() {
        val current = _notifications.value.map { it.copy(isRead = true) }
        _notifications.value = current
        saveToPrefs(current)
    }

    fun deleteNotification(id: String) {
        val current = _notifications.value.filterNot { it.id == id }
        _notifications.value = current
        saveToPrefs(current)
    }

    fun clearAll() {
        _notifications.value = emptyList()
        saveToPrefs(emptyList())
    }

    companion object {
        private const val PREFS_NAME = "oadan_notifications_store"
        private const val KEY_NOTIFICATIONS = "key_notifications_json"
        private const val KEY_INITIAL_SEEDED = "key_initial_seeded"
    }
}
