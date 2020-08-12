# QRCodeEditTextPreference

1. Add it in your root build.gradle at the end of repositories:

       repositories {
          maven { url 'https://jitpack.io' }
       }
2. Add this in your app's build.gradle

 	   implementation 'com.github.Lee827:QRCodeEditTextPreference:1.0.0'
     
3. Add this in your app's Manifest

       <activity android:name="com.elmolee.qrcodeedittext.QRCodeScannerActivity" />
     
4. To use this in XML File

       <com.elmolee.qrcodeedittext.QRCodeEditTextPreference
              android:defaultValue="defaultValue"
              android:key="@string/url_key"
              android:title="URL"
       />
       
5. Add the onDisplayPreferenceDialog function in PreferenceFragmentCompat

       override fun onDisplayPreferenceDialog(preference: Preference) {
           var dialogFragment: DialogFragment? = null
           if (preference is QRCodeEditTextPreference) {
               // Create a new instance of PreferenceDialogFragment with the key of the related
               dialogFragment = when (preference.key) {
                   getString(R.string.url_key) -> QRCodePreferenceDialogFragmentCompat.newInstance(preference.key, true) // check Valid URL, default = false
                   getString(R.string.key2) -> QRCodePreferenceDialogFragmentCompat.newInstance(preference.key)
                   else -> null
               }
           }

           if (dialogFragment != null) {
               dialogFragment.setTargetFragment(this, 0)
               dialogFragment.show(this.fragmentManager!!, null)
           } else {
               super.onDisplayPreferenceDialog(preference)
           }
       }

