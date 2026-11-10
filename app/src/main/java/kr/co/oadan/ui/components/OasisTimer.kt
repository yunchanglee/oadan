package kr.co.oadan.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.co.oadan.domain.FastingTimerState
import kr.co.oadan.domain.MetabolicStage
import kr.co.oadan.ui.theme.DeepInk
import kr.co.oadan.ui.theme.Dune
import kr.co.oadan.ui.theme.OasisTeal
import kr.co.oadan.ui.theme.StageDesert
import kr.co.oadan.ui.theme.StageLagoon
import kr.co.oadan.ui.theme.StagePalm
import kr.co.oadan.ui.theme.StageSpring
import kr.co.oadan.ui.theme.StageWind
import kr.co.oadan.ui.theme.Stone
import kr.co.oadan.ui.theme.SunsetCoral
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun OasisTimer(
    timerState: FastingTimerState,
    modifier: Modifier = Modifier,
    isDark: Boolean = isSystemInDarkTheme()
) {
    val animatedProgress by animateFloatAsState(
        targetValue = timerState.progressRatio.coerceAtMost(1.0f),
        animationSpec = tween(durationMillis = 600),
        label = "progressAnim"
    )

    val elapsedMin = (timerState.elapsedMillis / 60000L).toInt()
    val elapsedHoursPart = elapsedMin / 60
    val elapsedMinsPart = elapsedMin % 60
    val a11yDescription = "단식 ${elapsedHoursPart}시간 ${elapsedMinsPart}분 경과, 목표까지 ${timerState.formattedRemaining}, ${timerState.currentStage.stageName} 단계"

    Box(
        modifier = modifier
            .size(280.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = a11yDescription
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(280.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val strokeWidth = 14.dp.toPx()
            val ringRadius = (size.width - strokeWidth) / 2f
            val sceneRadius = ringRadius - strokeWidth / 2f - 4.dp.toPx()

            // 1. 내부 오아시스 일러스트 그리기
            drawOasisScene(
                center = center,
                radius = sceneRadius,
                progress = animatedProgress,
                isDark = isDark,
                currentStage = timerState.currentStage
            )

            // 2. 타이머 링 트랙 (Background Ring)
            val trackColor = if (isDark) Color(0xFF22322F) else Dune
            drawCircle(
                color = trackColor,
                radius = ringRadius,
                center = center,
                style = Stroke(width = strokeWidth)
            )

            // 3. 진행도 아크 그리기 (대사 단계 스윕 컬러)
            val sweepAngle = (animatedProgress * 360f).coerceIn(0f, 360f)
            if (sweepAngle > 0f) {
                val stageBrush = Brush.sweepGradient(
                    colors = listOf(
                        StageDesert,
                        StageWind,
                        StageSpring,
                        StagePalm,
                        StageLagoon,
                        StageLagoon
                    ),
                    center = center
                )

                // 12시 방향(-90도)부터 시작
                drawArc(
                    brush = stageBrush,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - ringRadius, center.y - ringRadius),
                    size = Size(ringRadius * 2f, ringRadius * 2f),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // 진행 끝점 캡 도트 (Cap Dot)
                val angleRad = (sweepAngle - 90f) * (PI.toFloat() / 180f)
                val dotX = center.x + ringRadius * cos(angleRad)
                val dotY = center.y + ringRadius * sin(angleRad)

                // 도트 외곽 (흰색 그림자)
                drawCircle(
                    color = Color.White,
                    radius = 9.dp.toPx(),
                    center = Offset(dotX, dotY)
                )
                // 도트 내부 (현재 단계 색상 또는 Oasis Teal)
                drawCircle(
                    color = timerState.currentStage.color,
                    radius = 5.dp.toPx(),
                    center = Offset(dotX, dotY)
                )
            }

            // 4. 대사 단계 마커 (4, 8, 12, 16시간 지점 = 0.25, 0.50, 0.75, 1.0 지점)
            val markerAngles = listOf(0f, 90f, 180f, 270f)
            markerAngles.forEachIndexed { index, angleDeg ->
                val rad = (angleDeg - 90f) * (PI.toFloat() / 180f)
                val mx = center.x + ringRadius * cos(rad)
                val my = center.y + ringRadius * sin(rad)
                val markerColor = when (index) {
                    0 -> StageDesert
                    1 -> StageWind
                    2 -> StageSpring
                    else -> StagePalm
                }
                drawCircle(
                    color = Color.White,
                    radius = 5.dp.toPx(),
                    center = Offset(mx, my)
                )
                drawCircle(
                    color = markerColor,
                    radius = 3.dp.toPx(),
                    center = Offset(mx, my)
                )
            }

            // 5. 초과 달성 보너스 링 (100% 초과 시 외곽에 Coral 링)
            if (timerState.isGoalReached && timerState.progressRatio > 1.0f) {
                val bonusRadius = ringRadius + strokeWidth / 2f + 4.dp.toPx()
                val bonusSweep = ((timerState.progressRatio - 1.0f) * 360f).coerceAtMost(360f)
                drawArc(
                    color = SunsetCoral,
                    startAngle = -90f,
                    sweepAngle = bonusSweep,
                    useCenter = false,
                    topLeft = Offset(center.x - bonusRadius, center.y - bonusRadius),
                    size = Size(bonusRadius * 2f, bonusRadius * 2f),
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }

        // 중앙 읽기 텍스트
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "경과 시간",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDark) Color(0xFFA3B1AD) else Stone
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = timerState.formattedElapsed,
                fontSize = 44.sp,
                fontWeight = FontWeight.Light,
                color = if (isDark) Color(0xFFE6EFEC) else DeepInk,
                letterSpacing = 0.sp,
                style = TextStyle(fontFeatureSettings = "tnum")
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (timerState.remainingMillis > 0) {
                Text(
                    text = "목표까지 " + timerState.formattedRemaining,
                    fontSize = 13.sp,
                    color = if (isDark) Color(0xFFA3B1AD) else Stone
                )
            } else {
                Text(
                    text = "🎉 목표 달성! 초과 달성 중",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SunsetCoral
                )
            }
        }
    }
}

/**
 * 진행률에 따라 모래에서 물이 차오르고 야자수와 해가 깃든 오아시스 일러스트 그리기
 */
private fun DrawScope.drawOasisScene(
    center: Offset,
    radius: Float,
    progress: Float,
    isDark: Boolean,
    currentStage: MetabolicStage
) {
    // 원형 클립 마스크
    val clipPath = Path().apply {
        addOval(androidx.compose.ui.geometry.Rect(center.x - radius, center.y - radius, center.x + radius, center.y + radius))
    }

    clipPath(clipPath) {
        val w = radius * 2f
        val left = center.x - radius
        val top = center.y - radius

        // 1. 하늘 배경
        val skyBrush = if (isDark) {
            Brush.verticalGradient(
                colors = listOf(Color(0xFF132A33), Color(0xFF1D3B40)),
                startY = top,
                endY = top + w
            )
        } else {
            Brush.verticalGradient(
                colors = listOf(Color(0xFFFFFBF4), Color(0xFFF6EBDA)),
                startY = top,
                endY = top + w
            )
        }
        drawRect(brush = skyBrush, topLeft = Offset(left, top), size = Size(w, w))

        // 2. 해 또는 달
        if (isDark) {
            // 달
            val moonCenter = Offset(center.x + radius * 0.45f, center.y - radius * 0.5f)
            drawCircle(color = Color(0xFFF2E3C2), radius = 12.dp.toPx(), center = moonCenter)
            drawCircle(color = Color(0xFF1D3B40), radius = 10.dp.toPx(), center = Offset(moonCenter.x + 4.dp.toPx(), moonCenter.y - 2.dp.toPx()))

            // 작은 별들
            drawCircle(color = Color(0xFFE6EFEC), radius = 1.5f, center = Offset(center.x - radius * 0.5f, center.y - radius * 0.4f))
            drawCircle(color = Color(0xFFE6EFEC), radius = 1.2f, center = Offset(center.x - radius * 0.2f, center.y - radius * 0.6f))
            drawCircle(color = Color(0xFFE6EFEC), radius = 1.5f, center = Offset(center.x + radius * 0.1f, center.y - radius * 0.35f))
        } else {
            // 태양
            val sunCenter = Offset(center.x + radius * 0.45f, center.y - radius * 0.45f)
            drawCircle(color = Color(0x33F8CFA0), radius = 24.dp.toPx(), center = sunCenter)
            drawCircle(color = Color(0xAAF8CFA0), radius = 14.dp.toPx(), center = sunCenter)
        }

        // 3. 모래 언덕 (Dune 1 & Dune 2)
        val dune2Path = Path().apply {
            val duneTopY = center.y + radius * 0.3f
            moveTo(left, duneTopY)
            cubicTo(
                left + w * 0.3f, duneTopY - 18f,
                left + w * 0.6f, duneTopY + 12f,
                left + w, duneTopY - 10f
            )
            lineTo(left + w, top + w)
            lineTo(left, top + w)
            close()
        }
        val dune2Color = if (isDark) Color(0xFF2A3E3C) else Color(0xFFEAD5B2)
        drawPath(dune2Path, color = dune2Color)

        val dune1Path = Path().apply {
            val duneTopY = center.y + radius * 0.45f
            moveTo(left, duneTopY)
            cubicTo(
                left + w * 0.35f, duneTopY - 14f,
                left + w * 0.7f, duneTopY + 10f,
                left + w, duneTopY - 6f
            )
            lineTo(left + w, top + w)
            lineTo(left, top + w)
            close()
        }
        val dune1Color = if (isDark) Color(0xFF34494A) else Color(0xFFE3C79A)
        drawPath(dune1Path, color = dune1Color)

        // 4. 오아시스 샘물 (진행률에 따라 수위가 차오름)
        // progress: 0%일 때는 아주 작은 웅덩이 -> 100%일 때는 가득 찬 호수
        val waterProgress = progress.coerceIn(0.1f, 1.0f)
        val waterRx = radius * (0.35f + 0.45f * waterProgress)
        val waterRy = radius * (0.08f + 0.16f * waterProgress)
        val waterCenterY = center.y + radius * (0.65f - 0.08f * waterProgress)

        val waterBrush = if (isDark) {
            Brush.verticalGradient(
                colors = listOf(Color(0xFF3E7F86), Color(0xFF1D4F55)),
                startY = waterCenterY - waterRy,
                endY = waterCenterY + waterRy
            )
        } else {
            Brush.verticalGradient(
                colors = listOf(Color(0xFF9DD5CA), Color(0xFF2E9483)),
                startY = waterCenterY - waterRy,
                endY = waterCenterY + waterRy
            )
        }

        // 물 타원
        drawOval(
            brush = waterBrush,
            topLeft = Offset(center.x - waterRx, waterCenterY - waterRy),
            size = Size(waterRx * 2f, waterRy * 2f)
        )

        // 물결 잔물결 하이라이트
        val rippleColor = if (isDark) Color(0x66F2E3C2) else Color(0x99FFFFFF)
        drawLine(
            color = rippleColor,
            start = Offset(center.x - waterRx * 0.5f, waterCenterY - waterRy * 0.2f),
            end = Offset(center.x + waterRx * 0.1f, waterCenterY - waterRy * 0.2f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = rippleColor,
            start = Offset(center.x + waterRx * 0.2f, waterCenterY + waterRy * 0.3f),
            end = Offset(center.x + waterRx * 0.6f, waterCenterY + waterRy * 0.3f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // 5. 야자수 실루엣 (오른쪽 및 왼쪽에 배치)
        drawPalmTree(
            trunkBase = Offset(center.x + radius * 0.45f, center.y + radius * 0.52f),
            scale = 0.85f,
            isDark = isDark
        )
        drawPalmTree(
            trunkBase = Offset(center.x - radius * 0.5f, center.y + radius * 0.56f),
            scale = 0.65f,
            isDark = isDark
        )
    }
}

private fun DrawScope.drawPalmTree(
    trunkBase: Offset,
    scale: Float,
    isDark: Boolean
) {
    val trunkTop = Offset(trunkBase.x + 6.dp.toPx() * scale, trunkBase.y - 48.dp.toPx() * scale)

    // Trunk
    val trunkPath = Path().apply {
        moveTo(trunkBase.x, trunkBase.y)
        quadraticTo(
            trunkBase.x + 2.dp.toPx() * scale,
            trunkBase.y - 24.dp.toPx() * scale,
            trunkTop.x,
            trunkTop.y
        )
    }
    drawLine(
        color = if (isDark) Color(0xFF6B5336) else Color(0xFF8A6A45),
        start = trunkBase,
        end = trunkTop,
        strokeWidth = 3.5.dp.toPx() * scale,
        cap = StrokeCap.Round
    )

    // Palm Leaves (Teal / Green)
    val leafColor1 = if (isDark) Color(0xFF2E9483) else OasisTeal
    val leafColor2 = if (isDark) Color(0xFF5FB8A8) else StageSpring

    // 잎 4~5장 방사형 호
    val leaves = listOf(
        Pair(-28f, -16f),
        Pair(26f, -14f),
        Pair(-18f, -32f),
        Pair(18f, -30f),
        Pair(-34f, 6f),
        Pair(32f, 8f)
    )

    leaves.forEachIndexed { i, (dx, dy) ->
        val leafEnd = Offset(trunkTop.x + dx.dp.toPx() * scale, trunkTop.y + dy.dp.toPx() * scale)
        val leafColor = if (i % 2 == 0) leafColor1 else leafColor2
        val leafPath = Path().apply {
            moveTo(trunkTop.x, trunkTop.y)
            quadraticTo(
                trunkTop.x + (dx * 0.5f).dp.toPx() * scale,
                trunkTop.y + (dy * 0.8f - 8f).dp.toPx() * scale,
                leafEnd.x,
                leafEnd.y
            )
        }
        drawPath(
            path = leafPath,
            color = leafColor,
            style = Stroke(width = 3.dp.toPx() * scale, cap = StrokeCap.Round)
        )
    }
}
