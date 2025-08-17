package com.sanaa.image_viewer.transformation

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation

class BlurTransformation(
    private val context: Context,
    private val radius: Float = 10f
) : Transformation {

    override val cacheKey: String = "${javaClass.name}-$radius"
    override suspend fun transform(
        input: Bitmap,
        size: Size
    ) = blurBitmap(input, radius)


    private fun blurBitmap(bitmap: Bitmap, radius: Float): Bitmap {
        val output = createBitmap(bitmap.width, bitmap.height, bitmap.config!!)

        val rs = RenderScript.create(context)
        val inputAlloc = Allocation.createFromBitmap(rs, bitmap)
        val outputAlloc = Allocation.createFromBitmap(rs, output)

        val blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        blur.setRadius(radius.coerceIn(1f, 25f))
        blur.setInput(inputAlloc)
        blur.forEach(outputAlloc)

        outputAlloc.copyTo(output)
        rs.destroy()

        return output
    }
}