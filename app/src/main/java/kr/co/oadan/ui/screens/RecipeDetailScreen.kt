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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
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
import kr.co.oadan.ui.theme.Sand
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.Teal50
import kr.co.oadan.ui.theme.Teal700

@Composable
fun RecipeDetailScreen(
    recipe: Recipe,
    onBack: () -> Unit,
    onToggleBookmark: (String) -> Unit,
    onRecordWithThisMenu: (menuName: String) -> Unit
) {
    // 장보기 체크리스트 (OA-04)
    val checkedIngredients = remember { mutableStateMapOf<String, Boolean>() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Sand)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp)
        ) {
            // 헤더 이미지 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Color(0xFFB9C7A4)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = recipe.emoji, fontSize = 96.sp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.85f))
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }

                    IconButton(
                        onClick = { onToggleBookmark(recipe.id) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.85f))
                    ) {
                        Icon(
                            imageVector = if (recipe.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "북마크",
                            tint = if (recipe.isBookmarked) OasisTeal else Stone
                        )
                    }
                }
            }

            // 본문 상세
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GlycemicChip(friendly = recipe.glycemicFriendly)
                    Text(
                        text = "${recipe.stage} · ${recipe.prepTimeMinutes}분",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Stone
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = recipe.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepInk
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recipe.subtitle,
                    fontSize = 14.sp,
                    color = Stone
                )

                if (recipe.seasonInfo != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Teal50)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "💡 ${recipe.seasonInfo}",
                            fontSize = 12.sp,
                            color = Teal700
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 먹는 순서 가이드
                Text(
                    text = "먹는 순서 가이드",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepInk
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF3F6EE))
                        .padding(14.dp)
                ) {
                    Text(
                        text = recipe.eatOrderGuide,
                        fontSize = 13.sp,
                        color = Color(0xFF55703F),
                        lineHeight = 19.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 재료 목록 (오가닉 장보기 체크리스트 OA-04)
                Text(
                    text = "재료 준비 (장보기 체크리스트)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepInk
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    recipe.ingredients.forEach { item ->
                        val isChecked = checkedIngredients[item] == true
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { checkedIngredients[item] = !isChecked }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isChecked) OasisTeal else Color.White)
                                    .border(2.dp, if (isChecked) OasisTeal else Pebble, RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isChecked) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = item,
                                fontSize = 14.sp,
                                color = if (isChecked) Stone else DeepInk
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 조리 순서
                Text(
                    text = "부드러운 조리 순서",
                    fontSize = 16.sp,
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
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    recipe.instructions.forEachIndexed { idx, step ->
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Teal50),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${idx + 1}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Teal700
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = step,
                                fontSize = 14.sp,
                                color = DeepInk,
                                lineHeight = 20.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // 하단 CTA
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Sand)
                .padding(20.dp)
        ) {
            Button(
                onClick = { onRecordWithThisMenu(recipe.title) },
                colors = ButtonDefaults.buttonColors(containerColor = OasisTeal),
                shape = RoundedCornerShape(999.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "이 메뉴로 보식 기록하기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
