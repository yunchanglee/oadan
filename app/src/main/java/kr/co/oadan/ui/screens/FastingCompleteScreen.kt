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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RiceBowl
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import kr.co.oadan.domain.MetabolicStage
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Sand
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.SunsetCoral
import kr.co.oadan.ui.theme.Teal50
import kr.co.oadan.ui.theme.Teal700

@Composable
fun FastingCompleteScreen(
    fastedHours: Float,
    streakDays: Int,
    waterAmountMl: Int,
    onNavigateToBreakFast: () -> Unit,
    onDismissToHome: () -> Unit
) {
    val stage = MetabolicStage.fromElapsedHours(fastedHours)
    val hoursInt = fastedHours.toInt()
    val minutesInt = ((fastedHours - hoursInt) * 60).toInt()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Sand)
            .padding(20.dp)
    ) {
        // 상단 닫기 버튼
        IconButton(
            onClick = onDismissToHome,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "닫기")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 상단 축하 오아시스 심볼
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Teal50),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🎉", fontSize = 56.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "목표 달성",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = OasisTeal
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "오늘의 오아시스에 도착했어요",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DeepInk,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 큰 결과 시간
            Text(
                text = "${hoursInt}h ${minutesInt}m",
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                color = Teal700,
                letterSpacing = 0.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3열 요약 카드 (도달 단계 / 오아시스 연속 / 수분)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryCard(
                    emoji = stage.emoji,
                    label = "도달 단계",
                    value = stage.stageName,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    emoji = "🔥",
                    label = "오아시스 연속",
                    value = "${streakDays}일",
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    emoji = "💧",
                    label = "수분",
                    value = String.format("%.1fL", waterAmountMl / 1000f),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Celebration CTA: 오늘의 오아시스 한 끼 보기 (Sunset Coral)
            Button(
                onClick = onNavigateToBreakFast,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SunsetCoral,
                    contentColor = DeepInk
                ),
                shape = RoundedCornerShape(999.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(imageVector = Icons.Default.RiceBowl, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "오늘의 오아시스 한 끼 보기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(onClick = onDismissToHome) {
                Text("나중에 기록할게요", color = Stone, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun SummaryCard(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(vertical = 14.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = emoji, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, fontSize = 11.sp, color = Stone)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepInk)
        }
    }
}
