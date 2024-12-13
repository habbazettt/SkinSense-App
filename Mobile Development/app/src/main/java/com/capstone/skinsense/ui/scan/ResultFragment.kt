package com.capstone.skinsense.ui.scan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.capstone.skinsense.R
import com.capstone.skinsense.data.local.AppDatabase
import com.capstone.skinsense.data.local.PredictionResult
import com.capstone.skinsense.databinding.FragmentResultBinding
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments?.let {
            ResultFragmentArgs.fromBundle(it)
        }

        // Set preview image
        val imageUri = args?.imageUri?.toUri()
        binding.previewImageView.setImageURI(imageUri)

        // Set prediction result
        val resultText = args?.resultText
        binding.resultTextView.text = resultText

        // Set confidence score
        val confidenceScore = args?.confidenceScore
        binding.confidenceScoreTextView.text = String.format("%.2f%%", confidenceScore)

        // Set suggestion
        val suggestionText = args?.suggestionText
        val formattedSuggestion = suggestionText?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
        binding.suggestionTextView.text = formattedSuggestion

        // Handle klik tombol Save
        binding.saveButton.setOnClickListener {
            saveResultToLocalDatabase(imageUri.toString(), resultText, suggestionText, confidenceScore)
        }

        // Handle klik tombol Scan Again
        binding.rescanButton.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_navigation_scan)
        }

        binding.homeButton.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_navigation_home)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveResultToLocalDatabase(imageUri: String?, result: String?, suggestion: String?, confidenceScore: Float?) {
        if (imageUri != null && result != null && suggestion != null && confidenceScore != null) {
            val database = AppDatabase.getInstance(requireContext())
            val predictionResult = PredictionResult(
                imageUri = imageUri,
                result = result,
                suggestion = suggestion,
                confidenceScore = confidenceScore
            )

            lifecycleScope.launch {
                database.predictionResultDao().insert(predictionResult)
                Toast.makeText(requireContext(), "Prediction saved successfully!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Failed to save. Data is incomplete.", Toast.LENGTH_SHORT).show()
        }
    }

}
