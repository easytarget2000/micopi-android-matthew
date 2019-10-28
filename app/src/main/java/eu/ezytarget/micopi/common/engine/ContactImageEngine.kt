package eu.ezytarget.micopi.common.engine

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import eu.ezytarget.matthew.Matthew
import eu.ezytarget.matthew.util.RandomNumberGenerator
import eu.ezytarget.micopi.common.data.ContactHashWrapper

class ContactImageEngine(
    private val matthew: Matthew = Matthew(),
    private val patternGenerator: PatternGenerator = PatternGenerator(),
    private val initialsPainter: InitialsPainter = InitialsPainter()
) {
    private lateinit var randomNumberGenerator: RandomNumberGenerator
    var numberOfInitials = 1

    fun populateColorProvider(resources: Resources) {
        matthew.populateColorProvider(resources)
    }

    fun generateBitmap(contactHashWrapper: ContactHashWrapper): Bitmap {
        val contactHash = contactHashWrapper.hashCode()
        randomNumberGenerator = RandomNumberGenerator(contactHash)
        val bitmapBackedCanvas = matthew.configuredBitmapBackedCanvas(
            IMAGE_WIDTH,
            IMAGE_HEIGHT,
            random = randomNumberGenerator.source
        )

        paintBackground(bitmapBackedCanvas.canvas)
        paintPattern(bitmapBackedCanvas.canvas)
        paintInitials(bitmapBackedCanvas.canvas, contactHashWrapper)

        return bitmapBackedCanvas.bitmap
    }

    fun generateBitmapAsync(
        contactHashWrapper: ContactHashWrapper,
        callback: ContatImageEngineCallback
    ) {
        val asyncTask = AsyncTask(this, contactHashWrapper, callback)
        asyncTask.execute()
    }

    private fun paintBackground(canvas: Canvas) {
        val backgroundColor = matthew.colorAtModuloIndex(randomNumberGenerator.positiveInt())
        matthew.fillCanvas(canvas, backgroundColor)
    }

    private fun paintPattern(canvas: Canvas) {
        patternGenerator.paint(matthew, canvas, randomNumberGenerator)
    }

    private fun paintInitials(canvas: Canvas, contactHashWrapper: ContactHashWrapper) {
        val initials = contactHashWrapper.initials(numberOfInitials)
        initialsPainter.paint(initials, canvas)
    }

    private class AsyncTask(
        private val engine: ContactImageEngine,
        private val contactHashWrapper: ContactHashWrapper,
        private val callback: ContatImageEngineCallback? = null
    ): android.os.AsyncTask<ContactHashWrapper, Bitmap?, Bitmap?>() {

        override fun doInBackground(vararg p0: ContactHashWrapper?): Bitmap? {
            return engine.generateBitmap(contactHashWrapper)
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            callback?.invoke(contactHashWrapper, result, true)
        }
    }

    companion object {
        private const val IMAGE_WIDTH = 1024
        private const val IMAGE_HEIGHT = IMAGE_WIDTH

    }
}