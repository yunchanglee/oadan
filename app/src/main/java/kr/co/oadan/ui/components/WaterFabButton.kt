package kr.co.oadan.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Teal50
import kr.co.oadan.ui.theme.Teal700
import kr.co.oadan.ui.theme.Teal800

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WaterFabButton(
    currentWaterMl: Int,
    dailyGoalMl: Int = 2000,
    onAddWater: (amountMl: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var customAmountText by remember { mutableStateOf("") }

    val waterRatio = (currentWaterMl.toFloat() / dailyGoalMl.toFloat()).coerceIn(0f, 1f)
    val animatedHeightRatio by animateFloatAsState(
        targetValue = waterRatio,
        animationSpec = tween(500),
        label = "waterFabLevel"
    )

    Box(
        modifier = modifier
            .size(64.dp)
            .shadow(elevation = 8.dp, shape = CircleShape)
            .clip(CircleShape)
            .combinedClickable(
                onClick = { onAddWater(250) },
                onLongClick = { showDialog = true }
            ),
        contentAlignment = Alignment.Center
    ) {
        // 배경 & 수위 상승 Canvas
        Canvas(modifier = Modifier.size(64.dp)) {
            // 흰색 바탕
            drawRect(color = Color.White)

            // 아래에서부터 차오르는 물 수위
            val levelHeight = size.height * animatedHeightRatio
            val levelTop = size.height - levelHeight
            val waterBrush = Brush.verticalGradient(
                colors = listOf(Color(0xFFC6E7E0), Color(0xFF9DD5CA)),
                startY = levelTop,
                endY = size.height
            )
            drawRect(
                brush = waterBrush,
                topLeft = Offset(0f, levelTop),
                size = Size(size.width, levelHeight)
            )
        }

        // 아이콘 & 섭취량 라벨
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.WaterDrop,
                contentDescription = "수분 기록",
                tint = Teal700,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = String.format("%.1fL", currentWaterMl / 1000f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Teal800
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "수분 섭취 기록", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        text = "자주 마시는 용량을 선택하거나 직접 입력하세요.",
                        fontSize = 13.sp,
                        color = Color(0xFF5C6663)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(150, 250, 500).forEach { amount ->
                            Button(
                                onClick = {
                                    onAddWater(amount)
                                    showDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Teal50,
                                    contentColor = Teal700
                                ),
                                shape = RoundedCornerShape(999.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                            ) {
                                Text("${amount}ml", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = customAmountText,
                        onValueChange = { customAmountText = it.filter { char -> char.isDigit() } },
                        label = { Text("직접 입력 (ml)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = customAmountText.toIntOrNull() ?: 250
                        onAddWater(amount)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OasisTeal)
                ) {
                    Text("추가하기")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("취소", color = Color(0xFF5C6663))
                }
            }
        )
    }
}
