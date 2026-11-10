package kr.co.oadan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kr.co.oadan.domain.FastingTimerState
import kr.co.oadan.ui.theme.Dune
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.Teal50
import kr.co.oadan.ui.theme.Teal700

@Composable
fun FastingDefenseDialog(
    timerState: FastingTimerState,
    onDismiss: () -> Unit,
    onKeepGoing1Hour: () -> Unit,
    onReduceTarget: () -> Unit,
    onFinishAsPartial: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 중앙 미니 일러스트 아이콘
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(Teal50),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "💧", fontSize = 42.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "샘물이 거의 다 찼어요 💧",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                val nextStageName = timerState.nextStage?.stageName ?: "목표"
                Text(
                    text = "${timerState.formattedRemaining}만 더 쉬어가면\n$nextStageName 단계에 도착해요.",
                    fontSize = 14.sp,
                    color = Stone,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 프로그레스 바
                LinearProgressIndicator(
                    progress = timerState.progressRatio.coerceIn(0f, 1f),
                    color = OasisTeal,
                    trackColor = Dune,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 1. 1시간만 더 쉬어가기 (Primary)
                Button(
                    onClick = {
                        onKeepGoing1Hour()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("1시간만 더 쉬어가기", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 2. 오늘 목표 줄이기 (Tonal)
                Button(
                    onClick = {
                        onReduceTarget()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Teal50,
                        contentColor = Teal700
                    ),
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("오늘 목표 줄이기", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 3. 여기서 마무리하기 (부분 달성)
                TextButton(
                    onClick = {
                        onFinishAsPartial()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "여기서 마무리하기 (부분 달성)",
                        color = Stone,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
