package kr.co.oadan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.ErrorRed
import kr.co.oadan.ui.theme.Stone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafetyBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onStopFastingNow: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFBE9E7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.HealthAndSafety,
                        contentDescription = "건강 안전 안내",
                        tint = ErrorRed,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "공복을 멈추고 쉬어 주세요",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3단계 행동 지침
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFBF6F5))
                    .padding(16.dp)
            ) {
                InstructionRow(num = "1", text = "앉거나 누워서 잠시 편안히 쉬어 주세요.")
                Spacer(modifier = Modifier.height(8.dp))
                InstructionRow(num = "2", text = "주스나 사탕 등 당분을 조금 섭취하세요.")
                Spacer(modifier = Modifier.height(8.dp))
                InstructionRow(num = "3", text = "어지러움이 계속되면 즉시 의료진과 상담하세요.")
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "임신·수유 중이거나 당뇨 등 만성 질환이 있는 경우 단식 전 전문의 상담이 필요합니다. 오아단은 의료기기 및 진단 앱이 아닙니다.",
                fontSize = 12.sp,
                color = Stone,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    onStopFastingNow()
                    onDismissRequest()
                },
                colors = ButtonDefaults.buttonColors(containerColor = DeepInk),
                shape = RoundedCornerShape(999.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text("지금 공복 마치기", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            TextButton(
                onClick = onDismissRequest,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("괜찮아요, 계속할게요", color = Stone, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun InstructionRow(num: String, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = num,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = ErrorRed,
            modifier = Modifier.width(20.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = DeepInk,
            lineHeight = 20.sp
        )
    }
}
