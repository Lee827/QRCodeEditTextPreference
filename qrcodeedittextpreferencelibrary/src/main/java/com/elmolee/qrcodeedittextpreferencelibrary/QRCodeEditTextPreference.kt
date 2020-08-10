package com.elmolee.qrcodeedittextpreferencelibrary

import android.content.Context
import android.content.res.TypedArray
import androidx.preference.DialogPreference
import android.util.AttributeSet

class QRCodeEditTextPreference : DialogPreference {
    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private val mDialogLayoutResId = R.layout.preferences_qrcode_edittext

    override fun setSummary(summary: CharSequence?) {
        super.setSummary(summary)
        persistString(summary.toString())
    }

    override fun getDialogLayoutResource(): Int {
        return mDialogLayoutResId
    }

    override fun onGetDefaultValue(a: TypedArray?, index: Int): Any {
        return a!!.getString(index).toString()
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        val value = if (restorePersistedValue) {
            if (defaultValue == null) {
                getPersistedString("")
            } else {
                getPersistedString(defaultValue.toString())
            }
        } else {
            defaultValue.toString()
        }
        summary = value
    }
}
