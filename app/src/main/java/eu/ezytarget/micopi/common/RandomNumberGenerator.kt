package eu.ezytarget.micopi.common

import kotlin.random.Random

class RandomNumberGenerator(
    seed: Int = System.currentTimeMillis().toInt(),
    val source: Random = Random(seed)
) {

    fun boolean(): Boolean {
        return source.nextBoolean()
    }

    fun int(from: Int = Int.MIN_VALUE, until: Int = Int.MAX_VALUE): Int {
        return source.nextInt(from, until)
    }

    fun positiveInt(): Int {
        return int(from = 1, until = Int.MAX_VALUE)
    }

    fun float(from: Float, until: Float): Float {
        return source.nextDouble(from.toDouble(), until.toDouble()).toFloat()
    }
}