package com.sanaa.image_viewer.classifier

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.scale
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

internal class TfLiteImageClassifier(private val context: Context) {

    private var classifier: ImageClassifier? = null

    fun isInappropriateImage(bitmap: Bitmap, sfwThreshold: Float, nsfwThreshold: Float): Boolean {
        val classifications = classify(bitmap.scale(BITMAP_SCALE, BITMAP_SCALE))

        val sfwScore = classifications.find { it.label == SFW_LABEL }?.score ?: 0f
        val nsfwScore = classifications.find { it.label == NSFW_LABEL }?.score ?: 0f

        return (sfwScore < sfwThreshold && nsfwScore > nsfwThreshold)
    }

    private fun classify(bitmap: Bitmap): List<Classification> {
        if (classifier == null) {
            setupClassifier()
        }
        val imageProcessor = ImageProcessor.Builder().build()
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

        val results = classifier?.classify(tensorImage) ?: emptyList()

        return results.flatMap { classifications ->
            classifications.categories.mapIndexed { index, category ->
                Classification(
                    label = category.label,
                    score = category.score
                )
            }
        }
    }

    private fun setupClassifier() {
        val baseOptions = BaseOptions.builder()
            .setNumThreads(NUMBER_OF_THREADS).build()
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(MAX_RESULTS_NUMBER)
            .build()

        try {
            classifier = ImageClassifier.createFromFileAndOptions(context, MODEL_PATH, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private data class Classification(
        val label: String,
        val score: Float,
    )

    companion object {
        private const val MODEL_PATH = "nsfw_model.tflite"
        private const val SFW_LABEL = "0"
        private const val NSFW_LABEL = "1"
        private const val NUMBER_OF_THREADS = 2
        private const val MAX_RESULTS_NUMBER = 2
        private const val BITMAP_SCALE = 224
    }
}

