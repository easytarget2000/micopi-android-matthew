package eu.ezytarget.micopi

import eu.ezytarget.micopi.common.engine.Calculator
import eu.ezytarget.micopi.common.engine.TightDiskPainter
import org.junit.Test

import org.junit.Assert.*
import kotlin.math.exp

class CalculatorTests {

    @Test
    fun lerp_isCorrect() {
        val calculator = Calculator()

        val lerpValue1 = calculator.lerp(0f, 1f, 0.5f)
        val expectedLerpValue1 = 0.5f
        assert(lerpValue1 == expectedLerpValue1)

        val lerpValue2 = calculator.lerp(0f, 1f, 0.8f)
        val expectedLerpValue2 = 0.8f
        assert(lerpValue2 == expectedLerpValue2)

        val lerpValue3 = calculator.lerp(0f, 1f, 0f)
        val expectedLerpValue3 = 0f
        assert(lerpValue3 == expectedLerpValue3)
    }

    @Test
    fun indexLerp_isCorrect() {
        val calculator = Calculator()

        val lerpValue1 = calculator.lerp(0f, 1f, 0, 100)
        val expectedLerpValue1 = 0f
        assert(lerpValue1 == expectedLerpValue1)

        val lerpValue2 = calculator.lerp(0f, 1f, 50, 100)
        val expectedLerpValue2 = 0.5f
        assert(lerpValue2 == expectedLerpValue2)

        val lerpValue3 = calculator.lerp(0f, 1f, 75, 100)
        val expectedLerpValue3 = 0.75f
        assert(lerpValue3 == expectedLerpValue3)
    }
}
