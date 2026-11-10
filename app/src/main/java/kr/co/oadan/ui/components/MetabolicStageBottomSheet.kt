package kr.co.oadan.ui.components

import androidx.activity.compose.BackHandler

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import kr.co.oadan.domain.FastTimerEngine
import kr.co.oadan.domain.MetabolicStage
import kr.co.oadan.ui.theme.InfoBlue
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.Teal50
import kr.co.oadan.ui.theme.Teal800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetabolicStageBottomSheet(
    currentStage: MetabolicStage,
    elapsedMillis: Long,
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    BackHandler {
        onDismissRequest()
    }

    var expandedBasisStage by remember { mutableStateOf<MetabolicStage?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "공복 여정",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${FastTimerEngine.formatFullHoursMinutes(elapsedMillis)} 경과",
                    fontSize = 13.sp,
                    color = Stone
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5단계 타임라인 항목 렌더링
            MetabolicStage.values().forEach { stage ->
                val isCurrent = stage == currentStage
                val isCompleted = stage.step < currentStage.step

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // 왼쪽 이모지 뱃지
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCurrent) stage.color else if (isCompleted) Color(0xFFF1EADF) else Color(0xFFFAF6EF)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stage.emoji, fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // 오른쪽 본문
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .then(
                                if (isCurrent) {
                                    Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Teal50)
                                        .padding(12.dp)
                                } else Modifier.padding(top = 2.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${stage.stageName} · ${stage.subtitle}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrent) Teal800 else Color(0xFF1E2A28),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = if (stage.step == 5) "16h~" else "${stage.startHours.toInt()}–${stage.endHours.toInt()}h" + if (isCompleted) " ✓" else "",
                                fontSize = 12.sp,
                                color = Stone,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        if (isCurrent) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stage.description,
                                fontSize = 13.sp,
                                color = Stone,
                                lineHeight = 19.sp
                            )

                            TextButton(
                                onClick = {
                                    expandedBasisStage = if (expandedBasisStage == stage) null else stage
                                },
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = InfoBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (expandedBasisStage == stage) "근거 닫기" else "과학적 근거 보기",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = InfoBlue
                                )
                            }

                            if (expandedBasisStage == stage) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White)
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = stage.scientificBasis,
                                        fontSize = 12.sp,
                                        color = Stone,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "※ 일반적인 연구 기준이며 개인차가 있어요. 의료적 진단이나 치료가 아닙니다.",
                fontSize = 11.sp,
                color = Stone,
                lineHeight = 16.sp
            )
        }
    }
}
