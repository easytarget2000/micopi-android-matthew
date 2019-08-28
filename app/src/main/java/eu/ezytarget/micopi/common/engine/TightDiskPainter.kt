package eu.ezytarget.micopi.common.engine

import android.graphics.Canvas
import android.util.Log
import eu.ezytarget.matthew.CanvasSizeQuantifier
import eu.ezytarget.matthew.Color
import eu.ezytarget.matthew.Matthew
import eu.ezytarget.micopi.common.RandomNumberGenerator

class TightDiskPainter(
    private val canvasSizeQuantifier: CanvasSizeQuantifier = CanvasSizeQuantifier()
) {

    lateinit var matthew: Matthew

    var numberOfDisks = 48
    var minRadiusToImageRatio = 1f / 32f
    var centerRelativeXPosition = 0.5f
    var centerRelativeYPosition = 0.5f

    fun configureRandomly(randomNumberGenerator: RandomNumberGenerator = RandomNumberGenerator()) {
        numberOfDisks = randomNumberGenerator.nextInt(
            from = MIN_RANDOM_NUMBER_OF_DISKS,
            until = MAX_RANDOM_NUMBER_OF_DISKS
        )

        centerRelativeXPosition = randomNumberGenerator.nextFloat(
            from = MIN_RELATIVE_POSITION,
            until = MAX_RELATIVE_POSITION
        )
        centerRelativeYPosition = randomNumberGenerator.nextFloat(
            from = MIN_RELATIVE_POSITION,
            until = MAX_RELATIVE_POSITION
        )
    }

    fun paint(canvas: Canvas, oneColor: Color? = null) {
        if (numberOfDisks !in numberOfDisksRange) {
            Log.e(tag, "paint(): numberOfDisks is out of range, $numberOfDisks.")
            return
        }

        val imageSize = canvasSizeQuantifier.valueForCanvas(canvas)

        val stackMinRadius = imageSize * minRadiusToImageRatio
        val stackCenterX = imageSize * centerRelativeXPosition
        val stackCenterY = imageSize * centerRelativeYPosition

        for (diskCounter in numberOfDisks downTo 0) {
            val color: Color = oneColor ?: matthew.colorAtModuloIndex(diskCounter)
            val radius = stackMinRadius * diskCounter.toFloat()
            matthew.paintCircularShapeWithRadius(
                stackCenterX,
                stackCenterY,
                radius,
                color,
                canvas
            )
        }
    }

    companion object {
        const val MIN_NUMBER_OF_DISKS = 1
        const val MAX_NUMBER_OF_DISKS = 1000
        const val MIN_RANDOM_NUMBER_OF_DISKS = 40
        const val MAX_RANDOM_NUMBER_OF_DISKS = 56
        const val MIN_RELATIVE_POSITION = 0.1f
        const val MAX_RELATIVE_POSITION = 0.9f
        val tag = TightDiskPainter::class.java.simpleName
        private val numberOfDisksRange = MIN_NUMBER_OF_DISKS..MAX_NUMBER_OF_DISKS
    }
}