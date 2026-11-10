package kr.co.oadan.ui.screens

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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kr.co.oadan.data.model.FastSession
import kr.co.oadan.domain.EncouragementMessageProvider
import kr.co.oadan.domain.FastTimerEngine
import kr.co.oadan.domain.FastingTimerState
import kr.co.oadan.domain.MetabolicStage
import kr.co.oadan.ui.components.FastingDefenseDialog
import kr.co.oadan.ui.components.FastingTimeEditDialog
import kr.co.oadan.ui.components.HealthyBodyIllustration
import kr.co.oadan.ui.components.MetabolicStageBottomSheet
import kr.co.oadan.ui.components.OasisTimer
import kr.co.oadan.ui.components.Over72HoursDialog
import kr.co.oadan.ui.components.WaterFabButton
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.Dune
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Sand
import kr.co.oadan.ui.theme.StageLagoon
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.SunsetCoral
import kr.co.oadan.ui.theme.Teal50
import kr.co.oadan.ui.theme.Teal700
import kr.co.oadan.ui.theme.Teal800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    activeSession: FastSession?,
    currentWaterMl: Int,
    dailyWaterGoalMl: Int,
    onStartFasting: () -> Unit,
    onEndFasting: (isPartial: Boolean) -> Unit,
    onUpdateSessionTimes: (sessionId: Long, newStart: Long, newTargetEnd: Long) -> Unit,
    onUpdateTargetHours: (Float) -> Unit,
    onAddWater: (Int) -> Unit,
    onNavigateToComplete: (fastedHours: Float) -> Unit,
    onNavigateToPlanSelect: () -> Unit
) {
    // 실시간 1초 단위 시간 갱신
    var currentTimeUtc by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTimeUtc = System.currentTimeMillis()
            delay(1000)
        }
    }

    // 바텀시트 & 다이얼로그 상태
    var showStageSheet by remember { mutableStateOf(false) }
    var showDefenseDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showOver72Dialog by remember { mutableStateOf(false) }

    val stageSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val timerState: FastingTimerState? = if (activeSession != null) {
        FastTimerEngine.calculateState(
            startTimeUtc = activeSession.startTimeUtc,
            targetEndTimeUtc = activeSession.targetEndTimeUtc,
            currentTimeUtc = currentTimeUtc
        )
    } else null

    // 72시간 초과 팝업 트리거
    LaunchedEffect(timerState?.isOver72Hours) {
        if (timerState?.isOver72Hours == true) {
            showOver72Dialog = true
        }
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
                .padding(bottom = 20.dp)
        ) {
            // App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${FastTimerEngine.formatTodayDateKorean()} · ${activeSession?.protocol ?: "휴식 중"}",
                        fontSize = 12.sp,
                        color = Stone
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "오늘의 공복",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                IconButton(onClick = onNavigateToPlanSelect) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "알림 및 플랜",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            if (activeSession != null && timerState != null) {
                // 단식 중 상태 (SC-11)
                Spacer(modifier = Modifier.height(6.dp))

                // 현재 대사 단계 배지 (클릭 시 바텀시트 오픈)
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(Teal50)
                            .clickable { showStageSheet = true }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${timerState.currentStage.emoji} ${timerState.currentStage.stageName} 단계",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Teal700
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Teal700,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hero 컴포넌트: OasisTimer
                OasisTimer(
                    timerState = timerState,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 시작 및 목표 종료 시각 (수동 소급 수정 지원)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.clickable { showEditDialog = true }
                    ) {
                        Text(text = "시작", fontSize = 12.sp, color = Stone)
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = timerState.startTimeFormatted,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "시작 시간 수정",
                                tint = OasisTeal,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "목표 종료", fontSize = 12.sp, color = Stone)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = timerState.targetEndTimeFormatted,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 다음 단계 안내 카드 (SC-11)
                if (timerState.nextStage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        // SC-11 좌측 인디케이터 컬러 바
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .width(5.dp)
                                .height(46.dp)
                                .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                                .background(Teal800)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "✨", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${timerState.nextStage.stageName}까지 ",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = FastTimerEngine.formatRemainingKorean(timerState.nextStageRemainingMillis),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OasisTeal
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = timerState.nextStage.description,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 공복 응원 메시지 영역 (공복 진행 중 따뜻한 격려 또는 목표 초과 달성 칭찬)
                val elapsedHours = (timerState.elapsedMillis / (3600f * 1000f)).toInt()
                if (timerState.isGoalReached) {
                    // 목표 시간(지정 기간) 초과 시 응원 카드
                    val overachievedHours = ((timerState.elapsedMillis - activeSession.targetHours * 3600000f) / 3600000f).toInt().coerceAtLeast(0)
                    val overCheer = remember(overachievedHours) {
                        EncouragementMessageProvider.getOverachievedMessage(overachievedHours)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SunsetCoral.copy(alpha = 0.12f))
                            .border(1.dp, SunsetCoral.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = overCheer.emoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = overCheer.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SunsetCoral
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = overCheer.message,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                } else {
                    // 공복 진행 중 격려 카드 (터치 시 다른 응원 메시지 리프레시)
                    var cheerKey by remember { mutableStateOf(0) }
                    val midCheer = remember(elapsedHours, cheerKey) {
                        if (cheerKey == 0) {
                            EncouragementMessageProvider.getMidFastMessage(elapsedHours)
                        } else {
                            EncouragementMessageProvider.getRandomMidFastCheer()
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Teal50)
                            .border(1.dp, OasisTeal.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                            .clickable { cheerKey++ }
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = midCheer.emoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = midCheer.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OasisTeal
                                    )
                                    Text(
                                        text = "터치하여 다른 응원 보기 ↻",
                                        fontSize = 10.sp,
                                        color = Stone
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = midCheer.message,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // SC-11: 하단 액션 영역 (좌측: 공복 마치기 버튼 / 우측: 물마심 원형 버튼)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. 공복 마치기 버튼 (충동적 종료 방지 Tonal 스타일 + 알약형)
                    Button(
                        onClick = {
                            if (!timerState.isGoalReached) {
                                showDefenseDialog = true
                            } else {
                                val elapsedHours = timerState.elapsedMillis / (3600f * 1000f)
                                onEndFasting(false)
                                onNavigateToComplete(elapsedHours)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Teal50,
                            contentColor = OasisTeal
                        ),
                        shape = RoundedCornerShape(999.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .padding(end = 16.dp)
                    ) {
                        Text(
                            text = if (timerState.isGoalReached) "목표 달성! 공복 마치기" else "공복 마치기",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = OasisTeal
                        )
                    }

                    // 2. 수분 원터치 기록 원형 버튼
                    WaterFabButton(
                        currentWaterMl = currentWaterMl,
                        dailyGoalMl = dailyWaterGoalMl,
                        onAddWater = onAddWater
                    )
                }

            } else {
                // 단식 대기 상태 (SC-10)
                Spacer(modifier = Modifier.height(40.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HealthyBodyIllustration(
                        canvasSize = 180.dp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "오늘의 공복을 시작해 볼까요?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "편안히 쉬어가며 속을 비워내는 시간이에요.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = onStartFasting,
                        colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                        shape = RoundedCornerShape(999.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(56.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("공복 시작하기", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 단식 대기 상태(SC-10)일 때만 우측 하단 플로팅 버튼으로 표시
        if (activeSession == null) {
            WaterFabButton(
                currentWaterMl = currentWaterMl,
                dailyGoalMl = dailyWaterGoalMl,
                onAddWater = onAddWater,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 20.dp)
            )
        }

        // 바텀시트 & 다이얼로그 렌더링
        if (showStageSheet && timerState != null) {
            MetabolicStageBottomSheet(
                currentStage = timerState.currentStage,
                elapsedMillis = timerState.elapsedMillis,
                sheetState = stageSheetState,
                onDismissRequest = { showStageSheet = false }
            )
        }

        if (showDefenseDialog && timerState != null) {
            FastingDefenseDialog(
                timerState = timerState,
                onDismiss = { showDefenseDialog = false },
                onKeepGoing1Hour = {
                    // 1시간 더 쉬어가기 (목표 1시간 연장)
                    val newTarget = (activeSession?.targetHours ?: 16f) + 1f
                    onUpdateTargetHours(newTarget)
                },
                onReduceTarget = {
                    // 오늘 목표 줄이기 (경과 시간 기준으로 단축)
                    val elapsedHours = (timerState.elapsedMillis / (3600f * 1000f)).coerceAtLeast(1f)
                    onUpdateTargetHours(elapsedHours)
                },
                onFinishAsPartial = {
                    // 부분 달성으로 마무리
                    val elapsedHours = timerState.elapsedMillis / (3600f * 1000f)
                    onEndFasting(true)
                    onNavigateToComplete(elapsedHours)
                }
            )
        }

        if (showEditDialog && activeSession != null) {
            FastingTimeEditDialog(
                session = activeSession,
                onDismiss = { showEditDialog = false },
                onSave = { newStart, newTargetEnd ->
                    onUpdateSessionTimes(activeSession.id, newStart, newTargetEnd)
                }
            )
        }

        if (showOver72Dialog && activeSession != null) {
            Over72HoursDialog(
                onDismiss = { showOver72Dialog = false },
                onAdjustTime = { showEditDialog = true },
                onFinishNow = {
                    val elapsedHours = (timerState?.elapsedMillis ?: 0L) / (3600f * 1000f)
                    onEndFasting(false)
                    onNavigateToComplete(elapsedHours)
                }
            )
        }
    }
}
