package com.elmolee.qrcodeedittextpreferencelibrary

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Patterns
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.net.MalformedURLException

class QRCodeScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private var scannerView: ZXingScannerView? = null

    val requiredPermissions: Array<String>
        inline get() = packageManager
            .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            .requestedPermissions

    private val areRequiredPermissionsHaveBeenGranted: Boolean
        inline get() = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> requiredPermissions.map {
                ContextCompat.checkSelfPermission(
                    this,
                    it
                )
            }.none { it != PackageManager.PERMISSION_GRANTED }
            else -> true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
        requestPermissionForExternalStorage()
    }

    override fun onResume() {
        super.onResume()
        startScannerView(true)
    }

    override fun onPause() {
        super.onPause()
        startScannerView(false)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (areRequiredPermissionsHaveBeenGranted) {
            false -> {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
            }
            else -> requestPermissionForExternalStorage()
        }
    }

    override fun handleResult(rawResult: Result) {
        val isURL = isValidURL(rawResult.text)
        if (isURL) {
            val intent = Intent()
            intent.putExtra(Intent.EXTRA_TEXT, rawResult.text)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            scannerView?.apply {
                Thread.sleep(500)
                resumeCameraPreview(this@QRCodeScannerActivity)
            }
            Toast.makeText(this,  getString(R.string.toast_invalid_url), Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestPermissionForExternalStorage() {
        when (areRequiredPermissionsHaveBeenGranted) {
            false -> {
                ActivityCompat.requestPermissions(this, requiredPermissions, 1)
            }
        }
    }

    private fun startScannerView(start: Boolean) {
        scannerView?.apply {
            when (start) {
                true -> {
                    setResultHandler(this@QRCodeScannerActivity)
                    setAutoFocus(true)
                    setAspectTolerance(0.5f)
                    startCamera()
                }
                else -> {
                    stopCamera()
                }
            }
        }
    }

    private fun isValidURL(urlString: String): Boolean {
        try {
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches()
        } catch (e: MalformedURLException) {
        }
        return false
    }
}