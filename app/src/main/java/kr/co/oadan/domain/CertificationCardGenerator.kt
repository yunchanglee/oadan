package kr.co.oadan.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class CardTemplate {
    PHOTO,
    MINIMAL,
    OASIS
}

enum class CardAspectRatio {
    SQUARE_1_1,
    STORY_9_16
}

data class CertCardData(
    val fastedTimeString: String, // "16h 12m"
    val stage: MetabolicStage,
    val organicGrade: String, // "오아시스", "잎새", "새싹"
    val streakDays: Int,
    val dateString: String = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(Date()),
    val quote: String = "오늘도 나만의 오아시스에서",
    val showOrganicScore: Boolean = true,
    val showWeightChange: Boolean = false,
    val weightChangeText: String? = null
)

object CertificationCardGenerator {

    fun generateCardBitmap(
        context: Context,
        data: CertCardData,
        template: CardTemplate = CardTemplate.PHOTO,
        aspectRatio: CardAspectRatio = CardAspectRatio.SQUARE_1_1,
        photoBitmap: Bitmap? = null
    ): Bitmap {
        val width = 1080
        val height = if (aspectRatio == CardAspectRatio.SQUARE_1_1) 1080 else 1920

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        when (template) {
            CardTemplate.PHOTO -> {
                drawPhotoTemplate(canvas, width, height, data, photoBitmap, paint)
            }
            CardTemplate.MINIMAL -> {
                drawMinimalTemplate(canvas, width, height, data, paint)
            }
            CardTemplate.OASIS -> {
                drawOasisTemplate(canvas, width, height, data, paint)
            }
        }

        return bitmap
    }

