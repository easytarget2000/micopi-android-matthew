package eu.ezytarget.micopi.common.engine

import android.content.res.Resources
import android.graphics.Canvas
import eu.ezytarget.matthew.Matthew
import eu.ezytarget.matthew.pattern.TwirlyDisksMatthewPattern
import eu.ezytarget.matthew.util.RandomNumberGenerator
import eu.ezytarget.micopi.common.data.ContactHashWrapper

class ContactImageEngine(
    private val matthew: Matthew = Matthew(),
    private val disksMatthewPattern: TwirlyDisksMatthewPattern = TwirlyDisksMatthewPattern(),
    private val initialsPainter: InitialsPainter = InitialsPainter()
) {
    private lateinit var contactHashWrappers: Array<ContactHashWrapper>
    private lateinit var randomNumberGenerator: RandomNumberGenerator
    private var stopped = false
    var numberOfInitials = 1

    fun generateImageAsync(
        contactHashWrappers: Array<ContactHashWrapper>,
        resources: Resources?,
        callback: ContatImageEngineCallback?
    ) {
        this.contactHashWrappers = contactHashWrappers
        if (resources != null) {
            matthew.populateColorProvider(resources)
        }
        stopped = false

        contactHashWrappers.forEach {
            generateImage(it, callback)
        }
    }

    fun generateImage(
        contactHashWrapper: ContactHashWrapper,
        callback: ContatImageEngineCallback?
    ) {
        val contactHash = contactHashWrapper.hashCode()
        randomNumberGenerator = RandomNumberGenerator(contactHash)
        val bitmapBackedCanvas = matthew.configuredBitmapBackedCanvas(
            IMAGE_WIDTH,
            IMAGE_HEIGHT,
            random = randomNumberGenerator.source
        )

        paintBackground(bitmapBackedCanvas.canvas)
        paintShapes(bitmapBackedCanvas.canvas)
        paintInitials(bitmapBackedCanvas.canvas, contactHashWrapper)
        callback?.invoke(contactHashWrapper, bitmapBackedCanvas.bitmap, true, true)
    }

    private fun paintBackground(canvas: Canvas) {
        val backgroundColor = matthew.colorAtModuloIndex(randomNumberGenerator.positiveInt())
        matthew.fillCanvas(canvas, backgroundColor)
    }

    private fun paintShapes(canvas: Canvas) {
        disksMatthewPattern.randomNumberGenerator = randomNumberGenerator
        disksMatthewPattern.configureRandomly()
        disksMatthewPattern.paint(matthew, canvas)
    }

    private fun paintInitials(canvas: Canvas, contactHashWrapper: ContactHashWrapper) {
        var initials = contactHashWrapper.initials(numberOfInitials)
        initialsPainter.paint(initials, canvas)
    }

    companion object {
        private const val IMAGE_WIDTH = 1080
        private const val IMAGE_HEIGHT = IMAGE_WIDTH

    }
}