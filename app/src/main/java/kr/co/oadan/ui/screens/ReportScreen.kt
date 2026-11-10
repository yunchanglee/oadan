package kr.co.oadan.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.data.model.FastSession
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.Dune
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Pebble
import kr.co.oadan.ui.theme.Sand
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.SuccessGreen
import kr.co.oadan.ui.theme.SunsetCoral
import kr.co.oadan.ui.theme.Teal100
import kr.co.oadan.ui.theme.Teal700
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ReportScreen(
    sessions: List<FastSession>,
    streakDays: Int,
    saverCount: Int = 2
) {
    var isMonthly by remember { mutableStateOf(true) }

    // 평균 공복 시간 계산
    val completedSessions = sessions.filter { it.isCompleted && it.fastedHours != null }
    val avgHours = if (completedSessions.isNotEmpty()) {
        completedSessions.map { it.fastedHours!! }.average().toFloat()
    } else 15.8f

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
                Text(
                    text = "리포트",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepInk
                )

                // 주간 / 월간 토글
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Dune)
                        .padding(3.dp)
                ) {
                    ToggleChip(text = "주간", isSelected = !isMonthly) { isMonthly = false }
                    ToggleChip(text = "월간", isSelected = isMonthly) { isMonthly = true }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2열 요약 카드 (SC-40)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    label = "평균 공복",
                    value = String.format(Locale.getDefault(), "%.1fh", avgHours),
                    subText = "▲ 0.6h 지난달 대비",
                    subColor = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "오아시스 연속",
                    value = "${streakDays}일 🔥",
                    subText = "세이버 ${saverCount}회 남음",
                    subColor = Stone,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 캘린더 카드 (SC-40)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null, tint = Stone)
                    }
                    Text(text = "2026년 11월", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = {}, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Stone)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 요일 헤더
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    listOf("일", "월", "화", "수", "목", "금", "토").forEach { day ->
                        Text(text = day, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Stone)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 캘린더 날짜 그리드 (색각 이상 대응: 채움/옅음/점선)
                val completedDates = sessions.filter { it.isCompleted }.map { it.date }
                val partialDates = sessions.filter { it.isPartiallyCompleted }.map { it.date }

                for (week in 0..1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (dayOfWeek in 1..7) {
                            val dayNum = week * 7 + dayOfWeek
                            val dateStr = String.format("2026-11-%02d", dayNum)
                            val isCompleted = completedDates.contains(dateStr) || dayNum in listOf(1, 2, 4, 5, 6, 7, 9, 10)
                            val isPartial = partialDates.contains(dateStr) || dayNum == 3
                            val isRest = dayNum == 8
                            val isToday = dayNum == 10

                            CalendarDayItem(
                                dayNumber = dayNum,
                                isCompleted = isCompleted,
                                isPartial = isPartial,
                                isRest = isRest,
                                isToday = isToday
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 범례
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    LegendItem(color = OasisTeal, label = "달성")
                    Spacer(modifier = Modifier.width(16.dp))
                    LegendItem(color = Teal100, label = "부분 달성")
                    Spacer(modifier = Modifier.width(16.dp))
                    LegendItem(color = Color.Transparent, label = "휴식", isDashed = true)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 공복 시간 & 체중 이중축 그래프 (FR-06)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "공복 시간 · 체중 추이", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = "최근 7일", fontSize = 12.sp, color = Stone)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    val w = size.width
                    val h = size.height

                    // 1. 공복 시간 막대 (7개)
                    val barWidth = 22.dp.toPx()
                    val barSpacing = (w - barWidth * 7) / 8f
                    val sampleFastedHours = listOf(16f, 17f, 14f, 18f, 16.5f, 19f, 16.2f)

                    sampleFastedHours.forEachIndexed { i, hours ->
                        val barHeight = (hours / 24f) * (h * 0.75f)
                        val x = barSpacing + i * (barWidth + barSpacing)
                        val y = h - barHeight
                        val barColor = if (i == 6) OasisTeal else if (hours < 16f) Teal100 else Color(0xFF9DD5CA)

                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(6.dp.toPx())
                        )
                    }

                    // 2. 체중 꺾은선 (Sunset Coral)
                    val weightPoints = listOf(
                        Offset(barSpacing + barWidth / 2f, h * 0.35f),
                        Offset(barSpacing * 2f + barWidth * 1.5f, h * 0.38f),
                        Offset(barSpacing * 3f + barWidth * 2.5f, h * 0.30f),
                        Offset(barSpacing * 4f + barWidth * 3.5f, h * 0.42f),
                        Offset(barSpacing * 5f + barWidth * 4.5f, h * 0.46f),
                        Offset(barSpacing * 6f + barWidth * 5.5f, h * 0.50f),
                        Offset(barSpacing * 7f + barWidth * 6.5f, h * 0.52f)
                    )

                    val linePath = Path().apply {
                        moveTo(weightPoints[0].x, weightPoints[0].y)
                        for (idx in 1 until weightPoints.size) {
                            lineTo(weightPoints[idx].x, weightPoints[idx].y)
                        }
                    }

                    drawPath(
                        path = linePath,
                        color = SunsetCoral,
                        style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // 최근 지점 강조 점
                    drawCircle(
                        color = Color.White,
                        radius = 5.dp.toPx(),
                        center = weightPoints.last()
                    )
                    drawCircle(
                        color = SunsetCoral,
                        radius = 3.5.dp.toPx(),
                        center = weightPoints.last()
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    subText: String,
    subColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(14.dp)
    ) {
        Column {
            Text(text = label, fontSize = 12.sp, color = Stone)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DeepInk)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subText, fontSize = 11.sp, color = subColor, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ToggleChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) DeepInk else Stone
        )
    }
}

@Composable
private fun CalendarDayItem(
    dayNumber: Int,
    isCompleted: Boolean,
    isPartial: Boolean,
    isRest: Boolean,
    isToday: Boolean
) {
    val bgColor = when {
        isCompleted -> OasisTeal
        isPartial -> Teal100
        else -> Color.Transparent
    }
    val textColor = when {
        isCompleted -> Color.White
        isPartial -> Teal700
        isRest -> Stone
        else -> Stone
    }

    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(bgColor)
            .then(
                if (isRest) Modifier.border(1.5.dp, Pebble, CircleShape)
                else if (isToday) Modifier.border(2.dp, SunsetCoral, CircleShape)
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dayNumber.toString(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun LegendItem(color: Color, label: String, isDashed: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
                .then(if (isDashed) Modifier.border(1.5.dp, Pebble, CircleShape) else Modifier)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, fontSize = 11.sp, color = Stone)
    }
}
