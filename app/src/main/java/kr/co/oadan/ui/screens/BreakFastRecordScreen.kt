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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import kr.co.oadan.domain.OrganicEvaluation
import kr.co.oadan.domain.OrganicScoreCalculator
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.Dune
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Pebble
import kr.co.oadan.ui.theme.Sand
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.Teal50
import kr.co.oadan.ui.theme.Teal800

@Composable
fun BreakFastRecordScreen(
    initialMenuName: String = "두부 채소 비빔 볼",
    lastFastedHours: Float = 16.2f,
    onBack: () -> Unit,
    onSaveAndCreateCertCard: (
        menuName: String,
        checkVegFirst: Boolean,
        checkNoProcessed: Boolean,
        checkSlow20m: Boolean,
        checkNoSweetDrink: Boolean,
        checkSeasonal: Boolean,
        notes: String
    ) -> Unit
) {
    var menuName by remember { mutableStateOf(initialMenuName) }
    var notes by remember { mutableStateOf("") }

    var checkVegFirst by remember { mutableStateOf(true) }
    var checkNoProcessed by remember { mutableStateOf(true) }
    var checkSlow20m by remember { mutableStateOf(true) }
    var checkNoSweetDrink by remember { mutableStateOf(false) }
    var checkSeasonal by remember { mutableStateOf(true) }

    val evaluation: OrganicEvaluation by remember(
        checkVegFirst, checkNoProcessed, checkSlow20m, checkNoSweetDrink, checkSeasonal
    ) {
        derivedStateOf {
            OrganicScoreCalculator.calculate(
                checkVegFirst = checkVegFirst,
                checkNoProcessed = checkNoProcessed,
                checkSlow20m = checkSlow20m,
                checkNoSweetDrink = checkNoSweetDrink,
                checkSeasonal = checkSeasonal
            )
        }
    }

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "닫기")
                }
                Text(
                    text = "보식 기록",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepInk
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 식단 사진 / 플레이스홀더 영역 (SC-22)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFB9C7A4)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🥗", fontSize = 72.sp)

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x991E2A28))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "사진 등록", fontSize = 12.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 메뉴명 입력
            OutlinedTextField(
                value = menuName,
                onValueChange = { menuName = it },
                label = { Text("첫 끼 메뉴 이름") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${lastFastedHours.toInt()}시간 공복 후 소중한 첫 끼",
                fontSize = 12.sp,
                color = Stone
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 5대 오가닉 체크리스트 (OA-02)
            Text(
                text = "오가닉 식사 습관 체크",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DeepInk
            )
            Spacer(modifier = Modifier.height(8.dp))

            CheckItem(
                text = "채소 먼저 먹었어요 (식이섬유 우선)",
                checked = checkVegFirst,
                onCheckedChange = { checkVegFirst = it }
            )
            CheckItem(
                text = "가공식품 없는 깨끗한 자연식이에요",
                checked = checkNoProcessed,
                onCheckedChange = { checkNoProcessed = it }
            )
            CheckItem(
                text = "20분 이상 천천히 꼭꼭 씹었어요",
                checked = checkSlow20m,
                onCheckedChange = { checkSlow20m = it }
            )
            CheckItem(
                text = "단 음료나 정제 당분을 피했어요",
                checked = checkNoSweetDrink,
                onCheckedChange = { checkNoSweetDrink = it }
            )
            CheckItem(
                text = "제철 자연 식재료를 썼어요",
                checked = checkSeasonal,
                onCheckedChange = { checkSeasonal = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 오가닉 스코어 배지 & 긍정 피드백 카드 (SC-22)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Teal50)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = evaluation.grade.emoji, fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${evaluation.grade.title} 등급",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Teal800
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${evaluation.score}점",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OasisTeal
                            )
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = evaluation.feedbackMessage,
                            fontSize = 13.sp,
                            color = Teal800,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // 하단 저장 CTA
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Sand)
                .padding(20.dp)
        ) {
            Button(
                onClick = {
                    onSaveAndCreateCertCard(
                        menuName,
                        checkVegFirst,
                        checkNoProcessed,
                        checkSlow20m,
                        checkNoSweetDrink,
                        checkSeasonal,
                        notes
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                shape = RoundedCornerShape(999.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "저장하고 인증 카드 만들기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CheckItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (checked) OasisTeal else Color.White)
                .border(2.dp, if (checked) OasisTeal else Pebble, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (checked) FontWeight.Medium else FontWeight.Normal,
            color = DeepInk
        )
    }
}
