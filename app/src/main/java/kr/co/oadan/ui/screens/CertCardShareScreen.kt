package kr.co.oadan.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import kr.co.oadan.domain.CardAspectRatio
import kr.co.oadan.domain.CardTemplate
import kr.co.oadan.domain.CertCardData
import kr.co.oadan.domain.CertificationCardGenerator
import kr.co.oadan.domain.MetabolicStage
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Pebble
import kr.co.oadan.ui.theme.Sand
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.Teal700

@Composable
fun CertCardShareScreen(
    fastedHours: Float = 16.2f,
    organicGrade: String = "오아시스",
    streakDays: Int = 7,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var selectedTemplate by remember { mutableStateOf(CardTemplate.PHOTO) }
    var selectedRatio by remember { mutableStateOf(CardAspectRatio.SQUARE_1_1) }
    var showOrganicScore by remember { mutableStateOf(true) }
    var showWeightChange by remember { mutableStateOf(false) }

    val hoursInt = fastedHours.toInt()
    val minutesInt = ((fastedHours - hoursInt) * 60).toInt()
    val fastedTimeStr = "${hoursInt}h ${minutesInt}m"
    val stage = MetabolicStage.fromElapsedHours(fastedHours)

    val certCardData = CertCardData(
        fastedTimeString = fastedTimeStr,
        stage = stage,
        organicGrade = organicGrade,
        streakDays = streakDays,
        showOrganicScore = showOrganicScore,
        showWeightChange = showWeightChange,
        weightChangeText = "-0.4kg"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2A3533)) // 다크 편집 배경
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로", tint = Color.White)
                }
                Text(
                    text = "인증 카드",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                IconButton(onClick = {
                    val bitmap = CertificationCardGenerator.generateCardBitmap(
                        context = context,
                        data = certCardData,
                        template = selectedTemplate,
                        aspectRatio = selectedRatio
                    )
                    CertificationCardGenerator.saveBitmapToCache(context, bitmap)
                    Toast.makeText(context, "인증 카드가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = "저장", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 카드 미리보기 (Square 1:1 or Story 9:16)
            Box(
                modifier = Modifier
                    .fillMaxWidth(if (selectedRatio == CardAspectRatio.SQUARE_1_1) 0.88f else 0.72f)
                    .aspectRatio(if (selectedRatio == CardAspectRatio.SQUARE_1_1) 1f else 9f / 16f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Sand)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // 상단 사진 / 일러스트 영역 (60%)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.6f)
                            .background(
                                when (selectedTemplate) {
                                    CardTemplate.PHOTO -> Color(0xFFB9C7A4)
                                    CardTemplate.MINIMAL -> Sand
                                    CardTemplate.OASIS -> OasisTeal
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (selectedTemplate == CardTemplate.OASIS) "🌴" else "🥗",
                            fontSize = 64.sp
                        )

                        Text(
                            text = "OADAN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = if (selectedTemplate == CardTemplate.OASIS) Color.White else Color(0xCCFFFFFF),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(14.dp)
                        )
                    }

                    // 하단 정보 영역 (40%)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.4f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = fastedTimeStr,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Teal700
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "오늘도 나만의 오아시스에서",
                                    fontSize = 11.sp,
                                    color = Stone,
                                    fontFamily = FontFamily.Serif
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${stage.emoji} ${stage.stageName}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepInk
                                )
                                if (showOrganicScore) {
                                    Text(
                                        text = "🌳 $organicGrade",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepInk
                                    )
                                }
                                Text(
                                    text = "🔥 ${streakDays}일째",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepInk
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = certCardData.dateString,
                                fontSize = 10.sp,
                                color = Stone
                            )
                            Text(
                                text = "#오아단인증",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Teal700
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 템플릿 & 비율 선택 칩
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TemplateChip("사진형", selectedTemplate == CardTemplate.PHOTO) { selectedTemplate = CardTemplate.PHOTO }
                TemplateChip("미니멀", selectedTemplate == CardTemplate.MINIMAL) { selectedTemplate = CardTemplate.MINIMAL }
                TemplateChip("오아시스", selectedTemplate == CardTemplate.OASIS) { selectedTemplate = CardTemplate.OASIS }
                TemplateChip(
                    if (selectedRatio == CardAspectRatio.SQUARE_1_1) "1:1" else "9:16",
                    isSelected = true
                ) {
                    selectedRatio = if (selectedRatio == CardAspectRatio.SQUARE_1_1) {
                        CardAspectRatio.STORY_9_16
                    } else {
                        CardAspectRatio.SQUARE_1_1
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 토글 설정
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x1AFFFFFF))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "오가닉 스코어 표시", color = Color.White, fontSize = 14.sp)
                    Switch(
                        checked = showOrganicScore,
                        onCheckedChange = { showOrganicScore = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = OasisTeal)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "체중 변화 표시", color = Color.White, fontSize = 14.sp)
                    Switch(
                        checked = showWeightChange,
                        onCheckedChange = { showWeightChange = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = OasisTeal)
                    )
                }
            }
        }

        // 하단 공유 CTA (해시태그 자동 복사 및 Sharesheet 호출)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xFF2A3533))
                .navigationBarsPadding()
                .padding(20.dp)
        ) {
            Button(
                onClick = {
                    // 1. 해시태그 클립보드 자동 복사 (OA-03)
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("hashtag", "#오아단 #오아단인증 #간헐적단식 #오가닉보식")
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "#오아단인증 해시태그가 복사되었습니다.", Toast.LENGTH_SHORT).show()

                    // 2. 비트맵 렌더링 및 파일 생성
                    val bitmap = CertificationCardGenerator.generateCardBitmap(
                        context = context,
                        data = certCardData,
                        template = selectedTemplate,
                        aspectRatio = selectedRatio
                    )
                    val file = CertificationCardGenerator.saveBitmapToCache(context, bitmap)
                    val uri = FileProvider.getUriForFile(
                        context,
                        "kr.co.oadan.fileprovider",
                        file
                    )

                    // 3. Android Sharesheet 호출
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "image/png"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        putExtra(Intent.EXTRA_TEXT, "#오아단 #오아단인증 #간헐적단식")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "인증 카드 공유하기"))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF72C1B2),
                    contentColor = Color(0xFF063530)
                ),
                shape = RoundedCornerShape(999.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(imageVector = Icons.Default.IosShare, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "공유하기 · #오아단인증 복사됨",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TemplateChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color.White else Color(0x26FFFFFF))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) DeepInk else Color.White
        )
    }
}
