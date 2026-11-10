package kr.co.oadan.domain

import androidx.compose.ui.graphics.Color
import kr.co.oadan.ui.theme.StageDesert
import kr.co.oadan.ui.theme.StageLagoon
import kr.co.oadan.ui.theme.StagePalm
import kr.co.oadan.ui.theme.StageSpring
import kr.co.oadan.ui.theme.StageWind

enum class MetabolicStage(
    val step: Int,
    val stageName: String,
    val subtitle: String,
    val emoji: String,
    val color: Color,
    val startHours: Float,
    val endHours: Float,
    val description: String,
    val scientificBasis: String
) {
    STAGE_1(
        step = 1,
        stageName = "모래",
        subtitle = "소화 · 혈당 상승",
        emoji = "🏜️",
        color = StageDesert,
        startHours = 0f,
        endHours = 4f,
        description = "섭취한 음식을 소화하며 혈당과 인슐린 수치가 올라가는 시기예요.",
        scientificBasis = "식후 약 4시간 동안 섭취된 탄수화물이 포도당으로 전환되고, 인슐린이 분비되어 남은 포도당이 간과 근육에 글리코겐으로 저장됩니다."
    ),
    STAGE_2(
        step = 2,
        stageName = "바람",
        subtitle = "혈당 안정 · 인슐린 하락",
        emoji = "🌬️",
        color = StageWind,
        startHours = 4f,
        endHours = 8f,
        description = "인슐린 분비가 줄어들고 간에 저장된 글리코겐을 꺼내 쓰기 시작해요.",
        scientificBasis = "소화가 완료되고 혈당이 정상 범위로 안정화되며, 췌장의 인슐린 분비가 억제되어 지방 분해를 억제하던 브레이크가 서서히 풀립니다."
    ),
    STAGE_3(
        step = 3,
        stageName = "샘물",
        subtitle = "지방 연소 시작",
        emoji = "💧",
        color = StageSpring,
        startHours = 8f,
        endHours = 12f,
        description = "간 글리코겐이 점차 소진되며 체지방을 본격적으로 태우기 시작해요.",
        scientificBasis = "간의 저장 글리코겐이 고갈에 가까워짐에 따라 인체는 체지방을 분해하여 유리지방산을 방출하고, 이를 주요 에너지원으로 사용하기 시작합니다."
    ),
    STAGE_4(
        step = 4,
        stageName = "야자수",
        subtitle = "케토시스(Ketosis)",
        emoji = "🌴",
        color = StagePalm,
        startHours = 12f,
        endHours = 16f,
        description = "지방 대사 산물인 케톤을 뇌와 몸이 주 연료로 쓰며, 머리가 맑아지고 공복감이 줄어요.",
        scientificBasis = "간에서 지방산이 케톤체(아세토아세테이트, 베타-하이드록시부티레이트)로 합성되어 뇌혈관장벽을 통과해 뇌의 대체 에너지원으로 공급됩니다."
    ),
    STAGE_5(
        step = 5,
        stageName = "오아시스",
        subtitle = "오토파지(Autophagy)",
        emoji = "✨",
        color = StageLagoon,
        startHours = 16f,
        endHours = 72f,
        description = "세포가 손상된 단백질과 노폐물을 스스로 청소하고 세포 재생을 활성화해요.",
        scientificBasis = "세포 내 자가포식(Autophagy) 기전이 활성화되어 미토콘드리아와 세포 내 불량 단백질을 정화·재활용함으로써 세포 재생과 항노화에 기여합니다. (2016 노벨생리의학상)"
    );

    companion object {
        fun fromElapsedHours(hours: Float): MetabolicStage {
            return when {
                hours < 4f -> STAGE_1
                hours < 8f -> STAGE_2
                hours < 12f -> STAGE_3
                hours < 16f -> STAGE_4
                else -> STAGE_5
            }
        }
    }
}
