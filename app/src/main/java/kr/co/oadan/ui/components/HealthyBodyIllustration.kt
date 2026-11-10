package kr.co.oadan.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.Sand
import kr.co.oadan.ui.theme.SunsetCoral

/**
 * 단식 대기 화면(SC-10)의 대표 일러스트:
 * 날씬하고 균형 잡힌 건강한 인체 실루엣과 코어 활력(생체 대사 에너지) 오라 그래픽.
 * 공복을 통해 몸을 정화하고 자연스러운 생체 밸런스를 되찾는 오아단의 철학을 시각화합니다.
 */
@Composable
fun HealthyBodyIllustration(
    modifier: Modifier = Modifier,
    canvasSize: Dp = 180.dp,
    isDark: Boolean = MaterialTheme.colorScheme.background != Sand
) {
    // 코어 활력의 부드러운 숨쉬기 펄스 애니메이션 (2.4초 주기)
    val infiniteTransition = rememberInfiniteTransition(label = "vitalityPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .size(canvasSize)
            .semantics { contentDescription = "건강하고 날씬한 인체 실루엣과 활력 에너지 그래픽" },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(canvasSize)) {
            val cx = drawContext.size.width / 2f
            val cy = drawContext.size.height / 2f
            val bgRadius = drawContext.size.width * 0.44f

            // 1. 은은한 웰니스 앰비언트 배경 원 (라이트: 에메랄드 샌드 글로우 / 다크: 밤의 오아시스)
            val bgGradient = if (isDark) {
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1B3835),
                        Color(0xFF122423),
                        Color(0xFF0F1A19)
                    ),
                    center = Offset(cx, cy),
                    radius = bgRadius
                )
            } else {
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE4F3ED),
                        Color(0xFFEEF7F2),
                        Color(0xFFFBF8F2)
                    ),
                    center = Offset(cx, cy),
                    radius = bgRadius
                )
            }

            drawCircle(
                brush = bgGradient,
                radius = bgRadius,
                center = Offset(cx, cy)
            )

            // 2. 부드러운 외곽 오라 링
            val ringColor = if (isDark) Color(0x3348C9AB) else Color(0x402E8B75)
            drawCircle(
                color = ringColor,
                radius = bgRadius,
                center = Offset(cx, cy),
                style = Stroke(width = 1.5.dp.toPx())
            )

            // 3. 건강한 코어(복부/단식 대사) 활력 펄스 오라
            val coreY = cy - 2.dp.toPx()
            val coreRadius = 24.dp.toPx() * pulseScale
            val corePulseBrush = Brush.radialGradient(
                colors = listOf(
                    (if (isDark) Color(0xFF64DFDF) else Color(0xFF48C9AB)).copy(alpha = pulseAlpha),
                    (if (isDark) Color(0xFF48C9AB) else OasisTeal).copy(alpha = pulseAlpha * 0.4f),
                    Color.Transparent
                ),
                center = Offset(cx, coreY),
                radius = coreRadius
            )
            drawCircle(
                brush = corePulseBrush,
                radius = coreRadius,
                center = Offset(cx, coreY)
            )

            // 4. 날씬하고 건강한 인체 실루엣 그리기
            drawHealthyBodySilhouette(
                cx = cx,
                cy = cy,
                scale = drawContext.size.width / 180.dp.toPx(),
                isDark = isDark
            )

            // 5. 복부 코어 활력 포인트 (Vitality Center Dot)
            val centerDotColor = if (isDark) Color(0xFFE0F7F2) else Color.White
            drawCircle(
                color = centerDotColor,
                radius = 2.5.dp.toPx(),
                center = Offset(cx, coreY)
            )
            drawCircle(
                color = if (isDark) Color(0xFF64DFDF) else OasisTeal,
                radius = 4.5.dp.toPx(),
                center = Offset(cx, coreY),
                style = Stroke(width = 1.5.dp.toPx())
            )

            // 6. 상단 웰니스 하모니 장식: 머리 위 잔잔한 생명 잎사귀 및 반짝임
            drawWellnessAccents(
                cx = cx,
                topY = cy - bgRadius + 14.dp.toPx(),
                isDark = isDark
            )
        }
    }
}

/**
 * 정교하고 미려한 날씬하고 균형 잡힌 인체 실루엣 (Slender, Balanced Human Body)
 */
