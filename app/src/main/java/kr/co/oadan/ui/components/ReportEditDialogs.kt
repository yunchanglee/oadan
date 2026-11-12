package kr.co.oadan.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.data.model.ConditionLog
import kr.co.oadan.data.model.FastSession
import kr.co.oadan.ui.screens.conditionOptions
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.Dune
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.SunsetCoral
import kr.co.oadan.ui.theme.Teal50
import kr.co.oadan.ui.theme.Teal700
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// ------------------------------------------------------------------
// 1. 단식 기록 수정 / 추가 다이얼로그 (ReportFastEditDialog)
// ------------------------------------------------------------------
@Composable
fun ReportFastEditDialog(
    session: FastSession?,
    targetDateStr: String, // YYYY-MM-DD
    onDismiss: () -> Unit,
    onSave: (FastSession) -> Unit,
    onDelete: ((FastSession) -> Unit)? = null
) {
    val scrollState = rememberScrollState()
    val sdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    // 기준 날짜 캘린더 생성
    val baseDate = remember(targetDateStr) {
        try {
            sdf.parse(targetDateStr) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }

    // 날짜 옵션 생성 (기준 날짜 전후 7일)
    val dateOptions = remember(baseDate) {
        val cal = Calendar.getInstance().apply { time = baseDate }
        val list = mutableListOf<DateOption>()
        for (offset in -7..7) {
            val c = Calendar.getInstance().apply {
                time = baseDate
                add(Calendar.DAY_OF_YEAR, offset)
            }
            val y = c.get(Calendar.YEAR)
            val m = c.get(Calendar.MONTH)
            val d = c.get(Calendar.DAY_OF_MONTH)
            val label = String.format(Locale.KOREA, "%04d.%02d.%02d (%s)", y, m + 1, d,
                SimpleDateFormat("E", Locale.KOREA).format(c.time))
            list.add(DateOption(label, y, m, d))
        }
        list
    }

    // 시간 옵션 (0..23)
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

    // 분 옵션 (0..55 step 5)
    val minuteOptions = remember {
        (0..55 step 5).map { m ->
            MinuteOption(minute = m, label = String.format(Locale.KOREA, "%02d분", m))
        }
    }

    // 목표 시간 옵션
    val targetOptions = remember {
        listOf(
            TargetOption(12f, "12시간 (초보 입문 12:12)"),
            TargetOption(14f, "14시간 (순한 단식 14:10)"),
            TargetOption(16f, "16시간 (오아시스 표준 16:8)"),
            TargetOption(18f, "18시간 (심화 단식 18:6)"),
            TargetOption(20f, "20시간 (워리어 프로토콜 20:4)"),
            TargetOption(24f, "24시간 (1일 1식 OMAD)")
        )
    }

    // 초기 시작 및 종료 시간 산출
    val initialStartCal = remember(session, baseDate) {
        Calendar.getInstance().apply {
            if (session != null) {
                timeInMillis = session.startTimeUtc
            } else {
                time = baseDate
                set(Calendar.HOUR_OF_DAY, 20)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }
    }

    val initialEndCal = remember(session, baseDate) {
        Calendar.getInstance().apply {
            if (session != null) {
                timeInMillis = session.actualEndTimeUtc ?: session.targetEndTimeUtc
            } else {
                time = baseDate
                set(Calendar.HOUR_OF_DAY, 12)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.DAY_OF_YEAR, 1) // 다음 날 낮 12시 (16시간 단식)
            }
        }
    }

    var selectedStartDate by remember {
        val y = initialStartCal.get(Calendar.YEAR)
        val m = initialStartCal.get(Calendar.MONTH)
        val d = initialStartCal.get(Calendar.DAY_OF_MONTH)
        mutableStateOf(dateOptions.find { it.year == y && it.month == m && it.dayOfMonth == d } ?: dateOptions[7])
    }
    var selectedStartHour by remember {
        val h = initialStartCal.get(Calendar.HOUR_OF_DAY)
        mutableStateOf(hourOptions.find { it.hour == h } ?: hourOptions[20])
    }
    var selectedStartMinute by remember {
        val min = (initialStartCal.get(Calendar.MINUTE) / 5) * 5
        mutableStateOf(minuteOptions.find { it.minute == min } ?: minuteOptions[0])
    }

    var selectedEndDate by remember {
        val y = initialEndCal.get(Calendar.YEAR)
        val m = initialEndCal.get(Calendar.MONTH)
        val d = initialEndCal.get(Calendar.DAY_OF_MONTH)
        mutableStateOf(dateOptions.find { it.year == y && it.month == m && it.dayOfMonth == d } ?: dateOptions[8])
    }
    var selectedEndHour by remember {
        val h = initialEndCal.get(Calendar.HOUR_OF_DAY)
        mutableStateOf(hourOptions.find { it.hour == h } ?: hourOptions[12])
    }
    var selectedEndMinute by remember {
        val min = (initialEndCal.get(Calendar.MINUTE) / 5) * 5
        mutableStateOf(minuteOptions.find { it.minute == min } ?: minuteOptions[0])
    }

    var selectedTarget by remember {
        val targetH = session?.targetHours ?: 16f
        mutableStateOf(targetOptions.find { it.hours == targetH } ?: targetOptions[2])
    }

    // 계산된 밀리초
    val calculatedStartTime = remember(selectedStartDate, selectedStartHour, selectedStartMinute) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedStartDate.year)
            set(Calendar.MONTH, selectedStartDate.month)
            set(Calendar.DAY_OF_MONTH, selectedStartDate.dayOfMonth)
            set(Calendar.HOUR_OF_DAY, selectedStartHour.hour)
            set(Calendar.MINUTE, selectedStartMinute.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    val calculatedEndTime = remember(selectedEndDate, selectedEndHour, selectedEndMinute) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedEndDate.year)
            set(Calendar.MONTH, selectedEndDate.month)
            set(Calendar.DAY_OF_MONTH, selectedEndDate.dayOfMonth)
            set(Calendar.HOUR_OF_DAY, selectedEndHour.hour)
            set(Calendar.MINUTE, selectedEndMinute.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    // 계산된 총 공복 시간 (밀리초 및 시간/분)
    val totalDiffMillis = calculatedEndTime - calculatedStartTime
    val isValidDuration = totalDiffMillis > 0
    val totalHoursFloat = (totalDiffMillis / (3600f * 1000f)).coerceAtLeast(0f)
    val displayHours = (totalDiffMillis / (3600000L)).coerceAtLeast(0L)
    val displayMinutes = ((totalDiffMillis % 3600000L) / 60000L).coerceAtLeast(0L)
    val isCompleted = totalHoursFloat >= selectedTarget.hours

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (session != null) "$targetDateStr 단식 기록 수정" else "$targetDateStr 단식 기록 추가",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                // 1. 단식 시작 일시
                Text(
                    text = "단식 시작 일시",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                DropdownSelector(
                    label = "시작 날짜",
                    selectedValueText = selectedStartDate.label,
                    items = dateOptions,
                    itemLabel = { it.label },
                    onItemSelected = { selectedStartDate = it }
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DropdownSelector(
                        label = "시작 시",
                        selectedValueText = selectedStartHour.label,
                        items = hourOptions,
                        itemLabel = { it.label },
                        onItemSelected = { selectedStartHour = it },
                        modifier = Modifier.weight(1.3f)
                    )
                    DropdownSelector(
                        label = "시작 분",
                        selectedValueText = selectedStartMinute.label,
                        items = minuteOptions,
                        itemLabel = { it.label },
                        onItemSelected = { selectedStartMinute = it },
                        modifier = Modifier.weight(0.9f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. 단식 종료 일시
                Text(
                    text = "단식 종료 일시",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                DropdownSelector(
                    label = "종료 날짜",
                    selectedValueText = selectedEndDate.label,
                    items = dateOptions,
                    itemLabel = { it.label },
                    onItemSelected = { selectedEndDate = it }
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DropdownSelector(
                        label = "종료 시",
                        selectedValueText = selectedEndHour.label,
                        items = hourOptions,
                        itemLabel = { it.label },
                        onItemSelected = { selectedEndHour = it },
                        modifier = Modifier.weight(1.3f)
                    )
                    DropdownSelector(
                        label = "종료 분",
                        selectedValueText = selectedEndMinute.label,
                        items = minuteOptions,
                        itemLabel = { it.label },
                        onItemSelected = { selectedEndMinute = it },
                        modifier = Modifier.weight(0.9f)
                    )
                }

                // 빠른 종료 맞춤 칩
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Surface(
                        onClick = {
                            val autoEndCal = Calendar.getInstance().apply {
                                timeInMillis = calculatedStartTime + (selectedTarget.hours * 3600f * 1000f).toLong()
                            }
                            val ey = autoEndCal.get(Calendar.YEAR)
                            val em = autoEndCal.get(Calendar.MONTH)
                            val ed = autoEndCal.get(Calendar.DAY_OF_MONTH)
                            val eh = autoEndCal.get(Calendar.HOUR_OF_DAY)
                            val emin = (autoEndCal.get(Calendar.MINUTE) / 5) * 5

                            dateOptions.find { it.year == ey && it.month == em && it.dayOfMonth == ed }?.let {
                                selectedEndDate = it
                            }
                            hourOptions.find { it.hour == eh }?.let { selectedEndHour = it }
                            minuteOptions.find { it.minute == emin }?.let { selectedEndMinute = it }
                        },
                        shape = RoundedCornerShape(999.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = "⚡ 목표시간(+${selectedTarget.hours.toInt()}h)으로 종료 자동 맞춤",
                            fontSize = 11.sp,
                            color = OasisTeal,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. 목표 단식 플랜
                DropdownSelector(
                    label = "목표 단식 플랜",
                    selectedValueText = selectedTarget.label,
                    items = targetOptions,
                    itemLabel = { it.label },
                    onItemSelected = { selectedTarget = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 4. 요약 프리뷰 카드
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isValidDuration) {
                        if (isCompleted) Teal50 else Color(0xFFFBF4EC)
                    } else Color(0xFFFDE8E8),
                    border = BorderStroke(
                        1.dp,
                        if (isValidDuration) {
                            if (isCompleted) OasisTeal.copy(alpha = 0.5f) else SunsetCoral.copy(alpha = 0.5f)
                        } else Color(0xFFE53935)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        if (!isValidDuration) {
                            Text(
                                text = "⚠️ 종료 시간이 시작 시간보다 앞서 있습니다.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE53935)
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "계산된 총 공복 시간",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (isCompleted) "목표 달성 ✓" else "부분 달성 △",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCompleted) Teal700 else SunsetCoral
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${displayHours}시간 ${displayMinutes}분",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isValidDuration) {
                        val protocolName = when (selectedTarget.hours.toInt()) {
                            12 -> "12:12"
                            14 -> "14:10"
                            16 -> "16:8"
                            18 -> "18:6"
                            20 -> "20:4"
                            24 -> "24:0"
                            else -> "${selectedTarget.hours.toInt()}:${24 - selectedTarget.hours.toInt()}"
                        }
                        val updated = (session ?: FastSession(
                            startTimeUtc = calculatedStartTime,
                            targetEndTimeUtc = calculatedStartTime + (selectedTarget.hours * 3600f * 1000f).toLong(),
                            protocol = protocolName,
                            targetHours = selectedTarget.hours,
                            date = targetDateStr
                        )).copy(
                            startTimeUtc = calculatedStartTime,
                            targetEndTimeUtc = calculatedStartTime + (selectedTarget.hours * 3600f * 1000f).toLong(),
                            actualEndTimeUtc = calculatedEndTime,
                            protocol = protocolName,
                            targetHours = selectedTarget.hours,
                            fastedHours = totalHoursFloat,
                            isCompleted = isCompleted,
                            isPartiallyCompleted = !isCompleted,
                            date = targetDateStr
                        )
                        onSave(updated)
                        onDismiss()
                    }
                },
                enabled = isValidDuration,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SunsetCoral,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(999.dp)
            ) {
                Text("저장하기", color = Color.White)
            }
        },
        dismissButton = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (session != null && onDelete != null) {
                    TextButton(
                        onClick = {
                            onDelete(session)
                            onDismiss()
                        }
                    ) {
                        Text("삭제", color = Color(0xFFC2453D))
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("취소", color = Stone)
                }
            }
        }
    )
}

// ------------------------------------------------------------------
// 2. 체중 & 컨디션 기록 수정 다이얼로그 (ReportConditionEditDialog)
// ------------------------------------------------------------------
@Composable
fun ReportConditionEditDialog(
    dateStr: String,
    existingLog: ConditionLog?,
    onDismiss: () -> Unit,
    onSave: (weightKg: Float?, muscleKg: Float?, fatPercent: Float?, conditionTag: String?, memo: String?) -> Unit,
    onDelete: (() -> Unit)? = null
) {
    var weightText by remember {
        mutableStateOf(existingLog?.weightKg?.let { if (it % 1f == 0f) it.toInt().toString() else it.toString() } ?: "")
    }
    var selectedCondition by remember {
        mutableStateOf(existingLog?.conditionTag ?: "😊 상쾌")
    }
    var memoText by remember {
        mutableStateOf(existingLog?.memo ?: "")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "$dateStr 체중 · 컨디션 기록",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // 체중 입력
                Text(
                    text = "체중 (kg)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    placeholder = { Text("예: 68.5") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 컨디션 선택
                Text(
                    text = "컨디션 상태",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    conditionOptions.forEach { opt ->
                        val isSelected = selectedCondition == opt.fullTag
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCondition = opt.fullTag },
                            label = { Text(opt.fullTag, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = if (opt.isWarning) SunsetCoral.copy(alpha = 0.2f) else OasisTeal.copy(alpha = 0.2f),
                                selectedLabelColor = if (opt.isWarning) SunsetCoral else OasisTeal
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 메모 입력
                Text(
                    text = "한 줄 메모 (선택)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = memoText,
                    onValueChange = { memoText = it },
                    placeholder = { Text("가벼운 컨디션 유지 중") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val weight = weightText.toFloatOrNull()
                    onSave(weight, null, null, selectedCondition, memoText.ifBlank { null })
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = SunsetCoral),
                shape = RoundedCornerShape(999.dp)
            ) {
                Text("저장하기", color = Color.White)
            }
        },
        dismissButton = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (existingLog != null && onDelete != null) {
                    TextButton(
                        onClick = {
                            onDelete()
                            onDismiss()
                        }
                    ) {
                        Text("삭제", color = Color(0xFFC2453D))
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("취소", color = Stone)
                }
            }
        }
    )
}

// ------------------------------------------------------------------
// 3. 수분 섭취량 수정 다이얼로그 (ReportWaterEditDialog)
// ------------------------------------------------------------------
@Composable
fun ReportWaterEditDialog(
    dateStr: String,
    currentTotalMl: Int,
    onDismiss: () -> Unit,
    onSave: (totalMl: Int) -> Unit
) {
    var waterMl by remember { mutableIntStateOf(currentTotalMl) }

    val presetAmounts = listOf(500, 1000, 1500, 2000, 2500)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "$dateStr 수분 섭취량 기록",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "이 날 마신 총 물의 양",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = String.format(Locale.KOREA, "%,d ml (%.1fL)", waterMl, waterMl / 1000f),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = OasisTeal
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 빠른 설정 칩
                Text(
                    text = "빠른 선택",
                    fontSize = 12.sp,
                    color = Stone,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presetAmounts.forEach { amount ->
                        Surface(
                            onClick = { waterMl = amount },
                            shape = RoundedCornerShape(999.dp),
                            color = if (waterMl == amount) OasisTeal else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = "${amount}ml",
                                fontSize = 12.sp,
                                fontWeight = if (waterMl == amount) FontWeight.Bold else FontWeight.Normal,
                                color = if (waterMl == amount) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 미세 조정 버튼 (-250ml, +250ml, 0ml)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { waterMl = (waterMl - 250).coerceAtLeast(0) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("-250ml", color = MaterialTheme.colorScheme.onSurface)
                    }
                    Button(
                        onClick = { waterMl = 0 },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("초기화", color = Stone)
                    }
                    Button(
                        onClick = { waterMl = (waterMl + 250).coerceAtMost(5000) },
                        colors = ButtonDefaults.buttonColors(containerColor = OasisTeal.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("+250ml", color = OasisTeal, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(waterMl)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = SunsetCoral),
                shape = RoundedCornerShape(999.dp)
            ) {
                Text("저장하기", color = Color.White)
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
// 공통 드롭다운 선택 컴포넌트
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
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Surface(
                onClick = { expanded = true },
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedValueText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
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
                                    fontSize = 12.sp,
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
