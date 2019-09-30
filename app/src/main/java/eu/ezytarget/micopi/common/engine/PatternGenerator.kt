package eu.ezytarget.micopi.common.engine

import android.graphics.Canvas
import eu.ezytarget.matthew.Matthew
import eu.ezytarget.matthew.pattern.ConvergingCardsMatthewPattern
import eu.ezytarget.matthew.pattern.TwirlyDisksMatthewPattern
import eu.ezytarget.matthew.util.RandomNumberGenerator

class PatternGenerator(
    private val cardsMatthewPattern: ConvergingCardsMatthewPattern
    = ConvergingCardsMatthewPattern(),
    private val disksMatthewPattern: TwirlyDisksMatthewPattern = TwirlyDisksMatthewPattern()
) {

    fun paint(matthew: Matthew, canvas: Canvas, randomNumberGenerator: RandomNumberGenerator) {
        val patternDecision = randomNumberGenerator.float(0f, 1f)
        if (patternDecision < CARDS_PROBABILITY) {
            cardsMatthewPattern.configureRandomly()
            cardsMatthewPattern.paintRandomly(matthew, canvas)
        } else {
            disksMatthewPattern.randomNumberGenerator = randomNumberGenerator
            disksMatthewPattern.configureRandomly()
            disksMatthewPattern.paint(matthew, canvas)
        }
    }

    companion object {
        private const val CARDS_PROBABILITY = 0.5f
        private const val DISKS_PROBABILITY = 1f - CARDS_PROBABILITY
    }
}