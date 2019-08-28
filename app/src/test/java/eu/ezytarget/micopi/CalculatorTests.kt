package eu.ezytarget.micopi

import eu.ezytarget.micopi.common.engine.Calculator
import org.junit.Test

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

        val lerpValue3 = calculator.lerp(0f, 1f, 99, 100)
        val expectedLerpValue3 = 0.99f
        assert(lerpValue3 == expectedLerpValue3)

        val lerpValue4 = calculator.lerp(0f, 1f, 100, 100)
        val expectedLerpValue4 = 1f
        assert(lerpValue4 == expectedLerpValue4)
    }
}
