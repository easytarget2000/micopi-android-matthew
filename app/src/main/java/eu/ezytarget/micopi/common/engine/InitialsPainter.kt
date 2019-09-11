package eu.ezytarget.micopi.common.engine

import android.graphics.*
import eu.ezytarget.matthew.CanvasSizeQuantifier
import kotlin.math.sqrt

class InitialsPainter(
    private val paint: Paint = Paint(),
    private val canvasSizeQuantifier: CanvasSizeQuantifier = CanvasSizeQuantifier()
) {

    var typeface = Typeface.create(DEFAULT_TYPEFACE_FONT_FAMILY, Typeface.NORMAL)
    var textSizeMaxPercentage = 0.66f
    var color = Color.WHITE

    fun paint(string: String, canvas: Canvas) {
        if (string.isEmpty()) {
            return
        }

        val imageSize = canvasSizeQuantifier.valueForCanvas(canvas)
        paint.color = color
        paint.typeface = typeface
        paint.textSize = textSize(imageSize, numberOfChars = string.length)
        paint.textAlign = Paint.Align.CENTER
        paint.style = Paint.Style.FILL

        val rectFittingChars = Rect()
        paint.getTextBounds(string, 0, 1, rectFittingChars)
        val imageSizeHalf = imageSize / 2f
        val charsHeight = rectFittingChars.bottom - rectFittingChars.top

        canvas.drawText(
            string,
            imageSizeHalf,
            imageSizeHalf + (charsHeight.toFloat() / 2f),
            paint
        )
    }

    internal fun textSize(imageSize: Float, numberOfChars: Int): Float {
        return textSizeMaxPercentage / sqrt(numberOfChars.toFloat()) * imageSize
    }

    companion object {
        const val DEFAULT_TYPEFACE_FONT_FAMILY = "sans-serif"
    }
}