private fun DrawScope.drawHealthyBodySilhouette(
    cx: Float,
    cy: Float,
    scale: Float,
    isDark: Boolean
) {
    val s = scale * 0.92f

    // 실루엣 그라데이션 브러시: 상체에서 하체로 이어지는 활력 에메랄드 톤
    val bodyBrush = if (isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF5CD6BC),
                Color(0xFF38B89C),
                Color(0xFF278A74),
                Color(0xFF1D6656)
            ),
            startY = cy - 65.dp.toPx() * s,
            endY = cy + 65.dp.toPx() * s
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF2E8B75),
                Color(0xFF257563),
                Color(0xFF1F5F50),
                Color(0xFF17473C)
            ),
            startY = cy - 65.dp.toPx() * s,
            endY = cy + 65.dp.toPx() * s
        )
    }

    // 1. 머리 (균형 잡힌 타원)
    val headCenter = Offset(cx, cy - 47.dp.toPx() * s)
    val headRadiusX = 7.5.dp.toPx() * s
    val headRadiusY = 9.5.dp.toPx() * s
    drawOval(
        brush = bodyBrush,
        topLeft = Offset(headCenter.x - headRadiusX, headCenter.y - headRadiusY),
        size = Size(headRadiusX * 2f, headRadiusY * 2f)
    )

    // 2. 몸통 & 다리: 날씬하고 탄탄한 S라인 실루엣 (좌우 대칭 베지어 곡선)
    val bodyPath = Path().apply {
        // 목 상단 중앙에서 시작
        moveTo(cx, cy - 37.dp.toPx() * s)

        // [우측 외곽 라인]
        // 목 우측
        lineTo(cx + 3.2.dp.toPx() * s, cy - 35.dp.toPx() * s)
        // 어깨 (부드럽고 우아한 어깨선)
        cubicTo(
            cx + 8.dp.toPx() * s, cy - 34.dp.toPx() * s,
            cx + 14.dp.toPx() * s, cy - 32.dp.toPx() * s,
            cx + 17.dp.toPx() * s, cy - 27.dp.toPx() * s
        )
        // 가슴 및 상체
        cubicTo(
            cx + 14.5.dp.toPx() * s, cy - 20.dp.toPx() * s,
            cx + 12.5.dp.toPx() * s, cy - 12.dp.toPx() * s,
            cx + 9.2.dp.toPx() * s, cy - 3.dp.toPx() * s // 날씬한 허리 (Slim Waist)
        )
        // 허리 -> 골반 (자연스럽고 탄탄한 힙 라인)
        cubicTo(
            cx + 8.dp.toPx() * s, cy + 3.dp.toPx() * s,
            cx + 13.dp.toPx() * s, cy + 9.dp.toPx() * s,
            cx + 14.5.dp.toPx() * s, cy + 15.dp.toPx() * s // 골반 최고점
        )
        // 골반 -> 허벅지
        cubicTo(
            cx + 13.5.dp.toPx() * s, cy + 24.dp.toPx() * s,
            cx + 11.dp.toPx() * s, cy + 33.dp.toPx() * s,
            cx + 8.2.dp.toPx() * s, cy + 42.dp.toPx() * s // 무릎 외측
        )
        // 무릎 -> 종아리 -> 발목 (슬림하고 긴 다리 라인)
        cubicTo(
            cx + 9.dp.toPx() * s, cy + 49.dp.toPx() * s,
            cx + 7.5.dp.toPx() * s, cy + 56.dp.toPx() * s,
            cx + 4.2.dp.toPx() * s, cy + 62.dp.toPx() * s // 발목
        )
        // 발
        lineTo(cx + 2.5.dp.toPx() * s, cy + 65.dp.toPx() * s)
        // 우측 발 안쪽
        lineTo(cx + 1.2.dp.toPx() * s, cy + 65.dp.toPx() * s)
        // 다리 사이 안쪽 틈 (미세한 공간으로 곧게 뻗은 다리 강조)
        cubicTo(
            cx + 1.5.dp.toPx() * s, cy + 54.dp.toPx() * s,
            cx + 2.dp.toPx() * s, cy + 42.dp.toPx() * s,
            cx + 0.8.dp.toPx() * s, cy + 22.dp.toPx() * s // 사타구니 중앙
        )

        // [좌측 대칭 라인]
        // 좌측 허벅지 안쪽 -> 발목 -> 발
        cubicTo(
            cx - 2.dp.toPx() * s, cy + 42.dp.toPx() * s,
            cx - 1.5.dp.toPx() * s, cy + 54.dp.toPx() * s,
            cx - 1.2.dp.toPx() * s, cy + 65.dp.toPx() * s
        )
        lineTo(cx - 2.5.dp.toPx() * s, cy + 65.dp.toPx() * s)
        // 좌측 발목 -> 종아리 -> 무릎 외측
        cubicTo(
            cx - 4.2.dp.toPx() * s, cy + 62.dp.toPx() * s,
            cx - 7.5.dp.toPx() * s, cy + 56.dp.toPx() * s,
            cx - 9.dp.toPx() * s, cy + 49.dp.toPx() * s
        )
        // 무릎 -> 골반
        cubicTo(
            cx - 8.2.dp.toPx() * s, cy + 42.dp.toPx() * s,
            cx - 11.dp.toPx() * s, cy + 33.dp.toPx() * s,
            cx - 13.5.dp.toPx() * s, cy + 24.dp.toPx() * s
        )
        // 골반 -> 날씬한 허리
        cubicTo(
            cx - 14.5.dp.toPx() * s, cy + 15.dp.toPx() * s,
            cx - 13.dp.toPx() * s, cy + 9.dp.toPx() * s,
            cx - 8.dp.toPx() * s, cy + 3.dp.toPx() * s
        )
        // 허리 -> 가슴 -> 어깨
        cubicTo(
            cx - 9.2.dp.toPx() * s, cy - 3.dp.toPx() * s,
            cx - 12.5.dp.toPx() * s, cy - 12.dp.toPx() * s,
            cx - 14.5.dp.toPx() * s, cy - 20.dp.toPx() * s
        )
        cubicTo(
            cx - 17.dp.toPx() * s, cy - 27.dp.toPx() * s,
            cx - 14.dp.toPx() * s, cy - 32.dp.toPx() * s,
            cx - 8.dp.toPx() * s, cy - 34.dp.toPx() * s
        )
        // 목 좌측 -> 목 중앙
        lineTo(cx - 3.2.dp.toPx() * s, cy - 35.dp.toPx() * s)
        close()
    }

    drawPath(path = bodyPath, brush = bodyBrush)

    // 3. 우아한 양팔 실루엣 (몸통을 따라 자연스럽고 슬림하게 흐르는 팔)
    // 우측 팔
    val rightArmPath = Path().apply {
        moveTo(cx + 17.dp.toPx() * s, cy - 27.dp.toPx() * s)
        cubicTo(
            cx + 20.dp.toPx() * s, cy - 15.dp.toPx() * s,
            cx + 18.5.dp.toPx() * s, cy + 2.dp.toPx() * s,
            cx + 15.dp.toPx() * s, cy + 16.dp.toPx() * s // 손목/손
        )
        lineTo(cx + 13.5.dp.toPx() * s, cy + 15.dp.toPx() * s)
        cubicTo(
            cx + 16.dp.toPx() * s, cy + 2.dp.toPx() * s,
            cx + 17.5.dp.toPx() * s, cy - 14.dp.toPx() * s,
            cx + 14.8.dp.toPx() * s, cy - 25.dp.toPx() * s
        )
        close()
    }
    drawPath(path = rightArmPath, brush = bodyBrush)

    // 좌측 팔
    val leftArmPath = Path().apply {
        moveTo(cx - 17.dp.toPx() * s, cy - 27.dp.toPx() * s)
        cubicTo(
            cx - 20.dp.toPx() * s, cy - 15.dp.toPx() * s,
            cx - 18.5.dp.toPx() * s, cy + 2.dp.toPx() * s,
            cx - 15.dp.toPx() * s, cy + 16.dp.toPx() * s
        )
        lineTo(cx - 13.5.dp.toPx() * s, cy + 15.dp.toPx() * s)
        cubicTo(
            cx - 16.dp.toPx() * s, cy + 2.dp.toPx() * s,
            cx - 17.5.dp.toPx() * s, cy - 14.dp.toPx() * s,
            cx - 14.8.dp.toPx() * s, cy - 25.dp.toPx() * s
        )
        close()
    }
    drawPath(path = leftArmPath, brush = bodyBrush)
}

