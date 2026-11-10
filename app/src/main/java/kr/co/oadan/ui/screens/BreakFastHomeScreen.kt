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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.data.model.Recipe
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.Dune
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Pebble
import kr.co.oadan.ui.theme.Sand
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
    var showShoppingDialog by remember { mutableStateOf(false) }

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
            // App Bar (SC-20)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "오늘의 오아시스 한 끼",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 장보기 바구니 아이콘 (OA-04)
                    IconButton(
                        onClick = { showShoppingDialog = true },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBasket,
                            contentDescription = "장보기 리스트",
                            tint = OasisTeal
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Button(
                        onClick = onNavigateToRecord,
                        colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                        shape = RoundedCornerShape(999.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("보식 기록", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${lastFastedHours.toInt()}시간 공복 후엔 ${recommendedStage}을 추천해요",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 필터 칩 목록
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                filters.forEach { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) OasisTeal else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = filter,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
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
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFF55703F), modifier = Modifier.size(14.dp))
                        Text(text = " 🧈 단백질", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF55703F))
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFF55703F), modifier = Modifier.size(14.dp))
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
                        .background(MaterialTheme.colorScheme.surface)
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
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = featuredRecipe.subtitle,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${featuredRecipe.prepTimeMinutes}분",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "· ${featuredRecipe.tags.take(2).joinToString(" · ")}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                .background(MaterialTheme.colorScheme.surface)
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
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${recipe.prepTimeMinutes}분 · 혈당 ${recipe.glycemicFriendly}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // OA-04 오가닉 장보기 리스트 다이얼로그
        if (showShoppingDialog) {
            ShoppingListDialog(
                recipes = recipes,
                onDismiss = { showShoppingDialog = false }
            )
        }
    }
}

@Composable
fun ShoppingListDialog(
    recipes: List<Recipe>,
    onDismiss: () -> Unit
) {
    // 추천 및 보식 레시피에서 주요 재료 목록 추출
    val defaultIngredients = remember(recipes) {
        val list = mutableListOf<String>()
        recipes.forEach { recipe ->
            recipe.ingredients.forEach { item ->
                val cleaned = item.trim().replace("-", "").trim()
                if (cleaned.isNotBlank() && !list.contains(cleaned)) {
                    list.add(cleaned)
                }
            }
        }
        if (list.isEmpty()) {
            listOf("유기농 두부 1모", "제철 애호박 1개", "전통 된장 1큰술", "미니 단호박 1/2개", "유기농 현미", "자른 미역 10g", "양배추 1/4통")
        } else {
            list.take(12)
        }
    }

    val checkedItems = remember { mutableStateListOf<String>() }
    val customItems = remember { mutableStateListOf<String>() }
    var newIngredientText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ShoppingBasket, contentDescription = null, tint = OasisTeal)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("오가닉 장보기 리스트", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "닫기", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "추천 보식 레시피의 자연 식재료를 장보기 전에 확인하고 체크하세요 🥬",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                val allItems = defaultIngredients + customItems
                allItems.forEach { item ->
                    val isChecked = checkedItems.contains(item)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isChecked) checkedItems.remove(item)
                                else checkedItems.add(item)
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                if (checked) checkedItems.add(item)
                                else checkedItems.remove(item)
                            },
                            colors = CheckboxDefaults.colors(checkedColor = OasisTeal)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = item,
                            fontSize = 14.sp,
                            color = if (isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 식재료 직접 추가
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newIngredientText,
                        onValueChange = { newIngredientText = it },
                        placeholder = { Text("추가할 식재료 (예: 유기농 사과)", fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = {
                            if (newIngredientText.isNotBlank()) {
                                customItems.add(newIngredientText.trim())
                                newIngredientText = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("추가", fontSize = 12.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                shape = RoundedCornerShape(999.dp)
            ) {
                Text("장보기 완료", fontWeight = FontWeight.Bold)
            }
        }
    )
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
