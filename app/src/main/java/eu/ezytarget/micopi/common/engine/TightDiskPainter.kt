package eu.ezytarget.micopi.common.engine

import android.graphics.Canvas
import eu.ezytarget.matthew.CanvasSizeQuantifier
import eu.ezytarget.matthew.Matthew

class TightDiskPainter(
    private val canvasSizeQuantifier: CanvasSizeQuantifier = CanvasSizeQuantifier()
) {

    lateinit var matthew: Matthew

    fun paint(canvas: Canvas, numberOfDisks: Int = 48) {
        val imageSize = canvasSizeQuantifier.valueForCanvas(canvas)

        val firstStackMinRadius = imageSize / 32f
        val firstStackCenterX = imageSize / 3f
        val firstStackCenterY = imageSize * 0.67f

        for (diskCounter in numberOfDisks downTo 0) {
            val color = matthew.colorAtModuloIndex(diskCounter)
            val radius = firstStackMinRadius * diskCounter.toFloat()
            matthew.paintCircularShapeWithRadius(
                firstStackCenterX,
                firstStackCenterY,
                radius,
                color,
                canvas
            )
        }
    }
}