/**
 * 상단 웰니스 하모니 장식: 생명력의 작은 잎사귀 쌍과 청아한 반짝임
 */
private fun DrawScope.drawWellnessAccents(
    cx: Float,
    topY: Float,
    isDark: Boolean
) {
    val leafColor = if (isDark) Color(0xFF64DFDF) else OasisTeal

    // 작은 잎사귀 1 (우측 대각선)
    val leaf1 = Path().apply {
        moveTo(cx, topY)
        quadraticTo(cx + 6.dp.toPx(), topY - 3.dp.toPx(), cx + 7.dp.toPx(), topY - 9.dp.toPx())
        quadraticTo(cx + 1.dp.toPx(), topY - 8.dp.toPx(), cx, topY)
    }
    drawPath(leaf1, color = leafColor)

    // 작은 잎사귀 2 (좌측 대각선)
    val leaf2 = Path().apply {
        moveTo(cx, topY)
        quadraticTo(cx - 6.dp.toPx(), topY - 2.dp.toPx(), cx - 7.dp.toPx(), topY - 8.dp.toPx())
        quadraticTo(cx - 1.dp.toPx(), topY - 7.dp.toPx(), cx, topY)
    }
    drawPath(leaf2, color = leafColor.copy(alpha = 0.8f))

    // 은은한 반짝임 별빛 (우측 상단)
    val starX = cx + 32.dp.toPx()
    val starY = topY + 8.dp.toPx()
    val starColor = if (isDark) Color(0xFFFFD166) else SunsetCoral
    drawCircle(color = starColor, radius = 2.dp.toPx(), center = Offset(starX, starY))
    drawLine(
        color = starColor,
        start = Offset(starX - 4.dp.toPx(), starY),
        end = Offset(starX + 4.dp.toPx(), starY),
        strokeWidth = 1.dp.toPx()
    )
    drawLine(
        color = starColor,
        start = Offset(starX, starY - 4.dp.toPx()),
        end = Offset(starX, starY + 4.dp.toPx()),
        strokeWidth = 1.dp.toPx()
    )
}
