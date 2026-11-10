package kr.co.oadan.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.Dune
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Pebble
import kr.co.oadan.ui.theme.Sand
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.SunsetCoral
import kr.co.oadan.ui.theme.Teal50
import kr.co.oadan.ui.theme.Teal700

data class FastingPlan(
    val name: String,
    val targetHours: Float,
    val subtitle: String,
    val difficultyDots: Int,
    val recommendedBreakFastStage: String,
    val breakFastTip: String
)

val PRESET_PLANS = listOf(
    FastingPlan(
        name = "14:10",
        targetHours = 14f,
        subtitle = "처음이라면",
        difficultyDots = 1,
        recommendedBreakFastStage = "일반 보식",
        breakFastTip = "자연식 위주의 균형 잡힌 한 끼를 천천히 드세요"
    ),
    FastingPlan(
        name = "16:8",
        targetHours = 16f,
        subtitle = "가장 많이 선택",
        difficultyDots = 2,
        recommendedBreakFastStage = "부드러운 보식",
        breakFastTip = "채소 → 단백질 → 탄수화물 순서로 천천히 드세요"
    ),
    FastingPlan(
        name = "18:6",
        targetHours = 18f,
        subtitle = "조금 더 깊게",
        difficultyDots = 3,
        recommendedBreakFastStage = "순한 보식",
        breakFastTip = "소화가 잘되는 따뜻한 음식이나 채소죽을 드세요"
    ),
    FastingPlan(
        name = "20:4",
        targetHours = 20f,
        subtitle = "숙련자",
        difficultyDots = 4,
        recommendedBreakFastStage = "순한 보식",
        breakFastTip = "맑은 국물과 연두부로 속을 먼저 달래주세요"
    )
)

@Composable
fun PlanSelectScreen(
    currentProtocol: String,
    onPlanSelected: (protocol: String, targetHours: Float, useSchedule: Boolean) -> Unit,
    onBack: (() -> Unit)? = null
) {
    var selectedPlan by remember {
        mutableStateOf(PRESET_PLANS.find { it.name == currentProtocol } ?: PRESET_PLANS[1])
    }
    var useSchedule by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Sand)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 100.dp)
        ) {
            // App Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "플랜 선택", fontSize = 14.sp, color = Stone)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "나에게 맞는\n공복 리듬을 골라요",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DeepInk,
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "언제든 변경할 수 있어요", fontSize = 14.sp, color = Stone)

            Spacer(modifier = Modifier.height(20.dp))

            // 2x2 프리셋 그리드
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PlanCard(
                        plan = PRESET_PLANS[0],
                        isSelected = selectedPlan == PRESET_PLANS[0],
                        onClick = { selectedPlan = PRESET_PLANS[0] },
                        modifier = Modifier.weight(1f)
                    )
                    PlanCard(
                        plan = PRESET_PLANS[1],
                        isSelected = selectedPlan == PRESET_PLANS[1],
                        onClick = { selectedPlan = PRESET_PLANS[1] },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PlanCard(
                        plan = PRESET_PLANS[2],
                        isSelected = selectedPlan == PRESET_PLANS[2],
                        onClick = { selectedPlan = PRESET_PLANS[2] },
                        modifier = Modifier.weight(1f)
                    )
                    PlanCard(
                        plan = PRESET_PLANS[3],
                        isSelected = selectedPlan == PRESET_PLANS[3],
                        onClick = { selectedPlan = PRESET_PLANS[3] },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 추가 옵션 칩 (OMAD, 5:2)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChipOption(text = "OMAD (23:1)", isSelected = selectedPlan.name == "OMAD") {
                    selectedPlan = FastingPlan("OMAD", 23f, "1일 1식", 4, "순한 보식", "맑은 채수로 시작하세요")
                }
                ChipOption(text = "5:2 플랜", isSelected = selectedPlan.name == "5:2") {
                    selectedPlan = FastingPlan("5:2", 24f, "주 2회 간헐적 단식", 4, "회복 보식", "2단계 회복식을 준수하세요")
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 추천 보식 안내 카드 (FR-01 × OA-01)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF3F6EE))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(text = "🥣", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "${selectedPlan.name} 추천 보식 · ${selectedPlan.recommendedBreakFastStage}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF55703F)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = selectedPlan.breakFastTip,
                            fontSize = 13.sp,
                            color = Stone,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 요일별 스케줄 토글
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "요일별로 다르게 설정", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "주말엔 14:10처럼 가볍게 쉬어가요", fontSize = 12.sp, color = Stone)
                }
                Switch(
                    checked = useSchedule,
                    onCheckedChange = { useSchedule = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = OasisTeal)
                )
            }
        }

        // 하단 고정 시작 버튼
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Sand)
                .padding(20.dp)
        ) {
            Button(
                onClick = {
                    onPlanSelected(selectedPlan.name, selectedPlan.targetHours, useSchedule)
                },
                colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                shape = RoundedCornerShape(999.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "${selectedPlan.name}로 시작하기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PlanCard(
    plan: FastingPlan,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0xFFF6FBFA) else Color.White)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) OasisTeal else Pebble,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = plan.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Teal700 else DeepInk,
                    modifier = Modifier.weight(1f)
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "선택됨",
                        tint = OasisTeal,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = plan.subtitle,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (plan.name == "16:8") SunsetCoral else Stone
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                for (i in 1..4) {
                    Box(
                        modifier = Modifier
                            .size(14.dp, 4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (i <= plan.difficultyDots) OasisTeal else Dune)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChipOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Teal50 else Color.White)
            .border(1.dp, if (isSelected) OasisTeal else Pebble, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Teal700 else DeepInk
        )
    }
}
