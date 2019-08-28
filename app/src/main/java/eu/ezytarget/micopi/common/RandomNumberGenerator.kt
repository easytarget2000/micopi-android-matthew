package eu.ezytarget.micopi.common

import kotlin.random.Random

class RandomNumberGenerator(private val source: Random = Random(System.currentTimeMillis())) {

    fun nextInt(from: Int, until: Int): Int {
        return source.nextInt(from, until)
    }

    fun nextFloat(from: Float, until: Float): Float {
        return source.nextDouble(from.toDouble(), until.toDouble()).toFloat()
    }
}