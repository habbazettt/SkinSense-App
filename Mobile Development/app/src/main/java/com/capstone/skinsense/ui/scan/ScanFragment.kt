package com.capstone.skinsense.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.capstone.skinsense.R
import com.capstone.skinsense.data.TokenPreferences
import com.capstone.skinsense.data.api.ApiConfig
import com.capstone.skinsense.data.response.FileUploadResponse
import com.capstone.skinsense.databinding.FragmentScanBinding
import com.capstone.skinsense.util.getImageUri
import com.capstone.skinsense.util.reduceFileImage
import com.capstone.skinsense.util.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ScanViewModel
    private lateinit var tokenPreferences: TokenPreferences

    private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initailize TokenPreferences
        tokenPreferences = TokenPreferences(requireContext())

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[ScanViewModel::class.java]

        // Observe currentImageUri from ViewModel
        viewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                showImage(uri)
            }
        }

        binding.galleryButton.setOnClickListener {
            // Check for permission
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startGallery()
            }else{
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        binding.cameraButton.setOnClickListener {
            // Check for permission
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startCamera()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
        binding.uploadButton.setOnClickListener { uploadImage("1") } //just dummy params, change to get userID from datastore
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setImageUri(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        val imageUri = getImageUri(requireContext())
        viewModel.setImageUri(imageUri)
        launcherIntentCamera.launch(imageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            viewModel.currentImageUri.value?.let { showImage(it) }
        }
    }

    private fun showImage(uri: Uri) {
        Log.d("Image URI", "showImage: $uri")
        binding.previewImageView.setImageURI(uri)
    }

    private fun uploadImage(userId: String) {
        tokenPreferences.token.asLiveData().observe(viewLifecycleOwner) { token ->
            viewModel.currentImageUri.value?.let { uri ->
                val imageFile = uriToFile(uri,  requireContext()).reduceFileImage()
                showLoading(true)
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "image", // key untuk file gambar
                    imageFile.name,
                    requestImageFile
                )
                //User ID diubah menjadi RequestBody
                val userIdBody = userId.toRequestBody("text/plain".toMediaType())

                lifecycleScope.launch {
                    try {
                        val apiService = ApiConfig.getApiService()
                        val successResponse = apiService.uploadImage(multipartBody, userIdBody)

                        successResponse.data?.let { data ->
//                            val resultText = String.format("%s with %.2f%%", data.result, data.confidenceScore)
                            val resultText = data.result?: "No Result Available"
                            val confidenceScore = data.confidenceScore?: 0.0
                            val suggestionText = data.suggestion?: "No Suggestion Availabe"
                            // Navigate to ResultFragment with arguments
                            val action = ScanFragmentDirections.scanResultFragment(
                                uri.toString(),
                                resultText,
                                suggestionText,
                                confidenceScore.toFloat()
                            )
                            findNavController().navigate(action)
                        }
                        showLoading(false)
                    } catch (e: HttpException) {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                        showToast(errorResponse.message.toString())
                        showLoading(false)
                    }
                }
            } ?: showToast(getString(R.string.empty_image_warning))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}