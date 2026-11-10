package kr.co.oadan.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Pebble
import kr.co.oadan.ui.theme.Sand
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.Teal50

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    onImportBackup: () -> Unit
) {
    var step by remember { mutableStateOf(1) } // 1: 가치 소개, 2: 면책 고지 동의
    var agreeDisclaimer by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Sand)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
    ) {
        if (step == 1) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 상단 브랜드 로고
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(Teal50),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌴", fontSize = 54.sp)
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "OADAN",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = OasisTeal,
                    letterSpacing = 3.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "비우고, 채우는\n나만의 오아시스",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp,
                    color = DeepInk
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "공복은 참는 시간이 아니라 쉬어가는 시간.\n단식 후 첫 끼(보식)까지 오아단이 함께할게요.",
                    fontSize = 15.sp,
                    color = Stone,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(36.dp))

                // 인디케이터 점
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(24.dp, 6.dp).clip(RoundedCornerShape(3.dp)).background(OasisTeal))
                    Box(modifier = Modifier.size(6.dp).clip(RoundedCornerShape(3.dp)).background(Pebble))
                }

                Spacer(modifier = Modifier.height(36.dp))

                Button(
                    onClick = { step = 2 },
                    colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("시작하기", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onImportBackup) {
                    Text("기존 기록 불러오기", color = OasisTeal, fontSize = 14.sp)
                }
            }
        } else {
            // Step 2: 면책 고지 및 필수 안내 동의 (NFR-05, NFR-06)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "건강하고 안전한 공복을 위한\n면책 고지 안내",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepInk,
                    lineHeight = 30.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(18.dp)
                ) {
                    Column {
                        Text(
                            text = "1. 오아단은 의료기기 및 진단용 앱이 아닙니다.\n제공되는 대사 단계, 타이머 및 보식 추천 정보는 건강 증진을 돕기 위한 일반적 웰니스 가이드입니다.",
                            fontSize = 13.sp,
                            color = DeepInk,
                            lineHeight = 19.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "2. 다음 대상자는 단식을 피하고 전문의와 상담하세요:\n• 임산부, 수유부 및 성장기 청소년\n• 당뇨병 환자 및 저혈당 위험군\n• 섭식장애 병력자 및 저체중자",
                            fontSize = 13.sp,
                            color = DeepInk,
                            lineHeight = 19.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "3. 공복 중 심한 어지러움이나 두통이 느껴지면 즉시 단식을 중단하고 당분을 섭취하세요.",
                            fontSize = 13.sp,
                            color = DeepInk,
                            lineHeight = 19.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = agreeDisclaimer,
                        onCheckedChange = { agreeDisclaimer = it },
                        colors = CheckboxDefaults.colors(checkedColor = OasisTeal)
                    )
                    Text(
                        text = "위 내용을 확인하였으며 동의합니다.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepInk
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onFinishOnboarding,
                    enabled = agreeDisclaimer,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OasisTeal,
                        disabledContainerColor = Pebble
                    ),
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("오아단 시작하기", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
