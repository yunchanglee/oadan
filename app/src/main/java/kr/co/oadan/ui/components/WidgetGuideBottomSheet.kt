package kr.co.oadan.ui.components

import androidx.activity.compose.BackHandler
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import kr.co.oadan.widget.FastingAppWidgetProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetGuideBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    BackHandler {
        onDismissRequest()
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(Pebble)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
        ) {
            // 헤더
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Teal50),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "📱", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "홈 화면 위젯 안내 & 추가",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "앱을 열지 않고도 스마트폰 바탕화면에서 바로 확인하세요",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 1. 2x2 미니 위젯 프리뷰 & 추가 카드
            Text(
                text = "1. 미니 오아시스 위젯 (2×2)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .border(1.dp, Pebble, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    // 가상 위젯 실물 프리뷰 (2x2)
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Sand)
                            .border(1.dp, Dune, RoundedCornerShape(20.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🌴", fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "16h 24m",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepInk
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "목표까지 1h 36m",
                                fontSize = 11.sp,
                                color = OasisTeal,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "• 한눈에 쏙 들어오는 아담한 미니멀 디자인\n• 현재 대사 단계 아이콘과 공복 시간 실시간 표시\n• 위젯 탭 시 오아단 앱으로 즉시 이동",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            val success = FastingAppWidgetProvider.pinWidgetToHomeScreen(context, is4x2 = false)
                            if (success) {
                                Toast.makeText(context, "홈 화면에 2×2 위젯 추가 팝업이 표시됩니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "홈 화면 빈 공간을 길게 눌러 [위젯]에서 오아단을 선택해주세요.", Toast.LENGTH_LONG).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text(text = "홈 화면에 2×2 위젯 바로 추가", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. 4x2 와이드 위젯 프리뷰 & 추가 카드
            Text(
                text = "2. 대사 단계 & 수분 기록 위젯 (4×2)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .border(1.dp, Pebble, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    // 가상 위젯 실물 프리뷰 (4x2)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Sand)
                            .border(1.dp, Dune, RoundedCornerShape(20.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            // 대사 단계 타이틀
                            Text(
                                text = "🌴 야자수 단계 (지방 연소) · 16:8",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = OasisTeal
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            // 경과 시간 대형 표시
                            Text(
                                text = "16h 24m",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepInk
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            // 종료 및 잔여 시간
                            Text(
                                text = "내일 06:00 종료 · 목표까지 1h 36m 남음",
                                fontSize = 11.sp,
                                color = Stone
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // 프로그레스 바
                            LinearProgressIndicator(
                                progress = { 0.75f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = OasisTeal,
                                trackColor = Pebble
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // 하단 버튼 분리 행 (버튼 간격 및 텍스트 왜곡 방지)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Teal50)
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "💧 +250ml",
                                        fontSize = 11.sp,
                                        color = OasisTeal,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SunsetCoral.copy(alpha = 0.15f))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "공복 마치기",
                                        fontSize = 11.sp,
                                        color = SunsetCoral,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "• 5단계 대사 게이지와 정확한 잔여 시간 동시 제공\n• 앱을 켜지 않고 바탕화면에서 즉시 물 250ml 기록 가능\n• 공복 시작 / 마침 원터치 즉시 제어",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            val success = FastingAppWidgetProvider.pinWidgetToHomeScreen(context, is4x2 = true)
                            if (success) {
                                Toast.makeText(context, "홈 화면에 4×2 위젯 추가 팝업이 표시됩니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "홈 화면 빈 공간을 길게 눌러 [위젯]에서 오아단을 선택해주세요.", Toast.LENGTH_LONG).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text(text = "홈 화면에 4×2 위젯 바로 추가", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. 수동 추가 방법 안내 박스
            Text(
                text = "직접 홈 화면에서 위젯 추가하는 방법",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .border(1.dp, Pebble, RoundedCornerShape(12.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StepRow(step = "1", text = "스마트폰 홈 화면의 빈 공간을 1~2초간 길게 누릅니다.")
                StepRow(step = "2", text = "화면 하단 메뉴에서 [위젯] 또는 [Widgets] 아이콘을 누릅니다.")
                StepRow(step = "3", text = "목록에서 '오아단'을 찾아 원하는 위젯(2×2 또는 4×2)을 홈 화면으로 끌어다 놓습니다.")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 배터리 절전 안내
            Text(
                text = "🌿 오아단 위젯은 배터리를 보호하기 위해 초 단위가 아닌 분 단위로 스마트하게 갱신되며, 백그라운드 배터리 소모가 거의 없습니다.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
private fun StepRow(step: String, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(OasisTeal),
            contentAlignment = Alignment.Center
        ) {
            Text(text = step, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 17.sp,
            modifier = Modifier.weight(1f)
        )
    }
}
