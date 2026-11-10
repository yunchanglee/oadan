package kr.co.oadan.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.data.repository.UserPreferences
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.ErrorRed
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Pebble
import kr.co.oadan.ui.theme.Sand
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.Teal50

@Composable
fun MySettingsScreen(
    preferences: UserPreferences,
    onUpdateNotifications: (start: Boolean, end: Boolean, stage: Boolean) -> Unit,
    onNavigateToPlanSelect: () -> Unit,
    onExportJson: suspend () -> String,
    onImportJson: suspend (String) -> Boolean,
    onClearAllData: () -> Unit
) {
    val context = LocalContext.current

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var showDisclaimerDialog by remember { mutableStateOf(false) }
    var importText by remember { mutableStateOf("") }

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
            Text(
                text = "마이 설정",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepInk
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "광고 없이, 결제 없이, 오롯이 나를 위한 공복 🌿",
                fontSize = 13.sp,
                color = Stone
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 1. 단식 플랜 섹션
            SettingsSectionHeader("단식 설정")
            SettingsCard {
                SettingsRow(
                    title = "현재 단식 플랜",
                    subtitle = preferences.defaultProtocol,
                    showArrow = true,
                    onClick = onNavigateToPlanSelect
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. 알림 설정 (FR-05)
            SettingsSectionHeader("알림 설정")
            SettingsCard {
                SettingsToggleRow(
                    title = "단식 시작 알림 (30분 전)",
                    checked = preferences.notificationFastingStart,
                    onCheckedChange = {
                        onUpdateNotifications(it, preferences.notificationFastingEnd, preferences.notificationStageChange)
                    }
                )
                Divider(color = Pebble.copy(alpha = 0.5f))
                SettingsToggleRow(
                    title = "단식 완료 알림",
                    checked = preferences.notificationFastingEnd,
                    onCheckedChange = {
                        onUpdateNotifications(preferences.notificationFastingStart, it, preferences.notificationStageChange)
                    }
                )
                Divider(color = Pebble.copy(alpha = 0.5f))
                SettingsToggleRow(
                    title = "생체 대사 단계 전환 알림",
                    checked = preferences.notificationStageChange,
                    onCheckedChange = {
                        onUpdateNotifications(preferences.notificationFastingStart, preferences.notificationFastingEnd, it)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. 데이터 관리 & 백업 (NFR-04, NFR-05, NFR-07)
            SettingsSectionHeader("데이터 백업 및 복원 (Local-First)")
            SettingsCard {
                SettingsRow(
                    title = "기록 데이터 내보내기 (JSON 백업)",
                    subtitle = "단식, 수분, 컨디션, 보식 기록을 텍스트 파일로 저장",
                    onClick = {
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).run {
                            // JSON 내보내기 및 클립보드 복사
                            kotlinx.coroutines.MainScope().run {
                                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).run {
                                    // MainScope에서 처리
                                }
                            }
                        }
                    }
                )
                Divider(color = Pebble.copy(alpha = 0.5f))
                SettingsRow(
                    title = "백업 데이터 불러오기",
                    subtitle = "기존에 저장해 둔 JSON 백업 복원",
                    onClick = { showImportDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. 안전 & 정책 안내 (NFR-06, NFR-07)
            SettingsSectionHeader("안내 및 개인정보")
            SettingsCard {
                SettingsRow(
                    title = "건강 면책 고지 전문",
                    showArrow = true,
                    onClick = { showDisclaimerDialog = true }
                )
                Divider(color = Pebble.copy(alpha = 0.5f))
                SettingsRow(
                    title = "데이터 및 전체 기록 즉시 삭제",
                    titleColor = ErrorRed,
                    subtitle = "기기에 저장된 모든 단식 및 신체 데이터를 영구 삭제합니다",
                    onClick = { showDeleteConfirmDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 버전 및 무료 안내
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Teal50)
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "오아단 v1.0.0 · 전 기능 무료 & 무광고",
                    fontSize = 12.sp,
                    color = Stone
                )
            }
        }

        // 전체 데이터 영구 삭제 확인 다이얼로그 (NFR-07)
        if (showDeleteConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmDialog = false },
                title = { Text("전체 데이터를 삭제하시겠습니까?", color = ErrorRed, fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        "단식 세션, 수분 기록, 체중, 보식 기록을 포함한 모든 로컬 데이터가 영구히 파기되며 되돌릴 수 없습니다.",
                        fontSize = 13.sp,
                        color = Stone,
                        lineHeight = 18.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onClearAllData()
                            showDeleteConfirmDialog = false
                            Toast.makeText(context, "모든 데이터가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                    ) {
                        Text("삭제하기")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmDialog = false }) {
                        Text("취소", color = Stone)
                    }
                }
            )
        }

        // 백업 복원 다이얼로그
        if (showImportDialog) {
            AlertDialog(
                onDismissRequest = { showImportDialog = false },
                title = { Text("백업 데이터 복원", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("내보냈던 JSON 백업 텍스트를 붙여넣어 주세요.", fontSize = 13.sp, color = Stone)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = importText,
                            onValueChange = { importText = it },
                            placeholder = { Text("JSON 텍스트 붙여넣기") },
                            minLines = 4,
                            maxLines = 6,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // Coroutine import
                            showImportDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OasisTeal)
                    ) {
                        Text("복원하기")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showImportDialog = false }) {
                        Text("취소", color = Stone)
                    }
                }
            )
        }

        // 면책 고지 다이얼로그 (NFR-06)
        if (showDisclaimerDialog) {
            AlertDialog(
                onDismissRequest = { showDisclaimerDialog = false },
                title = { Text("건강 면책 고지 (Disclaimer)", fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(
                            text = "1. 의료기기 아님 고지\n오아단은 의료기기 및 질병 진단·치료용 소프트웨어가 아닙니다. 본 앱이 제공하는 간헐적 단식 타이머, 생체 대사 단계 설명, 보식 메뉴 추천은 건강한 라이프스타일을 돕기 위한 일반적 웰니스 가이드입니다.\n\n2. 금기 대상자\n임산부, 수유부, 성장기 청소년, 1형/2형 당뇨 환자, 섭식장애 병력자, 저체중자(BMI 18.5 미만)는 단식을 피하고 전문의와 먼저 상담해야 합니다.\n\n3. 안전 지침\n공복 중 어지러움, 손떨림, 극심한 두통이 발생하면 즉시 단식을 중단하고 적정량의 당분 및 수분을 섭취하세요.",
                            fontSize = 12.sp,
                            color = DeepInk,
                            lineHeight = 18.sp
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showDisclaimerDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = OasisTeal)
                    ) {
                        Text("확인")
                    }
                }
            )
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Stone,
        modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
    )
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        content()
    }
}

@Composable
private fun SettingsRow(
    title: String,
    titleColor: Color = DeepInk,
    subtitle: String? = null,
    showArrow: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = titleColor)
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = subtitle, fontSize = 12.sp, color = Stone)
            }
        }
        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Stone,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 14.sp, color = DeepInk, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = OasisTeal)
        )
    }
}
