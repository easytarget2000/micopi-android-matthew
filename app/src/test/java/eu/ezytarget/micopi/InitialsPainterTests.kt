package eu.ezytarget.micopi

import eu.ezytarget.micopi.common.engine.InitialsPainter
import org.junit.Test

class InitialsPainterTests {

    @Test
    fun textSize_isReasonable() {
        val initialsPainter = InitialsPainter()
        val imageSize = 1000f
        val numberOfChars1 = 1
        val textSize1 = initialsPainter.textSize(imageSize, numberOfChars1)
        val expectedTextSize1 = imageSize * initialsPainter.textSizeMaxPercentage
        assert(textSize1 == expectedTextSize1)

        val numberOfChars2 = 2
        val textSize2 = initialsPainter.textSize(imageSize, numberOfChars2)

        val numberOfChars3 = 16
        val textSize3 = initialsPainter.textSize(imageSize, numberOfChars3)

        assert(textSize1 > textSize2)
        assert(textSize2 > textSize3)
    }
}
