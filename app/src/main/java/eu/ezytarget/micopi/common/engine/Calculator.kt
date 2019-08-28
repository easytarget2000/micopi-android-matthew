package eu.ezytarget.micopi.common.engine

import kotlin.math.PI
import kotlin.math.sqrt

class Calculator {

    fun circleCoveringSquare(sideLength: Float): Float {
        return sideLength * sqrt(2f)
    }

    fun lerp(a: Float, b: Float, ratio: Float): Float {
        return a + (ratio * (b - a))
    }

    fun lerp(a: Float, b: Float, index: Int, maxIndex: Int): Float {
        if (maxIndex < 1) {
            return 0f
        }

        val ratio = index.toFloat() / maxIndex.toFloat()
        return lerp(a, b, ratio)
    }

    companion object {
        const val PIF = PI.toFloat()
        const val TWO_PIF = 2f * PIF
    }
}