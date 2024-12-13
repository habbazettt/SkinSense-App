package com.capstone.skinsense.ui.scan

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture

class ScanViewModel : ViewModel() {
    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> get() = _currentImageUri

    fun setImageUri(uri: Uri?) {
        _currentImageUri.value = uri
    }

    fun clearImageUri() {
        _currentImageUri.value = null
    }
}
