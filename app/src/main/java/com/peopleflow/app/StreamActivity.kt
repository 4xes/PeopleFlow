package com.peopleflow.app

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.peopleflow.app.camera.CameraInteractor
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.Loggers.logcat
import io.fotoapparat.log.Loggers.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.parameter.selector.FocusModeSelectors.*
import io.fotoapparat.parameter.selector.LensPositionSelectors.back
import io.fotoapparat.parameter.selector.Selectors.firstAvailable
import io.fotoapparat.parameter.selector.SizeSelectors.biggestSize
import kotlinx.android.synthetic.main.activity_camera.*
import pub.devrel.easypermissions.EasyPermissions

import pub.devrel.easypermissions.AfterPermissionGranted

class StreamActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private var fotoapparat: Fotoapparat? = null
    private var interactor: CameraInteractor? = null

    private val TAG = this.javaClass.simpleName!!

    companion object {
        const val CAMERA_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        permissions()
    }

    @AfterPermissionGranted(CAMERA_REQUEST_CODE)
    private fun permissions() {
        val perms = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            startCamera()
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission),
                    CAMERA_REQUEST_CODE, *perms)
        }
    }

    private fun startCamera() {
        takeAShot.visibility = View.VISIBLE
        fotoapparat = Fotoapparat
                .with(this)
                .into(cameraView)           // view which will draw the camera preview
                .previewScaleType(ScaleType.CENTER_CROP)  // we want the preview to fill the view
                .photoSize(biggestSize()) // we want to have the biggest photo possible
                .lensPosition(back())       // we want back camera
                .focusMode(firstAvailable(  // (optional) use the first focus mode which is supported by device
                        continuousFocus(),
                        autoFocus(),        // in case if continuous focus is not available on device, auto focus will be used
                        fixed()             // if even auto focus is not available - fixed focus mode will be used
                ))
                .frameProcessor {
                    //Log.d(TAG, it.toString())
                }   // (optional) receives each frame from preview stream
                .logger(loggers(
                        logcat()
                ))
                .build()
        interactor = CameraInteractor(this, fotoapparat!!)

        takeAShot.setOnClickListener {
            interactor?.startSend()
        }
    }



    override fun onPermissionsGranted(requestCode: Int, list: List<String>) {
        Log.e(TAG, "granted: " + list.toString())
        if(list.contains(Manifest.permission.CAMERA) && list.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            startCamera()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, list: List<String>) {
        Log.e(TAG, list.toString())
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onStart() {
        super.onStart()
        if (fotoapparat?.isAvailable == true) {
            fotoapparat?.start()
        }
    }

    override fun onStop() {
        super.onStop()
        if (fotoapparat?.isAvailable == true) {
            fotoapparat?.stop()
        }
    }
}
