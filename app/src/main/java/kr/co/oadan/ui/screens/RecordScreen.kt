package kr.co.oadan.ui.screens

import android.content.res.Configuration
import android.widget.Toast
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.data.model.ConditionLog
import kr.co.oadan.ui.components.SafetyBottomSheet
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.Dune
import kr.co.oadan.ui.theme.ErrorRed
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Pebble
import kr.co.oadan.ui.theme.Sand
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.Teal700

data class ConditionOption(
    val emoji: String,
    val name: String,
    val isWarning: Boolean
) {
    val fullTag: String get() = "$emoji $name"
}

val conditionOptions = listOf(
    ConditionOption("😊", "상쾌", false),
    ConditionOption("😐", "보통", false),
    ConditionOption("😣", "배고픔", false),
    ConditionOption("🤕", "두통", true),
    ConditionOption("💫", "어지러움", true)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    lastWeightKg: Float? = null,
    recentLogs: List<ConditionLog> = emptyList(),
    onSaveRecord: (
        weightKg: Float?,
        muscleKg: Float?,
        fatPercent: Float?,
        conditionTag: String?,
        memo: String?
    ) -> Unit,
    onStopFastingFromSafety: () -> Unit
) {
    val context = LocalContext.current

    var weightText by remember { mutableStateOf(lastWeightKg?.toString() ?: "") }
    var muscleText by remember { mutableStateOf("") }
    var fatText by remember { mutableStateOf("") }
    var selectedCondition by remember { mutableStateOf<String?>("😊 상쾌") }
    var memoText by remember { mutableStateOf("") }

    var showSafetySheet by remember { mutableStateOf(false) }
    val safetySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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
                .padding(top = 16.dp, bottom = 140.dp)
        ) {
            Text(
                text = "신체 & 컨디션 기록",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepInk
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "오늘의 몸 상태를 차분히 남겨두세요",
                fontSize = 14.sp,
                color = Stone
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 컨디션 태그 선택 (SC-51 트리거)
            Text(
                text = "지금 컨디션은 어때요?",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DeepInk
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                conditionOptions.forEach { option ->
                    val isSelected = selectedCondition == option.fullTag || selectedCondition?.contains(option.name) == true

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) {
                                    if (option.isWarning) Color(0xFFFBE9E7) else OasisTeal
                                } else Color.White
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) {
                                    if (option.isWarning) ErrorRed else OasisTeal
                                } else Pebble,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                selectedCondition = option.fullTag
                                // 어지러움이나 두통 선택 시 SafetyBottomSheet 자동 노출 (NFR-06)
                                if (option.isWarning) {
                                    showSafetySheet = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLandscape) {
                            // 가로 화면에서는 아이콘 + 텍스트 표시
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(text = option.emoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = option.name,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) {
                                        if (option.isWarning) Color(0xFF9E342D) else Color.White
                                    } else DeepInk
                                )
                            }
                        } else {
                            // 세로 화면에서는 아이콘(이모지)으로만 깔끔하게 표시하여 긴 텍스트 줄바꿈 방지
                            Text(
                                text = option.emoji,
                                fontSize = 22.sp
                            )
                        }
                    }
                }
            }

            // 세로 모드일 때 선택된 컨디션 상태 라벨 안내
            val currentSelectedOption = conditionOptions.find { selectedCondition == it.fullTag || selectedCondition?.contains(it.name) == true }
            if (currentSelectedOption != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "선택된 컨디션: ${currentSelectedOption.emoji} ${currentSelectedOption.name}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (currentSelectedOption.isWarning) ErrorRed else Teal700
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 체중 및 체성분 입력 카드
            Text(
                text = "체중 및 체성분",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DeepInk
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text("체중 (kg)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = muscleText,
                        onValueChange = { muscleText = it },
                        label = { Text("골격근량 (kg)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = fatText,
                        onValueChange = { fatText = it },
                        label = { Text("체지방률 (%)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 메모 입력
            Text(
                text = "오늘의 한 줄 메모",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DeepInk
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = memoText,
                onValueChange = { memoText = it },
                placeholder = { Text("오늘 느낀 공복의 기분이나 몸의 변화를 적어보세요.") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )

            // 최근 기록 히스토리 (최근 5건)
            if (recentLogs.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "최근 기록 내역",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepInk
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    recentLogs.take(5).forEach { log ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .padding(14.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = log.date,
                                        fontSize = 12.sp,
                                        color = Stone,
                                        fontWeight = FontWeight.Medium
                                    )
                                    log.conditionTag?.let { tag ->
                                        Text(
                                            text = tag,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (tag.contains("두통") || tag.contains("어지러움")) ErrorRed else Teal700
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    log.weightKg?.let { w ->
                                        Text(
                                            text = "${w}kg",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepInk
                                        )
                                    }
                                    if (log.skeletalMuscleKg != null || log.bodyFatPercent != null) {
                                        Text(
                                            text = "골격근: ${log.skeletalMuscleKg ?: "-"}kg · 체지방: ${log.bodyFatPercent ?: "-"}%",
                                            fontSize = 12.sp,
                                            color = Stone
                                        )
                                    }
                                }

                                if (!log.memo.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = log.memo,
                                        fontSize = 12.sp,
                                        color = Stone,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 하단 저장 CTA (네비게이션 바 패딩 적용으로 3버튼/제스처 소프트키 겹침 방지)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Sand)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Button(
                onClick = {
                    onSaveRecord(
                        weightText.toFloatOrNull(),
                        muscleText.toFloatOrNull(),
                        fatText.toFloatOrNull(),
                        selectedCondition,
                        memoText.ifBlank { null }
                    )
                    Toast.makeText(context, "기록이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                shape = RoundedCornerShape(999.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "기록 저장하기", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        // SC-51 건강 안전 안내 바텀시트
        if (showSafetySheet) {
            SafetyBottomSheet(
                sheetState = safetySheetState,
                onDismissRequest = { showSafetySheet = false },
                onStopFastingNow = {
                    onStopFastingFromSafety()
                    showSafetySheet = false
                }
            )
        }
    }
}
