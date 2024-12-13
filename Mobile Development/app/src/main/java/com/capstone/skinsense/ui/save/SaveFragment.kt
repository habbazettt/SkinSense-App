package com.capstone.skinsense.ui.save

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.skinsense.databinding.FragmentSaveBinding
import com.capstone.skinsense.data.local.AppDatabase
import com.capstone.skinsense.data.local.PredictionResult
import kotlinx.coroutines.launch

class SaveFragment : Fragment() {

    private var _binding: FragmentSaveBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SaveAdapter

    // Request permission launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            // Inform user if permission is denied
            Toast.makeText(requireContext(), "Permission denied to access media.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check for permission
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        // Setup RecyclerView
        adapter = SaveAdapter(mutableListOf()) { prediction ->
            navigateToDetailFragment(prediction)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SaveFragment.adapter
        }   

        // Fetch data from database
        val database = AppDatabase.getInstance(requireContext())
        val dao = database.predictionResultDao()

        dao.getAllResults().observe(viewLifecycleOwner) { results ->
            adapter.updateData(results)
        }
    }

    private fun navigateToDetailFragment(prediction: PredictionResult) {
        val action = SaveFragmentDirections.actionSaveFragmentToDetailFragment(
            prediction.imageUri,
            prediction.result,
            prediction.suggestion,
            prediction.confidenceScore!!
        )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}