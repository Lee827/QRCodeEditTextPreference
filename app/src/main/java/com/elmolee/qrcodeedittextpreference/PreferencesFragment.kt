package com.elmolee.qrcodeedittextpreference

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import com.elmolee.qrcodeedittextpreferencelibrary.QRCodeEditTextPreference
import com.elmolee.qrcodeedittextpreferencelibrary.QRCodePreferenceDialogFragmentCompat
import com.takisoft.preferencex.PreferenceFragmentCompat

class PreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        // the following call results in a dialogue being shown

        var dialogFragment: DialogFragment? = null
        if (preference is QRCodeEditTextPreference) {
            // Create a new instance of PreferenceDialogFragment with the key of the related
            dialogFragment = when (preference.key) {
                getString(R.string.pref_websiteUrl_key) -> QRCodePreferenceDialogFragmentCompat.newInstance(preference.key, true)
                getString(R.string.pref_string_key) -> QRCodePreferenceDialogFragmentCompat.newInstance(preference.key)
                else -> null
            }
        }

        if (dialogFragment != null) {
            // The dialog was created (it was one of our custom Preferences), show the dialog for it
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(this.fragmentManager!!, null)
        } else {
            // Dialog creation could not be handled here. Try with the super method.
            super.onDisplayPreferenceDialog(preference)
        }
    }
}