    private fun drawPhotoTemplate(
        canvas: Canvas,
        w: Int,
        h: Int,
        data: CertCardData,
        photo: Bitmap?,
        paint: Paint
    ) {
        val photoHeight = (h * 0.60f).toInt()
        val cardBgColor = 0xFFFAF6EF.toInt() // Sand

        // Background
        paint.color = cardBgColor
        canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), paint)

        // Photo / Top Area
        if (photo != null) {
            val src = Rect(0, 0, photo.width, photo.height)
            val dst = Rect(0, 0, w, photoHeight)
            canvas.drawBitmap(photo, src, dst, paint)
        } else {
            // Default elegant organic placeholder
            paint.color = 0xFF93A97D.toInt() // Sage
            canvas.drawRect(0f, 0f, w.toFloat(), photoHeight.toFloat(), paint)

            paint.color = 0xFFFFFFFF.toInt()
            paint.textSize = 120f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("🥗", w / 2f, photoHeight / 2f + 40f, paint)
        }

        // Top Brand Logo
        paint.color = 0xFFFFFFFF.toInt()
        paint.textSize = 42f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.LEFT
        paint.alpha = 240
        canvas.drawText("OADAN", 64f, 80f, paint)

        // Bottom Info Area
        val infoTop = photoHeight.toFloat()
        paint.color = 0xFF16796B.toInt() // Oasis Teal
        paint.textSize = 96f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText(data.fastedTimeString, 64f, infoTop + 140f, paint)

        paint.color = 0xFF5C6663.toInt() // Stone
        paint.textSize = 40f
        paint.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
        canvas.drawText(data.quote, 64f, infoTop + 210f, paint)

        // Right summary badges
        paint.textAlign = Paint.Align.RIGHT
        paint.textSize = 38f
        paint.color = 0xFF1E2A28.toInt()
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        val rightX = w - 64f
        var rightY = infoTop + 100f
        canvas.drawText("${data.stage.emoji} ${data.stage.stageName}", rightX, rightY, paint)
        rightY += 56f
        if (data.showOrganicScore) {
            canvas.drawText("🌳 ${data.organicGrade}", rightX, rightY, paint)
            rightY += 56f
        }
        canvas.drawText("🔥 ${data.streakDays}일 연속", rightX, rightY, paint)

        if (data.showWeightChange && data.weightChangeText != null) {
            rightY += 56f
            paint.color = 0xFF3E8E5A.toInt()
            canvas.drawText(data.weightChangeText, rightX, rightY, paint)
        }

        // Bottom watermark & date
        paint.textAlign = Paint.Align.LEFT
        paint.color = 0xFF5C6663.toInt()
        paint.textSize = 32f
        canvas.drawText(data.dateString, 64f, h - 60f, paint)

        paint.textAlign = Paint.Align.RIGHT
        paint.color = 0xFF16796B.toInt()
        paint.textSize = 34f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("#오아단인증", rightX, h - 60f, paint)
    }

    private fun drawMinimalTemplate(
        canvas: Canvas,
        w: Int,
        h: Int,
        data: CertCardData,
        paint: Paint
    ) {
        // Sand background
        paint.color = 0xFFFAF6EF.toInt()
        canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), paint)

        // Inner frame
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        paint.color = 0xFFD8D1C5.toInt()
        canvas.drawRect(50f, 50f, w - 50f, h - 50f, paint)
        paint.style = Paint.Style.FILL

        // Center quote & time
        paint.textAlign = Paint.Align.CENTER
        paint.color = 0xFF16796B.toInt()
        paint.textSize = 40f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("OADAN", w / 2f, 160f, paint)

        paint.textSize = 140f
        paint.color = 0xFF1E2A28.toInt()
        canvas.drawText(data.fastedTimeString, w / 2f, h / 2f - 40f, paint)

        paint.textSize = 48f
        paint.color = 0xFF5C6663.toInt()
        paint.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
        canvas.drawText(data.quote, w / 2f, h / 2f + 50f, paint)

        paint.textSize = 40f
        paint.color = 0xFF16796B.toInt()
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("${data.stage.emoji} ${data.stage.stageName}  ·  🔥 ${data.streakDays}일째", w / 2f, h / 2f + 140f, paint)

        // Footer
        paint.textSize = 36f
        paint.color = 0xFF5C6663.toInt()
        canvas.drawText("${data.dateString}  |  #오아단인증", w / 2f, h - 100f, paint)
    }

    private fun drawOasisTemplate(
        canvas: Canvas,
        w: Int,
        h: Int,
        data: CertCardData,
        paint: Paint
    ) {
        // Oasis Teal Gradient or Deep Calm
        paint.color = 0xFF16796B.toInt()
        canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), paint)

        // Desert Dune curves at bottom
        paint.color = 0xFF5FB8A8.toInt()
        val rectF = RectF(-200f, h - 450f, w + 200f, h + 300f)
        canvas.drawOval(rectF, paint)

        paint.color = 0xFFFAF6EF.toInt()
        val innerRect = RectF(-100f, h - 300f, w + 100f, h + 200f)
        canvas.drawOval(innerRect, paint)

        // Text in White
        paint.textAlign = Paint.Align.CENTER
        paint.color = 0xFFFFFFFF.toInt()
        paint.textSize = 48f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("비우고, 채우는 오아시스", w / 2f, 220f, paint)

        paint.textSize = 140f
        canvas.drawText(data.fastedTimeString, w / 2f, 420f, paint)

        paint.textSize = 48f
        canvas.drawText("${data.stage.emoji} ${data.stage.stageName} 도달  ·  🔥 ${data.streakDays}일째", w / 2f, 520f, paint)

        // Bottom on Sand
        paint.color = 0xFF1E2A28.toInt()
        paint.textSize = 40f
        canvas.drawText(data.quote, w / 2f, h - 140f, paint)

        paint.color = 0xFF16796B.toInt()
        paint.textSize = 34f
        canvas.drawText("${data.dateString}  #오아단인증", w / 2f, h - 70f, paint)
    }

    fun saveBitmapToCache(context: Context, bitmap: Bitmap): File {
        val cachePath = File(context.cacheDir, "cert_cards")
        cachePath.mkdirs()
        val file = File(cachePath, "oadan_cert_${System.currentTimeMillis()}.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return file
    }
}
