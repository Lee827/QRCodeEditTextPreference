package com.elmolee.qrcodeedittextpreferencelibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.preference.PreferenceDialogFragmentCompat
import kotlinx.android.synthetic.main.preferences_qrcode_edittext.view.*
import java.lang.ref.WeakReference

class QRCodePreferenceDialogFragmentCompat : PreferenceDialogFragmentCompat() {

    private var edittext: EditText? = null

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        edittext = view.editText_qrcode
        if (preference is QRCodeEditTextPreference) {
            view.editText_qrcode.setText(preference.summary)
        }

        view.imageButton_qrcode.setOnClickListener {
            when (getNumberOfCameras() > 0) {
                false -> Toast.makeText(this.context,  getString(R.string.toast_camera_error), Toast.LENGTH_SHORT).show()
                true -> {
                    val activityWeakRef = WeakReference(this)
                    activityWeakRef.get()?.apply {
                        var intent = Intent(this.context, QRCodeScannerActivity::class.java)
                        startActivityForResult(intent, 0)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                // get String data from Intent
                val returnString = data?.let { it.getStringExtra(Intent.EXTRA_TEXT) } ?: return
                edittext!!.setText(returnString)
            }
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            if (preference is QRCodeEditTextPreference) {
                if (preference.callChangeListener(edittext!!.text.toString())) {
                    // Save the value
                    preference.summary = edittext!!.text.toString()
                }
            }
        }
    }

    private fun getNumberOfCameras(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val manager = context!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
            manager?.apply {
                return cameraIdList.size
            }
        } else {
            return Camera.getNumberOfCameras()
        }
        return 0
    }

    companion object {
        fun newInstance(key: String): QRCodePreferenceDialogFragmentCompat {
            val fragment = QRCodePreferenceDialogFragmentCompat()
            val b = Bundle(1)
            b.putString(ARG_KEY, key)
            fragment.arguments = b

            return fragment
        }
    }
}