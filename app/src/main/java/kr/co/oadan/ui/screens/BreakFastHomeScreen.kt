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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.data.model.Recipe
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.Dune
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Pebble
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.Teal50
import kr.co.oadan.ui.theme.Teal700

@Composable
fun BreakFastHomeScreen(
    recipes: List<Recipe>,
    recommendedStage: String,
    lastFastedHours: Float,
    onSelectRecipe: (Recipe) -> Unit,
    onToggleBookmark: (String) -> Unit,
    onNavigateToRecord: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("전체") }
    val filters = listOf("전체", "🌱 비건", "저탄수", "글루텐프리")

    val filteredRecipes = recipes.filter { recipe ->
        when (selectedFilter) {
            "🌱 비건" -> recipe.tags.contains("비건")
            "저탄수" -> recipe.tags.contains("저탄수")
            "글루텐프리" -> recipe.tags.contains("글루텐프리")
            else -> true
        }
    }

    val featuredRecipe = filteredRecipes.firstOrNull()
    val subRecipes = if (filteredRecipes.size > 1) filteredRecipes.drop(1) else emptyList()

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
                .padding(top = 16.dp, bottom = 96.dp)
        ) {
            // App Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "오늘의 오아시스 한 끼",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepInk
                )

                Button(
                    onClick = onNavigateToRecord,
                    colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                    shape = RoundedCornerShape(999.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("보식 기록", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${lastFastedHours.toInt()}시간 공복 후엔 ${recommendedStage}을 추천해요",
                fontSize = 14.sp,
                color = Stone
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 필터 칩 목록
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                filters.forEach { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) OasisTeal else Dune)
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = filter,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) Color.White else DeepInk
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 먹는 순서 가이드 카드
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF3F6EE))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "먹는 순서",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF55703F)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🥬 채소", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF55703F))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF55703F), modifier = Modifier.size(14.dp))
                        Text(text = " 🧈 단백질", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF55703F))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF55703F), modifier = Modifier.size(14.dp))
                        Text(text = " 🍠 탄수화물", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF55703F))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 메인 추천 1 카드
            if (featuredRecipe != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .clickable { onSelectRecipe(featuredRecipe) }
                ) {
                    Column {
                        // 일러스트 / 이모지 헤더
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .background(Color(0xFFB9C7A4)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = featuredRecipe.emoji, fontSize = 72.sp)

                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(12.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White.copy(alpha = 0.92f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "추천 1",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Teal700
                                )
                            }

                            IconButton(
                                onClick = { onToggleBookmark(featuredRecipe.id) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(6.dp)
                            ) {
                                Icon(
                                    imageVector = if (featuredRecipe.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "저장",
                                    tint = Color.White
                                )
                            }
                        }

                        // 상세 텍스트
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = featuredRecipe.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepInk
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = featuredRecipe.subtitle,
                                fontSize = 13.sp,
                                color = Stone
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                GlycemicChip(friendly = featuredRecipe.glycemicFriendly)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = Stone,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${featuredRecipe.prepTimeMinutes}분",
                                        fontSize = 12.sp,
                                        color = Stone
                                    )
                                }
                                Text(
                                    text = "· ${featuredRecipe.tags.take(2).joinToString(" · ")}",
                                    fontSize = 12.sp,
                                    color = Stone
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 서브 레시피 2열 그리드
            if (subRecipes.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    subRecipes.take(2).forEach { recipe ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .clickable { onSelectRecipe(recipe) }
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(90.dp)
                                        .background(Color(0xFFEBC9A9)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = recipe.emoji, fontSize = 42.sp)
                                }
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = recipe.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepInk,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${recipe.prepTimeMinutes}분 · 혈당 ${recipe.glycemicFriendly}",
                                        fontSize = 11.sp,
                                        color = Stone
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GlycemicChip(friendly: String) {
    val (bgColor, textColor) = when (friendly) {
        "낮음" -> Pair(Color(0xFFE5F2E9), Color(0xFF2C6B42))
        "높음" -> Pair(Color(0xFFFBF0DC), Color(0xFF8A5D0E))
        else -> Pair(Color(0xFFEDF1E7), Color(0xFF55703F))
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = "혈당 친화 $friendly",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
