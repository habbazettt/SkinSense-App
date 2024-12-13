package com.capstone.skinsense.ui.save

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.capstone.skinsense.R
import com.capstone.skinsense.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = DetailFragmentArgs.fromBundle(requireArguments())
        binding.resultTextView.text = args.result
        binding.confidenceScoreTextView.text = String.format("%.2f%%", args.confidenceScore)
        val formattedSuggestion = args.suggestion.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
        binding.suggestionTextView.text = formattedSuggestion

        Glide.with(requireContext())
            .load(args.imageUri)
            .placeholder(R.drawable.ic_launcher_foreground) // Gambar sementara
            .error(R.drawable.ic_launcher_background) // Gambar jika ada error
            .into(binding.imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
