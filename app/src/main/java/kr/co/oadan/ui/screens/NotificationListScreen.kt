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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.data.model.NotificationItem
import kr.co.oadan.data.model.NotificationType
import kr.co.oadan.data.repository.UserPreferences
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.Dune
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.OrganicSage
import kr.co.oadan.ui.theme.Pebble
import kr.co.oadan.ui.theme.SpringWater
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.SunsetCoral
import kr.co.oadan.ui.theme.Teal50
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(
    notifications: List<NotificationItem>,
    preferences: UserPreferences,
    onBack: () -> Unit,
    onMarkAsRead: (String) -> Unit,
    onMarkAllAsRead: () -> Unit,
    onDeleteNotification: (String) -> Unit,
    onClearAll: () -> Unit,
    onNavigateToTab: (String) -> Unit,
    onUpdateNotifications: (start: Boolean, end: Boolean, stage: Boolean) -> Unit,
    onUpdateCheerNotification: (Boolean) -> Unit
) {
    var showClearAllConfirm by remember { mutableStateOf(false) }
    var showSettingDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "알림",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    if (notifications.isNotEmpty()) {
                        IconButton(onClick = onMarkAllAsRead) {
                            Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = "모두 읽음",
                                tint = OasisTeal
                            )
                        }
                        IconButton(onClick = { showClearAllConfirm = true }) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "전체 삭제",
                                tint = Stone
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
        ) {
            // 상단: 푸시 알림 설정 바로가기 카드
            Surface(
                onClick = { showSettingDialog = true },
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, Pebble.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Teal50),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = OasisTeal,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "알림 설정",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "공복 시작·종료, 대사 단계, 격려 알림 설정",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Text(
                        text = "설정 >",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OasisTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 알림 목록 or 빈 화면
            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = Stone,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "도착한 알림이 없어요",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "공복을 시작하거나 수분을 기록하면\n새로운 알림이 이곳에 차분히 도착해요 🌿",
                            fontSize = 13.sp,
                            color = Stone,
                            lineHeight = 18.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(notifications, key = { it.id }) { item ->
                        NotificationCard(
                            item = item,
                            onClick = {
                                onMarkAsRead(item.id)
                                item.routeTab?.let { tab ->
                                    onNavigateToTab(tab)
                                }
                            },
                            onDelete = {
                                onDeleteNotification(item.id)
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    // 전체 삭제 확인 팝업
    if (showClearAllConfirm) {
        AlertDialog(
            onDismissRequest = { showClearAllConfirm = false },
            title = {
                Text(text = "알림 전체 삭제", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = "모든 알림 내역을 삭제하시겠어요? 삭제 후에는 복구할 수 없습니다.", fontSize = 13.sp, color = Stone)
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAll()
                        showClearAllConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SunsetCoral)
                ) {
                    Text("삭제")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearAllConfirm = false }) {
                    Text("취소", color = Stone)
                }
            }
        )
    }

    // 알림 설정 다이얼로그
    if (showSettingDialog) {
        NotificationSettingDialog(
            preferences = preferences,
            onDismiss = { showSettingDialog = false },
            onUpdateNotifications = onUpdateNotifications,
            onUpdateCheerNotification = onUpdateCheerNotification
        )
    }
}

@Composable
private fun NotificationCard(
    item: NotificationItem,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val iconInfo = when (item.type) {
        NotificationType.WELCOME -> "🌿" to Teal50
        NotificationType.FASTING_START -> "🌿" to Teal50
        NotificationType.STAGE_CHANGE -> "🌴" to OrganicSage.copy(alpha = 0.15f)
        NotificationType.GOAL_REACHED -> "🎉" to SunsetCoral.copy(alpha = 0.15f)
        NotificationType.CHEER -> "💌" to SpringWater.copy(alpha = 0.2f)
        NotificationType.WATER -> "💧" to Teal50
        NotificationType.SYSTEM -> "🔔" to Dune
    }

    val timeText = formatRelativeTime(item.timestamp)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = if (item.isRead) MaterialTheme.colorScheme.surface.copy(alpha = 0.7f) else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (item.isRead) Pebble.copy(alpha = 0.4f) else OasisTeal.copy(alpha = 0.4f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            // 좌측 아이콘
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconInfo.second),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconInfo.first, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 내용 영역
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!item.isRead) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(OasisTeal)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = item.title,
                            fontSize = 13.sp,
                            fontWeight = if (item.isRead) FontWeight.SemiBold else FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text(
                        text = timeText,
                        fontSize = 10.sp,
                        color = Stone
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.message,
                    fontSize = 12.sp,
                    color = if (item.isRead) Stone else MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // 개별 삭제 버튼
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "삭제",
                    tint = Stone.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun NotificationSettingDialog(
    preferences: UserPreferences,
    onDismiss: () -> Unit,
    onUpdateNotifications: (start: Boolean, end: Boolean, stage: Boolean) -> Unit,
    onUpdateCheerNotification: (Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "알림 수신 설정", fontWeight = FontWeight.Bold, fontSize = 17.sp)
        },
        text = {
            Column {
                Text(
                    text = "원하는 알림을 켜고 끌 수 있어요.",
                    fontSize = 12.sp,
                    color = Stone
                )
                Spacer(modifier = Modifier.height(14.dp))

                SettingSwitchRow(
                    title = "공복 시작 알림",
                    subtitle = "매일 설정된 시작 시간에 알림",
                    checked = preferences.notificationFastingStart,
                    onCheckedChange = {
                        onUpdateNotifications(it, preferences.notificationFastingEnd, preferences.notificationStageChange)
                    }
                )

                SettingSwitchRow(
                    title = "목표 달성 및 종료 알림",
                    subtitle = "단식 목표 시간 도달 시 즉시 알림",
                    checked = preferences.notificationFastingEnd,
                    onCheckedChange = {
                        onUpdateNotifications(preferences.notificationFastingStart, it, preferences.notificationStageChange)
                    }
                )

                SettingSwitchRow(
                    title = "대사 단계 진입 알림",
                    subtitle = "케토시스, 오토파지 등 대사 단계 진입 시 알림",
                    checked = preferences.notificationStageChange,
                    onCheckedChange = {
                        onUpdateNotifications(preferences.notificationFastingStart, preferences.notificationFastingEnd, it)
                    }
                )

                SettingSwitchRow(
                    title = "공복 중간 격려 및 축하 알림",
                    subtitle = "공복 50% 진행 및 초과 달성 시 응원 메시지",
                    checked = preferences.notificationCheerMessage,
                    onCheckedChange = {
                        onUpdateCheerNotification(it)
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = OasisTeal)
            ) {
                Text("완료")
            }
        }
    )
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Stone
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = OasisTeal
            )
        )
    }
}

private fun formatRelativeTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val minute = 60 * 1000L
    val hour = 60 * minute
    val day = 24 * hour

    return when {
        diff < minute -> "방금 전"
        diff < hour -> "${diff / minute}분 전"
        diff < day -> "${diff / hour}시간 전"
        diff < 2 * day -> "어제"
        else -> SimpleDateFormat("M월 d일", Locale.KOREA).format(Date(timestamp))
    }
}
