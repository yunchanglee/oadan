package kr.co.oadan.domain

data class EncouragementMessage(
    val title: String,
    val message: String,
    val emoji: String
)

/**
 * 공복 진행 중 가끔 격려해주거나, 목표 지정 시간을 초과했을 때 응원해주는 메시지 공급자.
 * UX 라이팅 원칙: "곁에서 차분히 응원하는 친구" (해요체, 비난/강요 없는 따뜻한 공감)
 */
object EncouragementMessageProvider {

    private val midFastMessages = listOf(
        EncouragementMessage(
            title = "오늘도 나를 비우는 시간 🌿",
            message = "차분히 나에게 집중하는 시간, 정말 잘하고 계세요.",
            emoji = "🌿"
        ),
        EncouragementMessage(
            title = "물 한잔의 오아시스 💧",
            message = "목이 마를 땐 미온수나 시원한 물 한잔으로 수분을 채워주세요.",
            emoji = "💧"
        ),
        EncouragementMessage(
            title = "대사 스위치가 켜졌어요 ✨",
            message = "몸 안에 저장된 에너지를 쓰며 몸이 점점 가벼워지고 있어요.",
            emoji = "✨"
        ),
        EncouragementMessage(
            title = "배고픔의 파도는 곧 지나가요 🌊",
            message = "공복 호르몬 파도는 보통 15분이면 잔잔해져요. 깊게 숨을 들이마셔 보세요.",
            emoji = "🌊"
        ),
        EncouragementMessage(
            title = "세포가 정화되는 시간 🍃",
            message = "불필요한 것은 비우고, 본연의 생체 밸런스를 되찾고 있어요.",
            emoji = "🍃"
        ),
        EncouragementMessage(
            title = "편안하게 쉬어가세요 🧘",
            message = "조급해할 필요 없어요. 내 몸의 신호에 귀 기울이며 차분히 진행해요.",
            emoji = "🧘"
        )
    )

    private val overachievedMessages = listOf(
        EncouragementMessage(
            title = "🎉 목표 공복 시간 돌파! 대단해요",
            message = "오늘 목표를 멋지게 달성하셨어요! 무리하지 마시고 언제든 편안하게 보식을 시작하세요.",
            emoji = "🏆"
        ),
        EncouragementMessage(
            title = "✨ 놀라운 집중력과 절제력!",
            message = "지정된 목표를 넘어섰어요. 이제 몸을 부드럽게 깨우는 건강한 한 끼를 챙겨보세요.",
            emoji = "🥗"
        ),
        EncouragementMessage(
            title = "🌴 오아시스에 도착한 것을 축하해요",
            message = "충분히 잘 비워냈어요. 첫 식사는 채소나 부드러운 단백질로 천천히 시작해보세요.",
            emoji = "🎉"
        ),
        EncouragementMessage(
            title = "👏 무리하지 않는 것이 가장 중요해요",
            message = "초과 달성 중이시네요! 언제든 배고픔이 느껴지면 기분 좋게 식사를 즐기세요.",
            emoji = "💚"
        )
    )

    /**
     * 공복 진행 중 격려 메시지 (경과 시간 기반 선택)
     */
    fun getMidFastMessage(elapsedHours: Int = 0): EncouragementMessage {
        val index = (elapsedHours.coerceAtLeast(0)) % midFastMessages.size
        return midFastMessages[index]
    }

    /**
     * 목표 초과 달성 시 응원 메시지 (초과 시간 기반 선택)
     */
    fun getOverachievedMessage(overachievedHours: Int = 0): EncouragementMessage {
        val index = (overachievedHours.coerceAtLeast(0)) % overachievedMessages.size
        return overachievedMessages[index]
    }

    /**
     * 무작위 응원 문구 반환
     */
    fun getRandomMidFastCheer(): EncouragementMessage {
        return midFastMessages.random()
    }
}
