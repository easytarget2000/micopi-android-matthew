package eu.ezytarget.micopi.common.engine

import android.graphics.Canvas
import android.util.Log
import eu.ezytarget.matthew.CanvasSizeQuantifier
import eu.ezytarget.matthew.Color
import eu.ezytarget.matthew.Matthew
import eu.ezytarget.micopi.common.RandomNumberGenerator
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class TightDiskPainter(
    private val canvasSizeQuantifier: CanvasSizeQuantifier = CanvasSizeQuantifier()
) {

    lateinit var matthew: Matthew

    var numberOfDisks = 48
    var minRadiusToImageRatio = 1f / 32f
    var centerRelativeXPosition = 0.5f
    var centerRelativeYPosition = 0.5f
    var twirlRadiusToImageRatio = 1f / 6f
    var twirlXRatio = 1f
    var twirlYRatio = 1f

    fun configureRandomly(randomNumberGenerator: RandomNumberGenerator = RandomNumberGenerator()) {
        numberOfDisks = randomNumberGenerator.int(
            from = MIN_RANDOM_NUMBER_OF_DISKS,
            until = MAX_RANDOM_NUMBER_OF_DISKS
        )
        minRadiusToImageRatio = 1f / numberOfDisks

        centerRelativeXPosition = randomNumberGenerator.float(
            from = MIN_RELATIVE_POSITION,
            until = MAX_RELATIVE_POSITION
        )
        centerRelativeYPosition = randomNumberGenerator.float(
            from = MIN_RELATIVE_POSITION,
            until = MAX_RELATIVE_POSITION
        )
        twirlRadiusToImageRatio = randomNumberGenerator.float(
            from = MIN_TWIRL_RADIUS_TO_IMAGE_RATIO,
            until = MAX_TWIRL_RADIUS_TO_IMAGE_RATIO
        )
        twirlXRatio = randomNumberGenerator.float(
            from = MIN_TWIRL_RATIO,
            until = MAX_TWIRL_RATIO
        )
        twirlYRatio = randomNumberGenerator.float(
            from = MIN_TWIRL_RATIO,
            until = MAX_TWIRL_RATIO
        )
    }

    fun paint(canvas: Canvas, oneColor: Color? = null) {
        if (numberOfDisks !in numberOfDisksRange) {
            Log.e(tag, "paint(): numberOfDisks is out of range, $numberOfDisks.")
            return
        }

        val imageSize = canvasSizeQuantifier.valueForCanvas(canvas)

        val stackMinRadius = (imageSize * 1.5f) * minRadiusToImageRatio
        val stackCenterX = imageSize * centerRelativeXPosition
        val stackCenterY = imageSize * centerRelativeYPosition
        val maxDistanceOffset = imageSize * twirlRadiusToImageRatio

        for (diskCounter in numberOfDisks downTo 0) {
            val color: Color = oneColor ?: matthew.colorAtModuloIndex(diskCounter)
            val radius = stackMinRadius * diskCounter.toFloat()
            val normalizedCounter = diskCounter.toFloat() / numberOfDisks
            val twirlXOffset = sin(twirlXRatio * normalizedCounter * TWO_PIF) * maxDistanceOffset
            val twirlYOffset = cos(twirlYRatio * normalizedCounter * TWO_PIF) * maxDistanceOffset
            val diskX = stackCenterX + twirlXOffset
            val diskY = stackCenterY + twirlYOffset

            matthew.paintCircularShapeWithRadius(
                diskX,
                diskY,
                radius,
                color,
                canvas
            )
        }
    }

    companion object {
        const val MIN_NUMBER_OF_DISKS = 1
        const val MAX_NUMBER_OF_DISKS = 1000
        const val MIN_RANDOM_NUMBER_OF_DISKS = 48
        const val MAX_RANDOM_NUMBER_OF_DISKS = 64
        const val MIN_RELATIVE_POSITION = 0.25f
        const val MAX_RELATIVE_POSITION = 0.75f
        const val MIN_TWIRL_RADIUS_TO_IMAGE_RATIO = 1f / 7f
        const val MAX_TWIRL_RADIUS_TO_IMAGE_RATIO = 1f / 4f
        const val MIN_TWIRL_RATIO = -1.5f
        const val MAX_TWIRL_RATIO = 1.5f
        private const val PIF = PI.toFloat()
        private const val TWO_PIF = 2f * PIF
        val tag = TightDiskPainter::class.java.simpleName
        private val numberOfDisksRange = MIN_NUMBER_OF_DISKS..MAX_NUMBER_OF_DISKS
    }
}