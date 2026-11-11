package kr.co.oadan.data.model

enum class NotificationType {
    WELCOME,            // 환영 및 기본 가이드
    FASTING_START,      // 공복 시작
    STAGE_CHANGE,       // 대사 단계 진입
    GOAL_REACHED,       // 목표 달성 완료
    CHEER,              // 공복 응원 메시지
    WATER,              // 수분 섭취
    SYSTEM              // 시스템 및 일반 안내
}

data class NotificationItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val type: NotificationType,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val routeTab: String? = null // e.g. "HOME", "BREAK_FAST", "RECORD"
)
