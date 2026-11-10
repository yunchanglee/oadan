package kr.co.oadan.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.data.model.FastSession
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Stone
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// -------------------------------------------------------------
// 데이터 모델
// -------------------------------------------------------------
data class DateOption(
    val label: String,
    val year: Int,
    val month: Int, // 0-based
    val dayOfMonth: Int
)

data class HourOption(val hour: Int, val label: String)
data class MinuteOption(val minute: Int, val label: String)
data class TargetOption(val hours: Float, val label: String)

data class QuickPreset(
    val label: String,
    val calculateTimeMillis: () -> Long
)

// -------------------------------------------------------------
// 메인 다이얼로그
// -------------------------------------------------------------
@Composable
fun FastingTimeEditDialog(
    session: FastSession,
    onDismiss: () -> Unit,
    onSave: (newStartTimeUtc: Long, newTargetEndTimeUtc: Long) -> Unit
) {
    val scrollState = rememberScrollState()

    // 1. 날짜 리스트 생성 (오늘 ~ 6일 전, 세션 시작일 포함)
    val dateOptions = remember(session.startTimeUtc) {
        generateDateOptions(session.startTimeUtc)
    }

    // 2. 시간 리스트 (00시 ~ 23시)
    val hourOptions = remember {
        (0..23).map { h ->
            val amPm = if (h < 12) "오전" else "오후"
            val displayHour = when {
                h == 0 -> 12
                h > 12 -> h - 12
                else -> h
            }
            HourOption(
                hour = h,
                label = String.format(Locale.KOREA, "%s %02d시 (%02d:00)", amPm, displayHour, h)
            )
        }
    }

    // 3. 분 리스트 (00분 ~ 55분, 5분 단위)
    val minuteOptions = remember {
        (0..55 step 5).map { m ->
            MinuteOption(minute = m, label = String.format(Locale.KOREA, "%02d분", m))
        }
    }

    // 4. 목표 단식 시간 리스트
    val targetOptions = remember {
        listOf(
            TargetOption(12f, "12시간 (초보 입문 12:12)"),
            TargetOption(14f, "14시간 (순한 단식 14:10)"),
            TargetOption(16f, "16시간 (오아시스 표준 16:8)"),
            TargetOption(18f, "18시간 (심화 단식 18:6)"),
            TargetOption(20f, "20시간 (워리어 프로토콜 20:4)"),
            TargetOption(24f, "24시간 (1일 1식 OMAD)"),
            TargetOption(36f, "36시간 (클렌즈 단식)"),
            TargetOption(48f, "48시간 (리셋 단식)"),
            TargetOption(72f, "72시간 (최대 단식)")
        )
    }

    // 초기 상태 계산
    val initialCal = remember(session.startTimeUtc) {
        Calendar.getInstance().apply { timeInMillis = session.startTimeUtc }
    }

    var selectedDate by remember {
        val initialYear = initialCal.get(Calendar.YEAR)
        val initialMonth = initialCal.get(Calendar.MONTH)
        val initialDay = initialCal.get(Calendar.DAY_OF_MONTH)
        val matched = dateOptions.find {
            it.year == initialYear && it.month == initialMonth && it.dayOfMonth == initialDay
        } ?: dateOptions.first()
        mutableStateOf(matched)
    }

    var selectedHour by remember {
        val initialH = initialCal.get(Calendar.HOUR_OF_DAY)
        mutableStateOf(hourOptions.find { it.hour == initialH } ?: hourOptions[18])
    }

    var selectedMinute by remember {
        val initialM = initialCal.get(Calendar.MINUTE)
        val roundedM = (initialM / 5) * 5
        mutableStateOf(minuteOptions.find { it.minute == roundedM } ?: minuteOptions[0])
    }

    var selectedTarget by remember {
        val matched = targetOptions.find { it.hours == session.targetHours }
            ?: TargetOption(session.targetHours, "${session.targetHours.toInt()}시간")
        mutableStateOf(matched)
    }

    // 빠른 프리셋 목록
    val quickPresets = remember {
        listOf(
            QuickPreset("방금 전") {
                System.currentTimeMillis()
            },
            QuickPreset("1시간 전") {
                System.currentTimeMillis() - 1 * 3600 * 1000L
            },
            QuickPreset("2시간 전") {
                System.currentTimeMillis() - 2 * 3600 * 1000L
            },
            QuickPreset("어제 20:00") {
                getRelativeTime(daysAgo = 1, hour = 20, minute = 0)
            },
            QuickPreset("어제 19:00") {
                getRelativeTime(daysAgo = 1, hour = 19, minute = 0)
            },
            QuickPreset("어제 18:00") {
                getRelativeTime(daysAgo = 1, hour = 18, minute = 0)
            }
        )
    }

    // 선택된 시작 시각 계산
    val calculatedCal = Calendar.getInstance().apply {
        set(Calendar.YEAR, selectedDate.year)
        set(Calendar.MONTH, selectedDate.month)
        set(Calendar.DAY_OF_MONTH, selectedDate.dayOfMonth)
        set(Calendar.HOUR_OF_DAY, selectedHour.hour)
        set(Calendar.MINUTE, selectedMinute.minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val calculatedStartTime = calculatedCal.timeInMillis
    val now = System.currentTimeMillis()
    val isFuture = calculatedStartTime > now
    val calculatedEndTime = calculatedStartTime + (selectedTarget.hours * 3600f * 1000f).toLong()

    val timeFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = OasisTeal,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "단식 시간 수정",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "시작 시간을 소급하거나 목표 단식 시간을 리스트에서 선택해 보세요.",
                    fontSize = 13.sp,
                    color = Stone,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // -------------------------------------------------------------
                // 1. 빠른 프리셋 칩 목록
                // -------------------------------------------------------------
                Text(
                    text = "⚡ 빠른 시작 시간 선택",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickPresets.forEach { preset ->
                        FilterChip(
                            selected = false,
                            onClick = {
                                val targetTime = preset.calculateTimeMillis()
                                val pCal = Calendar.getInstance().apply { timeInMillis = targetTime }
                                val pYear = pCal.get(Calendar.YEAR)
                                val pMonth = pCal.get(Calendar.MONTH)
                                val pDay = pCal.get(Calendar.DAY_OF_MONTH)
                                val pHour = pCal.get(Calendar.HOUR_OF_DAY)
                                val pMin = (pCal.get(Calendar.MINUTE) / 5) * 5

                                dateOptions.find {
                                    it.year == pYear && it.month == pMonth && it.dayOfMonth == pDay
                                }?.let { selectedDate = it }

                                hourOptions.find { it.hour == pHour }?.let { selectedHour = it }
                                minuteOptions.find { it.minute == pMin }?.let { selectedMinute = it }
                            },
                            label = { Text(preset.label, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(999.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // -------------------------------------------------------------
                // 2. 단식 시작 날짜 리스트 선택
                // -------------------------------------------------------------
                DropdownSelector(
                    label = "시작 날짜 선택",
                    selectedValueText = selectedDate.label,
                    items = dateOptions,
                    itemLabel = { it.label },
                    onItemSelected = { selectedDate = it }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // -------------------------------------------------------------
                // 3. 단식 시작 시간(시) & 분 리스트 선택 (2열 배치)
                // -------------------------------------------------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DropdownSelector(
                        label = "시작 시각 (시)",
                        selectedValueText = selectedHour.label,
                        items = hourOptions,
                        itemLabel = { it.label },
                        onItemSelected = { selectedHour = it },
                        modifier = Modifier.weight(1.3f)
                    )
                    DropdownSelector(
                        label = "분",
                        selectedValueText = selectedMinute.label,
                        items = minuteOptions,
                        itemLabel = { it.label },
                        onItemSelected = { selectedMinute = it },
                        modifier = Modifier.weight(0.9f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // -------------------------------------------------------------
                // 4. 목표 단식 시간 리스트 선택
                // -------------------------------------------------------------
                DropdownSelector(
                    label = "목표 단식 시간 선택",
                    selectedValueText = selectedTarget.label,
                    items = targetOptions,
                    itemLabel = { it.label },
                    onItemSelected = { selectedTarget = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // -------------------------------------------------------------
                // 5. 실시간 변경 요약 카드
                // -------------------------------------------------------------
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isFuture) {
                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    },
                    border = BorderStroke(
                        1.dp,
                        if (isFuture) MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        if (isFuture) {
                            Text(
                                text = "⚠️ 시작 시각은 현재 시각보다 미래일 수 없어요.",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Text(
                                text = "🌿 변경 적용 미리보기",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = OasisTeal
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• 시작: ${timeFormat.format(Date(calculatedStartTime))}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "• 목표: ${timeFormat.format(Date(calculatedEndTime))} (${selectedTarget.hours.toInt()}시간)",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            val elapsedMillis = now - calculatedStartTime
                            val elapsedH = elapsedMillis / (3600 * 1000L)
                            val elapsedM = (elapsedMillis % (3600 * 1000L)) / (60 * 1000L)
                            val isCompleted = elapsedH >= selectedTarget.hours

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isCompleted) {
                                    "• 현재: ${elapsedH}시간 ${elapsedM}분 경과 (목표 달성 🎉)"
                                } else {
                                    "• 현재: ${elapsedH}시간 ${elapsedM}분 경과 (목표까지 ${selectedTarget.hours.toInt() - elapsedH}시간)"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isCompleted) OasisTeal else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (!isFuture) {
                        onSave(calculatedStartTime, calculatedEndTime)
                        onDismiss()
                    }
                },
                enabled = !isFuture,
                colors = ButtonDefaults.buttonColors(
                    containerColor = OasisTeal,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(999.dp)
            ) {
                Text("저장하기")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소", color = Stone)
            }
        }
    )
}

// -------------------------------------------------------------
// 공통 드롭다운 선택 컴포넌트 (텍스트 입력 없이 리스트 선택 전용)
// -------------------------------------------------------------
@Composable
private fun <T> DropdownSelector(
    label: String,
    selectedValueText: String,
    items: List<T>,
    itemLabel: (T) -> String,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            onClick = { expanded = true },
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedValueText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .heightIn(max = 240.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                items.forEach { item ->
                    val text = itemLabel(item)
                    val isSelected = text == selectedValueText
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = text,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) OasisTeal else MaterialTheme.colorScheme.onSurface
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = OasisTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 날짜 옵션 생성 헬퍼
// -------------------------------------------------------------
private fun generateDateOptions(initialTime: Long): List<DateOption> {
    val list = mutableListOf<DateOption>()
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat("M월 d일 (E)", Locale.KOREA)

    // 오늘부터 6일 전까지 (총 7일)
    for (i in 0..6) {
        val label = when (i) {
            0 -> "오늘 (${sdf.format(cal.time)})"
            1 -> "어제 (${sdf.format(cal.time)})"
            else -> "${i}일 전 (${sdf.format(cal.time)})"
        }
        list.add(
            DateOption(
                label = label,
                year = cal.get(Calendar.YEAR),
                month = cal.get(Calendar.MONTH),
                dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
            )
        )
        cal.add(Calendar.DAY_OF_YEAR, -1)
    }

    // 만약 세션 시작 시각이 6일 전보다 더 과거라면 해당 날짜 옵션도 추가
    val sessionCal = Calendar.getInstance().apply { timeInMillis = initialTime }
    val exists = list.any {
        it.year == sessionCal.get(Calendar.YEAR) &&
        it.month == sessionCal.get(Calendar.MONTH) &&
        it.dayOfMonth == sessionCal.get(Calendar.DAY_OF_MONTH)
    }
    if (!exists) {
        val fullSdf = SimpleDateFormat("yyyy년 M월 d일 (E)", Locale.KOREA)
        list.add(
            DateOption(
                label = fullSdf.format(sessionCal.time),
                year = sessionCal.get(Calendar.YEAR),
                month = sessionCal.get(Calendar.MONTH),
                dayOfMonth = sessionCal.get(Calendar.DAY_OF_MONTH)
            )
        )
    }
    return list
}

private fun getRelativeTime(daysAgo: Int, hour: Int, minute: Int): Long {
    return Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -daysAgo)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}
