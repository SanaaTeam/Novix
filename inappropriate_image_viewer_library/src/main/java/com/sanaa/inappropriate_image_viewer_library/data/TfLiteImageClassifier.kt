package com.sanaa.inappropriate_image_viewer_library.data

import android.content.Context
import android.graphics.Bitmap
import com.sanaa.inappropriate_image_viewer_library.domain.Classification
import com.sanaa.inappropriate_image_viewer_library.domain.ImageClassifierRepository
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class TfLiteImageClassifier(
    private val context: Context,
    private val modelPath: String,
) : ImageClassifierRepository {

    private var classifier: ImageClassifier? = null

    private fun setupClassifier() {
        val baseOptions = BaseOptions.builder()
            .setNumThreads(2).build()
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(2)
            .build()

        try {
            classifier = ImageClassifier.createFromFileAndOptions(context, modelPath, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun classify(bitmap: Bitmap): List<Classification> {
        if (classifier == null) {
            setupClassifier()
        }
        val imageProcessor = ImageProcessor.Builder().build()
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))


        val results = classifier?.classify(tensorImage) ?: emptyList()

        return results.flatMap { classifications ->
            classifications.categories.mapIndexed { index, category ->
                Classification(
                    name = labels[category.label.toIntOrNull() ?: 0], // Assuming index 0 is SFW and 1 is NSFW
                    score = category.score
                )
            }
        }
    }

    companion object{
        private val labels = listOf("SFW", "NSFW")
    }
}