package kr.co.oadan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RiceBowl
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.RiceBowl
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.ui.theme.Pebble
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.SunsetCoral
import kr.co.oadan.ui.theme.Teal50
import kr.co.oadan.ui.theme.Teal700

enum class OadanTab(
    val title: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
) {
    HOME("홈", Icons.Filled.WaterDrop, Icons.Outlined.WaterDrop),
    BREAK_FAST("보식", Icons.Filled.RiceBowl, Icons.Outlined.RiceBowl),
    RECORD("기록", Icons.Filled.EditNote, Icons.Outlined.EditNote),
    REPORT("리포트", Icons.Filled.BarChart, Icons.Outlined.BarChart),
    MY("마이", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun OadanBottomNavigation(
    selectedTab: OadanTab,
    onTabSelected: (OadanTab) -> Unit,
    showBreakFastBadge: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Divider(color = Pebble.copy(alpha = 0.6f), thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OadanTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                val icon = if (isSelected) tab.filledIcon else tab.outlinedIcon

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 64.dp, height = 32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) Teal50 else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = tab.title,
                            tint = if (isSelected) Teal700 else Stone,
                            modifier = Modifier.size(24.dp)
                        )

                        // 보식 탭 강조 배지 점
                        if (tab == OadanTab.BREAK_FAST && showBreakFastBadge) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .offset(x = 10.dp, y = (-8).dp)
                                    .clip(CircleShape)
                                    .background(SunsetCoral)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tab.title,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Teal700 else Stone
                    )
                }
            }
        }
    }
}
