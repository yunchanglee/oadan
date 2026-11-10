package kr.co.oadan.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.data.model.FastSession
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Stone
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FastingTimeEditDialog(
    session: FastSession,
    onDismiss: () -> Unit,
    onSave: (newStartTimeUtc: Long, newTargetEndTimeUtc: Long) -> Unit
) {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    var startText by remember {
        mutableStateOf(sdf.format(Date(session.startTimeUtc)))
    }
    var targetHoursText by remember {
        mutableStateOf(session.targetHours.toInt().toString())
    }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "단식 시간 수정", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text(
                    text = "시작 시간을 소급 수정하거나 목표 단식 시간을 변경할 수 있어요.",
                    fontSize = 13.sp,
                    color = Stone
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = startText,
                    onValueChange = {
                        startText = it
                        errorMsg = null
                    },
                    label = { Text("시작 일시 (YYYY-MM-DD HH:mm)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = targetHoursText,
                    onValueChange = {
                        targetHoursText = it.filter { c -> c.isDigit() }
                        errorMsg = null
                    },
                    label = { Text("목표 단식 시간 (시간 단위)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = errorMsg!!,
                        color = androidx.compose.ui.graphics.Color.Red,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    try {
                        val parsedStartDate = sdf.parse(startText)
                        val targetHours = targetHoursText.toFloatOrNull() ?: 16f
                        if (parsedStartDate == null || targetHours <= 0) {
                            errorMsg = "올바른 날짜와 시간을 입력해 주세요."
                            return@Button
                        }
                        val newStartTime = parsedStartDate.time
                        val newTargetEndTime = newStartTime + (targetHours * 3600f * 1000f).toLong()
                        onSave(newStartTime, newTargetEndTime)
                        onDismiss()
                    } catch (e: Exception) {
                        errorMsg = "날짜 형식(YYYY-MM-DD HH:mm)을 확인해 주세요."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
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
