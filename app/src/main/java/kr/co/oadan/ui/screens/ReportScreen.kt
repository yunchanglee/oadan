package kr.co.oadan.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.data.model.ConditionLog
import kr.co.oadan.data.model.FastSession
import kr.co.oadan.data.model.WaterLog
import kr.co.oadan.domain.FastTimerEngine
import kr.co.oadan.domain.MetabolicStage
import kr.co.oadan.ui.components.ReportConditionEditDialog
import kr.co.oadan.ui.components.ReportFastEditDialog
import kr.co.oadan.ui.components.ReportWaterEditDialog
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.Dune
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Pebble
import kr.co.oadan.ui.theme.Sand
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.SuccessGreen
import kr.co.oadan.ui.theme.SunsetCoral
import kr.co.oadan.ui.theme.Teal100
import kr.co.oadan.ui.theme.Teal50
import kr.co.oadan.ui.theme.Teal700
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ReportScreen(
    sessions: List<FastSession>,
    conditionLogs: List<ConditionLog> = emptyList(),
    waterLogs: List<WaterLog> = emptyList(),
    streakDays: Int,
    saverCount: Int = 2,
    onSaveFastSession: ((FastSession) -> Unit)? = null,
    onDeleteFastSession: ((FastSession) -> Unit)? = null,
    onSaveCondition: ((date: String, weightKg: Float?, muscleKg: Float?, fatPercent: Float?, conditionTag: String?, memo: String?) -> Unit)? = null,
    onDeleteCondition: ((date: String) -> Unit)? = null,
    onSaveWater: ((date: String, totalMl: Int) -> Unit)? = null
) {
    var isMonthly by remember { mutableStateOf(true) }

    // 다이얼로그 노출 상태
    var showFastEditDialog by remember { mutableStateOf(false) }
    var showConditionEditDialog by remember { mutableStateOf(false) }
    var showWaterEditDialog by remember { mutableStateOf(false) }

    // 캘린더 연/월 상태 (이전달/다음달 이동 기능)
    val calendarInstance = remember {
        Calendar.getInstance().apply {
            // 오늘이 포함된 월로 초기화
        }
    }
    var currentYear by remember { mutableIntStateOf(calendarInstance.get(Calendar.YEAR)) }
    var currentMonth by remember { mutableIntStateOf(calendarInstance.get(Calendar.MONTH)) } // 0~11

    // 오늘 날짜 포맷
    val todaySdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val todayString = remember { todaySdf.format(Date()) }

    // 선택된 날짜 (초기값: 오늘)
    var selectedDateStr by remember { mutableStateOf(todayString) }

    // 평균 공복 시간 계산
    val completedSessions = sessions.filter { it.isCompleted && it.fastedHours != null }
    val avgHours = if (completedSessions.isNotEmpty()) {
        completedSessions.map { it.fastedHours!! }.average().toFloat()
    } else 15.8f

    // 이번 달 및 지난 달 평균 비교 계산 (동적 델타)
    val curMonthPrefix = String.format(Locale.getDefault(), "%04d-%02d", currentYear, currentMonth + 1)
    val prevMonthCal = Calendar.getInstance().apply {
        set(Calendar.YEAR, currentYear)
        set(Calendar.MONTH, currentMonth)
        add(Calendar.MONTH, -1)
    }
    val prevMonthPrefix = String.format(Locale.getDefault(), "%04d-%02d", prevMonthCal.get(Calendar.YEAR), prevMonthCal.get(Calendar.MONTH) + 1)

    val currentMonthSessions = sessions.filter { it.date.startsWith(curMonthPrefix) && it.isCompleted && it.fastedHours != null }
    val prevMonthSessions = sessions.filter { it.date.startsWith(prevMonthPrefix) && it.isCompleted && it.fastedHours != null }

    val curMonthAvg = if (currentMonthSessions.isNotEmpty()) currentMonthSessions.map { it.fastedHours!! }.average().toFloat() else avgHours
    val prevMonthAvg = if (prevMonthSessions.isNotEmpty()) prevMonthSessions.map { it.fastedHours!! }.average().toFloat() else null

    val (deltaText, deltaColor) = when {
        prevMonthAvg != null -> {
            val diff = curMonthAvg - prevMonthAvg
            if (diff >= 0) {
                Pair("▲ +${String.format(Locale.getDefault(), "%.1f", diff)}h 지난달 대비", SuccessGreen)
            } else {
                Pair("▼ ${String.format(Locale.getDefault(), "%.1f", diff)}h 지난달 대비", SunsetCoral)
            }
        }
        completedSessions.isNotEmpty() -> Pair("▲ 0.6h 지난달 대비", SuccessGreen)
        else -> Pair("목표 16h 기준", Stone)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 120.dp)
        ) {
            // App Bar & 주간/월간 탭
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "리포트",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // 주간 / 월간 토글 칩
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
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
                    value = String.format(Locale.getDefault(), "%.1fh", curMonthAvg),
                    subText = deltaText,
                    subColor = deltaColor,
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

            if (isMonthly) {
                // ==================== 1. 월간 뷰 (캘린더) ====================
                MonthlyCalendarSection(
                    year = currentYear,
                    month = currentMonth,
                    todayString = todayString,
                    selectedDateStr = selectedDateStr,
                    sessions = sessions,
                    onPrevMonth = {
                        val cal = Calendar.getInstance().apply {
                            set(Calendar.YEAR, currentYear)
                            set(Calendar.MONTH, currentMonth)
                            add(Calendar.MONTH, -1)
                        }
                        currentYear = cal.get(Calendar.YEAR)
                        currentMonth = cal.get(Calendar.MONTH)
                    },
                    onNextMonth = {
                        val cal = Calendar.getInstance().apply {
                            set(Calendar.YEAR, currentYear)
                            set(Calendar.MONTH, currentMonth)
                            add(Calendar.MONTH, 1)
                        }
                        currentYear = cal.get(Calendar.YEAR)
                        currentMonth = cal.get(Calendar.MONTH)
                    },
                    onSelectDate = { dateStr ->
                        selectedDateStr = dateStr
                    }
                )
            } else {
                // ==================== 2. 주간 뷰 ====================
                WeeklySummarySection(
                    sessions = sessions,
                    conditionLogs = conditionLogs,
                    waterLogs = waterLogs
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==================== 3. 선택된 날짜 상세 카드 ====================
            SelectedDateDetailCard(
                dateStr = selectedDateStr,
                session = sessions.firstOrNull { it.date == selectedDateStr },
                condition = conditionLogs.firstOrNull { it.date == selectedDateStr },
                waterLogs = waterLogs.filter { it.date == selectedDateStr },
                onEditFast = { showFastEditDialog = true },
                onEditCondition = { showConditionEditDialog = true },
                onEditWater = { showWaterEditDialog = true }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ==================== 4. 공복 시간 & 체중 추이 이중축 차트 ====================
            DualAxisChartSection(
                sessions = sessions,
                conditionLogs = conditionLogs
            )
        }

        // ==================== 기록 수정 다이얼로그들 ====================
        if (showFastEditDialog) {
            val selectedSession = sessions.firstOrNull { it.date == selectedDateStr }
            ReportFastEditDialog(
                session = selectedSession,
                targetDateStr = selectedDateStr,
                onDismiss = { showFastEditDialog = false },
                onSave = { updatedSession ->
                    onSaveFastSession?.invoke(updatedSession)
                },
                onDelete = { sessionToDelete ->
                    onDeleteFastSession?.invoke(sessionToDelete)
                }
            )
        }

        if (showConditionEditDialog) {
            val selectedCondition = conditionLogs.firstOrNull { it.date == selectedDateStr }
            ReportConditionEditDialog(
                dateStr = selectedDateStr,
                existingLog = selectedCondition,
                onDismiss = { showConditionEditDialog = false },
                onSave = { w, m, f, c, memo ->
                    onSaveCondition?.invoke(selectedDateStr, w, m, f, c, memo)
                },
                onDelete = {
                    onDeleteCondition?.invoke(selectedDateStr)
                }
            )
        }

        if (showWaterEditDialog) {
            val currentTotalWaterMl = waterLogs.filter { it.date == selectedDateStr }.sumOf { it.amountMl }
            ReportWaterEditDialog(
                dateStr = selectedDateStr,
                currentTotalMl = currentTotalWaterMl,
                onDismiss = { showWaterEditDialog = false },
                onSave = { totalMl ->
                    onSaveWater?.invoke(selectedDateStr, totalMl)
                }
            )
        }
    }
}

/**
 * 월간 캘린더 컴포넌트: 이전/다음달 이동, 실제 일수/요일 동적 렌더링, 날짜 터치
 */
@Composable
private fun MonthlyCalendarSection(
    year: Int,
    month: Int,
    todayString: String,
    selectedDateStr: String,
    sessions: List<FastSession>,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSelectDate: (String) -> Unit
) {
    val cal = remember(year, month) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }

    // 1일의 요일 (1=일, 2=월 ... 7=토) -> 0..6 offset
    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        // 연도 & 월 헤더 및 이전/다음 달 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPrevMonth, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "이전달", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                text = "${year}년 ${month + 1}월",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = onNextMonth, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "다음달", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 요일 헤더 (일 ~ 토)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEachIndexed { idx, day ->
                Text(
                    text = day,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (idx == 0) Color(0xFFC2453D) else Stone
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 월 전체 날짜 그리드
        val totalCells = firstDayOfWeek + daysInMonth
        val numRows = (totalCells + 6) / 7

        for (row in 0 until numRows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (col in 0..6) {
                    val cellIndex = row * 7 + col
                    val dayNum = cellIndex - firstDayOfWeek + 1

                    if (dayNum in 1..daysInMonth) {
                        val dateStr = String.format("%04d-%02d-%02d", year, month + 1, dayNum)
                        val session = sessions.firstOrNull { it.date == dateStr }

                        val isCompleted = session?.isCompleted == true
                        val isPartial = session?.isPartiallyCompleted == true
                        val isToday = dateStr == todayString
                        val isSelected = dateStr == selectedDateStr
                        val isFuture = dateStr > todayString
                        val isRest = !isFuture && session == null && !isToday

                        CalendarDayCell(
                            dayNumber = dayNum,
                            isCompleted = isCompleted,
                            isPartial = isPartial,
                            isRest = isRest,
                            isToday = isToday,
                            isSelected = isSelected,
                            isFuture = isFuture,
                            onClick = { onSelectDate(dateStr) }
                        )
                    } else {
                        // 빈 셀
                        Spacer(modifier = Modifier.size(34.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 범례 (달성 / 부분 달성 / 휴식)
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
}

/**
 * 캘린더 날짜 셀 (선택, 달성 여부, 오늘 표시)
 */
@Composable
private fun CalendarDayCell(
    dayNumber: Int,
    isCompleted: Boolean,
    isPartial: Boolean,
    isRest: Boolean,
    isToday: Boolean,
    isSelected: Boolean,
    isFuture: Boolean,
    onClick: () -> Unit
) {
    val bgColor = when {
        isCompleted -> OasisTeal
        isPartial -> Teal100
        else -> Color.Transparent
    }
    val textColor = when {
        isCompleted -> Color.White
        isPartial -> Teal700
        isFuture -> Color(0xFFB5B9B7)
        isRest -> Stone
        else -> DeepInk
    }

    Box(
        modifier = Modifier
            .size(36.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // 배경 및 기본 테두리
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(bgColor)
                .then(
                    if (isSelected) {
                        Modifier.border(2.5.dp, SunsetCoral, CircleShape)
                    } else if (isToday) {
                        Modifier.border(1.5.dp, SunsetCoral.copy(alpha = 0.6f), CircleShape)
                    } else {
                        Modifier
                    }
                )
        )

        // 휴식(Rest)인 경우 점선 원 그리기
        if (isRest && !isCompleted && !isPartial) {
            Canvas(modifier = Modifier.size(32.dp)) {
                drawCircle(
                    color = Pebble,
                    radius = (size.minDimension - 2.dp.toPx()) / 2f,
                    style = Stroke(
                        width = 1.5.dp.toPx(),
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                    )
                )
            }
        }

        Text(
            text = dayNumber.toString(),
            fontSize = 12.sp,
            fontWeight = if (isToday || isSelected || isCompleted) FontWeight.Bold else FontWeight.Medium,
            color = textColor
        )
    }
}

/**
 * 주간 뷰: 최근 7일 요약 카드
 */
@Composable
private fun WeeklySummarySection(
    sessions: List<FastSession>,
    conditionLogs: List<ConditionLog>,
    waterLogs: List<WaterLog>
) {
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val recent7Days = (0..6).map { offset ->
        val c = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -6 + offset) }
        sdf.format(c.time)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text(
            text = "이번 주 공복 현황",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            recent7Days.forEach { dateStr ->
                val session = sessions.firstOrNull { it.date == dateStr }
                val dayOfWeekStr = try {
                    val d = sdf.parse(dateStr)
                    SimpleDateFormat("E", Locale.KOREA).format(d ?: Date())
                } catch (e: Exception) { "" }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = dayOfWeekStr, fontSize = 11.sp, color = Stone)
                    Spacer(modifier = Modifier.height(6.dp))

                    val (color, icon) = when {
                        session?.isCompleted == true -> Pair(OasisTeal, "✓")
                        session?.isPartiallyCompleted == true -> Pair(Teal100, "△")
                        else -> Pair(Dune, "-")
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = icon,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (session?.isCompleted == true) Color.White else Teal700
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = session?.fastedHours?.let { "${it.toInt()}h" } ?: "-",
                        fontSize = 10.sp,
                        color = Stone
                    )
                }
            }
        }
    }
}

/**
 * 선택된 날짜 상세 카드 (공복시간, 대사단계, 수분량, 체중 등 상세 정보 노출 및 기록 수정 연동)
 */
@Composable
private fun SelectedDateDetailCard(
    dateStr: String,
    session: FastSession?,
    condition: ConditionLog?,
    waterLogs: List<WaterLog>,
    onEditFast: () -> Unit,
    onEditCondition: () -> Unit,
    onEditWater: () -> Unit
) {
    val totalWaterMl = waterLogs.sumOf { it.amountMl }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        // 상단 헤더: 날짜 & 상태 뱃지
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$dateStr 기록 상세",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (session != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (session.isCompleted) Teal50 else Color(0xFFF1EADF))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (session.isCompleted) "목표 달성 ✓" else "부분 달성",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (session.isCompleted) Teal700 else Stone
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 1. 단식 기록 섹션
        if (session != null) {
            val hours = session.fastedHours ?: ((session.actualEndTimeUtc ?: session.targetEndTimeUtc) - session.startTimeUtc) / (3600f * 1000f)
            val stage = MetabolicStage.fromElapsedHours(hours)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DetailMiniCard(
                    title = "공복 시간",
                    value = String.format(Locale.KOREA, "%.1f시간", hours),
                    sub = session.protocol,
                    actionText = "수정 ✏️",
                    onAction = onEditFast,
                    modifier = Modifier.weight(1f)
                )
                DetailMiniCard(
                    title = "도달 대사 단계",
                    value = "${stage.emoji} ${stage.stageName}",
                    sub = stage.subtitle,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            // 단식 기록 없는 날
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "이 날은 단식 없이 편안히 몸을 쉬어간 날이에요 🌿",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(SunsetCoral.copy(alpha = 0.15f))
                        .border(1.dp, SunsetCoral.copy(alpha = 0.5f), RoundedCornerShape(999.dp))
                        .clickable(onClick = onEditFast)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "+ 이 날의 단식 기록 추가하기",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SunsetCoral
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. 수분 및 체중 라이프로그 섹션
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DetailMiniCard(
                title = "수분 섭취",
                value = if (totalWaterMl > 0) String.format(Locale.KOREA, "%.1fL", totalWaterMl / 1000f) else "기록 없음",
                sub = if (totalWaterMl > 0) "${waterLogs.size}회 섭취" else "탭하여 기록",
                actionText = "수정 ✏️",
                onAction = onEditWater,
                modifier = Modifier.weight(1f)
            )
            DetailMiniCard(
                title = "기록된 체중",
                value = condition?.weightKg?.let { "${it}kg" } ?: "미기록",
                sub = condition?.conditionTag ?: "탭하여 기록",
                actionText = "수정 ✏️",
                onAction = onEditCondition,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DetailMiniCard(
    title: String,
    value: String,
    sub: String,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.background)
            .then(
                if (onAction != null) {
                    Modifier.clickable(onClick = onAction)
                } else {
                    Modifier
                }
            )
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (actionText != null && onAction != null) {
                    Text(
                        text = actionText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SunsetCoral
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = sub,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 공복 시간 · 체중 추이 이중축 차트 (최근 7일 실제 데이터 매핑)
 */
@Composable
private fun DualAxisChartSection(
    sessions: List<FastSession>,
    conditionLogs: List<ConditionLog>
) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val recent7Days = (0..6).map { offset ->
        val c = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -6 + offset) }
        sdf.format(c.time)
    }

    // 각 날짜별 공복 시간 및 체중 데이터 추출
    val fastHoursList = recent7Days.map { d ->
        sessions.firstOrNull { it.date == d }?.fastedHours ?: 0f
    }
    val weightList = recent7Days.map { d ->
        conditionLogs.firstOrNull { it.date == d }?.weightKg
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "공복 시간 · 체중 추이", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = "최근 7일", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        ) {
            val w = size.width
            val h = size.height

            val barCount = 7
            val barWidth = 22.dp.toPx()
            val spacing = (w - barWidth * barCount) / (barCount + 1)

            // 1. 공복 시간 막대 그래프
            fastHoursList.forEachIndexed { i, hours ->
                val barHeight = if (hours > 0f) {
                    (hours / 24f).coerceIn(0.1f, 1f) * (h * 0.75f)
                } else {
                    8.dp.toPx() // 휴식일 미니 점
                }

                val x = spacing + i * (barWidth + spacing)
                val y = h - barHeight
                val barColor = when {
                    hours >= 16f -> OasisTeal
                    hours > 0f -> Teal100
                    else -> Dune
                }

                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(6.dp.toPx())
                )
            }

            // 2. 체중 꺾은선 그래프 (기록이 있는 경우 연동)
            val validWeights = weightList.filterNotNull()
            if (validWeights.size >= 2) {
                val minWeight = validWeights.minOrNull() ?: 60f
                val maxWeight = (validWeights.maxOrNull() ?: 70f).coerceAtLeast(minWeight + 2f)

                val points = mutableListOf<Offset>()
                recent7Days.forEachIndexed { i, d ->
                    val weight = conditionLogs.firstOrNull { it.date == d }?.weightKg
                    if (weight != null) {
                        val normalized = (weight - minWeight) / (maxWeight - minWeight)
                        val x = spacing + i * (barWidth + spacing) + barWidth / 2f
                        val y = h * 0.6f - normalized * (h * 0.45f)
                        points.add(Offset(x, y))
                    }
                }

                if (points.size >= 2) {
                    val path = Path().apply {
                        moveTo(points[0].x, points[0].y)
                        for (idx in 1 until points.size) {
                            lineTo(points[idx].x, points[idx].y)
                        }
                    }
                    drawPath(
                        path = path,
                        color = SunsetCoral,
                        style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                    )
                    points.forEach { pt ->
                        drawCircle(color = Color.White, radius = 5.dp.toPx(), center = pt)
                        drawCircle(color = SunsetCoral, radius = 3.5.dp.toPx(), center = pt)
                    }
                }
            } else {
                // 체중 기록이 적을 때 기준선 표시
                val lineY = h * 0.4f
                drawLine(
                    color = SunsetCoral.copy(alpha = 0.5f),
                    start = Offset(spacing, lineY),
                    end = Offset(w - spacing, lineY),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 날짜 라벨 (최근 7일 일자)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            recent7Days.forEach { dateStr ->
                val dayStr = dateStr.substringAfterLast("-")
                Text(text = "${dayStr}일", fontSize = 10.sp, color = Stone)
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
            .background(MaterialTheme.colorScheme.surface)
            .padding(14.dp)
    ) {
        Column {
            Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
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
            .background(if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LegendItem(color: Color, label: String, isDashed: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isDashed) {
            Canvas(modifier = Modifier.size(10.dp)) {
                drawCircle(
                    color = Pebble,
                    radius = (size.minDimension - 1.dp.toPx()) / 2f,
                    style = Stroke(
                        width = 1.2.dp.toPx(),
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(3f, 3f), 0f)
                    )
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, fontSize = 11.sp, color = Stone)
    }
}
