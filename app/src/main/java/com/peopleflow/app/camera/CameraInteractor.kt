package com.peopleflow.app.camera

import android.content.Context
import android.os.Environment
import android.util.Log
import io.fotoapparat.Fotoapparat
import io.reactivex.Observable
import okhttp3.*
import java.io.File
import java.util.concurrent.TimeUnit

import java.util.*


class CameraInteractor (private val context: Context, private val fotoapparat: Fotoapparat) {

    private val TAG = this.javaClass.simpleName!!
    val address = "http://192.168.1.2/upload"

    fun startSend() {
        Observable.timer(300, TimeUnit.MILLISECONDS).map {
            val output = videoFile()
            Log.e(TAG, output.toString())
            takeAPhoto(output)
            output
        }.map {
            //sendAPhoto(it)
        }
                .subscribe {

                }
    }

    private fun takeAPhoto(file: File) {
//        Completable.create({ source ->
//            val photoResult = fotoapparat.takePicture()
//            //photoResult.saveToFile(file)
//            photoResult.toPendingResult().whenAvailable {
//                Log.e(TAG, it.toString())
//            }
//        }).toSingle { true }.blockingGet()
        val photoResult = fotoapparat.takePicture()
        photoResult.saveToFile(file)
    }



    private fun sendAPhoto(sourceFile: File) {
        try {

            val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "frame.jpg", RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                    .build()

            val request = Request.Builder()
                    .url(address)
                    .post(requestBody)
                    .build()

            val client = OkHttpClient()
            client.newCall(request).execute()

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Other Error: " + e.localizedMessage)
        } finally {
            try {
                sourceFile.delete()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "Error: " + e.localizedMessage)
            }
        }

    }

    fun videoTempDirectory(): File {
        return File("${Environment.getExternalStorageState()} + /camera")
    }

    private fun videoDirectory(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
    }

    private fun randomUUID(): String {
        return UUID.randomUUID().toString()
    }

    fun videoFile(): File {
        val storageDir = videoDirectory()


        return File.createTempFile(
                randomUUID(),
                ".jpg",
                storageDir)
    }


    companion object {
        val MEDIA_TYPE_PNG = MediaType.parse("image/jpg")
    }

}
