package eu.ezytarget.micopi.common.engine

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Calculator {

    fun circleCoveringSquare(sideLength: Float): Float {
        return sideLength * sqrt(2f)
    }

    fun lerp(a: Float, b: Float, ratio: Float): Float {
        return a + (ratio * (b - a))
    }

    fun lerp(a: Float, b: Float, index: Int, maxIndex: Int): Float {
        val ratio = normalizedRatio(index, maxIndex)
        return lerp(a, b, ratio)
    }

    fun cosineInterpolate(
        a: Float,
        b: Float,
        mu: Float
    ): Float {
        val mu2 = (1f - cos(mu * PIF)) / 2f
        return a * (1f - mu2) + b * mu2
    }

    fun cosineInterpolate(a: Float, b: Float, index: Int, maxIndex: Int): Float {
        val ratio = normalizedRatio(index, maxIndex)
        return cosineInterpolate(a, b, ratio)
    }

    fun normalizedRatio(index: Int, maxIndex: Int): Float {
        if (maxIndex < 1) {
            return 0f
        }

        return index.toFloat() / maxIndex.toFloat()
    }

    fun pointOnCircleX(arcRelativePosition: Float, radius: Float): Float {
        return cos(x = arcRelativePosition * TWO_PIF) * radius
    }

    fun pointOnCircleY(arcRelativePosition: Float, radius: Float): Float {
        return sin(x = arcRelativePosition * TWO_PIF) * radius
    }

    companion object {
        const val PIF = PI.toFloat()
        const val TWO_PIF = 2f * PIF
    }
}