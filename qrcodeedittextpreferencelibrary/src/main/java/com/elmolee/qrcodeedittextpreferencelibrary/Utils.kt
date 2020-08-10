package com.elmolee.qrcodeedittextpreferencelibrary

import android.content.Context
import android.hardware.Camera
import android.hardware.camera2.CameraManager
import android.os.Build
import android.util.Patterns
import android.webkit.URLUtil
import java.net.MalformedURLException

class Utils {
    companion object {
        var checkValidURL: Boolean = false

        fun isValidURL(urlString: String): Boolean {
            try {
                return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches()
            } catch (e: MalformedURLException) {
            }
            return false
        }

        fun getNumberOfCameras(context: Context): Int {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
                manager?.apply {
                    return cameraIdList.size
                }
            } else {
                return Camera.getNumberOfCameras()
            }
            return 0
        }
    }
}