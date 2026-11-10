package kr.co.oadan.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.WarningAmber

@Composable
fun Over72HoursDialog(
    onDismiss: () -> Unit,
    onAdjustTime: () -> Unit,
    onFinishNow: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "단식이 아직 진행 중인가요?", fontWeight = FontWeight.Bold, color = WarningAmber)
        },
        text = {
            Column {
                Text(
                    text = "공복 시간이 72시간 이상 경과되었습니다. 종료를 깜빡하셨다면 실제 종료된 시간으로 수정할 수 있어요.",
                    fontSize = 14.sp,
                    color = Stone,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "장시간 과도한 단식은 건강에 무리를 줄 수 있으니 주의해 주세요.",
                    fontSize = 12.sp,
                    color = WarningAmber
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onAdjustTime()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                shape = RoundedCornerShape(999.dp)
            ) {
                Text("종료 시간 수정하기")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onFinishNow()
                    onDismiss()
                }
            ) {
                Text("지금 종료하기", color = Stone)
            }
        }
    )
}
