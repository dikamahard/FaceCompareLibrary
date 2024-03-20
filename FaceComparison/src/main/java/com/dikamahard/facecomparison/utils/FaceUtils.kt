package com.dikamahard.facecomparison.utils

import android.content.res.AssetManager
import android.graphics.Bitmap
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

object FaceUtils {

    @Throws(IOException::class)
    fun loadModelFile(
        assetManager: AssetManager,
        modelPath: String
    ): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream =
            FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            startOffset,
            declaredLength
        )
    }

    fun normalizeImage(bitmap: Bitmap): Array<Array<FloatArray>> {
        val h = bitmap.height
        val w = bitmap.width
        val floatValues =
            Array(
                h
            ) { Array(w) { FloatArray(3) } }
        val imageMean = 127.5f
        val imageStd = 128f
        val pixels = IntArray(h * w)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, w, h)
        for (i in 0 until h) {
            for (j in 0 until w) {
                val `val` = pixels[i * w + j]
                val r = ((`val` shr 16 and 0xFF) - imageMean) / imageStd
                val g = ((`val` shr 8 and 0xFF) - imageMean) / imageStd
                val b = ((`val` and 0xFF) - imageMean) / imageStd
                val arr = floatArrayOf(r, g, b)
                floatValues[i][j] = arr
            }
        }
        return floatValues
    }

    fun l2Normalize(
        embeddings: Array<FloatArray>,
        epsilon: Double
    ) {
        for (i in embeddings.indices) {
            var squareSum = 0f
            for (element in embeddings[i]) {
                squareSum += element.toDouble().pow(2.0).toFloat()
            }
            val xInvNorm = sqrt(
                max(
                    squareSum.toDouble(),
                    epsilon
                )
            ).toFloat()
            for (j in embeddings[i].indices) {
                embeddings[i][j] = embeddings[i][j] / xInvNorm
            }
        }
    }
}