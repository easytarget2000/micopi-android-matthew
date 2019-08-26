package eu.ezytarget.micopi.common.engine

import android.content.res.Resources
import android.graphics.Canvas
import eu.ezytarget.matthew.Matthew
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import kotlin.random.Random

class ContactImageEngine {

    private lateinit var contactHashWrappers: Array<ContactHashWrapper>
    private lateinit var random: Random
    private var stopped = false
    private var matthew: Matthew = Matthew()

    fun generateImageAsync(
        contactHashWrappers: Array<ContactHashWrapper>,
        resources: Resources,
        callback: ContatImageEngineCallback?
    ) {
        this.contactHashWrappers = contactHashWrappers
        matthew.populateColorProvider(resources)
        stopped = false

        contactHashWrappers.forEach {
            generateImage(it, callback)
        }
    }

    fun generateImage(
        contactHashWrapper: ContactHashWrapper,
        callback: ContatImageEngineCallback?
    ) {
        random = Random(contactHashWrapper.hashCode())
        val bitmapBackedCanvas = matthew.configuredBitmapBackedCanvas(
            IMAGE_WIDTH,
            IMAGE_HEIGHT,
            random = random
        )

        paintBackground(bitmapBackedCanvas.canvas)
        callback?.invoke(contactHashWrapper, bitmapBackedCanvas.bitmap, true, true)
    }

    private fun paintBackground(canvas: Canvas) {
        val backgroundColor = matthew.colorAtModuloIndex(random.nextInt(until = 2000))
        matthew.fillCanvas(canvas, backgroundColor)
    }

    companion object {
        private const val IMAGE_WIDTH = 1080
        private const val IMAGE_HEIGHT = IMAGE_WIDTH

    }
}