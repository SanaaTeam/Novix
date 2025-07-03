package com.sanaa.inappropriate_image_viewer_library.data

import android.content.Context
import android.graphics.Bitmap
import com.sanaa.inappropriate_image_viewer_library.domain.Classification
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

internal class TfLiteImageClassifier(private val context: Context) {

    private var classifier: ImageClassifier? = null

    fun isInappropriateImage(bitmap: Bitmap, sfwThreshold: Float, nsfwThreshold: Float): Boolean {
        val classifications = classify(bitmap)

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
            .setNumThreads(2).build()
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(2)
            .build()

        try {
            classifier = ImageClassifier.createFromFileAndOptions(context, MODEL_PATH, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val MODEL_PATH = "nsfw_model.tflite"
        private const val SFW_LABEL = "0"
        private const val NSFW_LABEL = "1"
    }
}