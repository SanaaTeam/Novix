package com.sanaa.image_viewer.cashe

import android.graphics.Bitmap
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set

object ImageCache {
    private val bitmapCache = ConcurrentHashMap<String, Bitmap>()
    private val classificationCache = ConcurrentHashMap<String, ClassificationResult>()

    data class ClassificationResult(
        val isSensitive: Boolean,
        val safeThreshold: Float,
        val sensitiveThreshold: Float
    )

    fun getBitmap(url: String): Bitmap? = bitmapCache[url]

    fun putBitmap(url: String, bitmap: Bitmap) {
        bitmapCache[url] = bitmap
    }

    fun getClassification(url: String, safeThreshold: Float, sensitiveThreshold: Float): Boolean? {
        val key = "${url}_${safeThreshold}_${sensitiveThreshold}"
        val result = classificationCache[key]
        return if (result?.safeThreshold == safeThreshold && result.sensitiveThreshold == sensitiveThreshold) {
            result.isSensitive
        } else null
    }

    fun putClassification(
        url: String,
        safeThreshold: Float,
        sensitiveThreshold: Float,
        isSensitive: Boolean
    ) {
        val key = "${url}_${safeThreshold}_${sensitiveThreshold}"
        classificationCache[key] =
            ClassificationResult(isSensitive, safeThreshold, sensitiveThreshold)
    }

    fun clearCache() {
        bitmapCache.clear()
        classificationCache.clear()
    }
}