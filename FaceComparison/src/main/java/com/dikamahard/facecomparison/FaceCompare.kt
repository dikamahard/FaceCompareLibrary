package com.dikamahard.facecomparison

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import com.dikamahard.facecomparison.model.MobileFaceNet
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.IOException


class FaceCompare(assetManager: AssetManager?) {

    lateinit var mfn: MobileFaceNet

    init {
        try {
            mfn = MobileFaceNet(assetManager)
        } catch (e: IOException) {
            Log.e("ERROR", "Error initing model", e)
        } finally {
            Log.d("DEBUG", "Succes initing model")
        }
    }

    fun compareFaces(bitmap1: Bitmap, bitmap2: Bitmap, callback: (Boolean) -> Unit) {
        var croppedBitmap1: Bitmap? = null
        var croppedBitmap2: Bitmap? = null


        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE) // skips landmark mapping
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE) // skips facial expressions and other classification such as wink
            .build()

        val facedetector = FaceDetection.getClient(options)
        var image = InputImage.fromBitmap(bitmap1, 0)

        facedetector.process(image)
            .addOnSuccessListener { faces1 ->
                for (face in faces1) {
                    val bounds = face.boundingBox
                    croppedBitmap1 = Bitmap.createBitmap(bitmap1,bounds.left, bounds.top, bounds.right - bounds.left, bounds.bottom - bounds.top)
                }

                image = InputImage.fromBitmap(bitmap2, 0)

                facedetector.process(image)
                    .addOnSuccessListener { faces2 ->
                        for (face in faces2) {
                            val bounds = face.boundingBox
                            croppedBitmap2 = Bitmap.createBitmap(bitmap2,bounds.left, bounds.top, bounds.right - bounds.left, bounds.bottom - bounds.top)
                        }

                        // ML Process
                        val similarity = mfn.compare(croppedBitmap1, croppedBitmap2)

                        if (similarity > MobileFaceNet.THRESHOLD) { // 0.8 by default, customize if you want
                            // faces match return true
                            // faces match, invoke the callback with true
                            callback(true)
                        } else {
                            // faces don't match return false
                            // faces don't match, invoke the callback with false
                            callback(false)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("ERROR", "No Face Detected On Image 2", e)
                        callback(false)
                    }

            }
            .addOnFailureListener { e ->
                Log.e("ERROR", "No Face Detected On Image 1", e)
                callback(false)
            }

        // how to wait for the face compare process so I can return the value? IDK so I'll use callback, feel free to fix my spagetthi code
        //return
    }



/*
    fun detectFace(bitmap: Bitmap):Bitmap? {
        var croppedBitmap: Bitmap? = null

        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE) // skips landmark mapping
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE) // skips facial expressions and other classification such as wink
            .build()

        val facedetector = FaceDetection.getClient(options)
        val image = InputImage.fromBitmap(bitmap, 0)

        // Initialize a thread-safe flag to indicate whether the face detection is complete
        val faceDetectionComplete = CountDownLatch(1)


        facedetector.process(image)
            .addOnSuccessListener { faces ->
                for(face in faces) {
                    val bounds = face.boundingBox
                    croppedBitmap = Bitmap.createBitmap(
                        bitmap,bounds.left,
                        bounds.top,
                        bounds.right - bounds.left,
                        bounds.bottom - bounds.top
                    )
                }
                Log.d("DEBUG", "Face Detected")
                // Signal that face detection is complete
                //faceDetectionComplete.countDown()

            }
            .addOnFailureListener {e ->
                Log.e("ERROR", "No Face Detected", e)
                // Signal that face detection is complete (even though it failed)
                //faceDetectionComplete.countDown()
            }




        // Wait for face detection to complete (timeout after 5 seconds)

        if (faceDetectionComplete.await(10, TimeUnit.SECONDS)) {
            return croppedBitmap
        } else {
            Log.e("ERROR", "Face detection timed out")
            return null
        }


    }
 */

}