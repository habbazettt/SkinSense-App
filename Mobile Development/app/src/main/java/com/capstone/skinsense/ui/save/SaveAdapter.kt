package com.capstone.skinsense.ui.save

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.skinsense.R
import com.capstone.skinsense.databinding.ItemPredictionResultBinding
import com.capstone.skinsense.data.local.PredictionResult
import okhttp3.internal.format

class SaveAdapter(private val results: MutableList<PredictionResult>,
                  private val onItemClick: (PredictionResult) -> Unit) :
    RecyclerView.Adapter<SaveAdapter.SaveViewHolder>() {

    inner class SaveViewHolder(private val binding: ItemPredictionResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: PredictionResult) {
//            val uri = Uri.parse(result.imageUri) // Ubah string menjadi Uri
            Glide.with(binding.root.context)
                .load(Uri.parse(result.imageUri))
                .placeholder(R.drawable.ic_launcher_foreground) // Gambar sementara
                .error(R.drawable.ic_launcher_background) // Gambar jika ada error
                .into(binding.imagePreview) // ImageView untuk menampilkan gambar
            binding.resultTextView.text = result.result
            binding.confidenceScoreTextView.text = String.format("%.2f%%", result.confidenceScore)
            if (result.confidenceScore!! >= 50.0) {
                binding.statusTextView.text = "Positive"
            } else {
                binding.statusTextView.text = "Negative"
            }
//            val formattedSuggestion = result.suggestion.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
//            binding.suggestionTextView.text = formattedSuggestion

            binding.root.setOnClickListener {
                onItemClick(result) // Panggil callback dengan item yang diklik
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveViewHolder {
        val binding = ItemPredictionResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SaveViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaveViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int = results.size

    fun updateData(newResults: List<PredictionResult>) {
        results.clear()
        results.addAll(newResults)
        notifyDataSetChanged()
    }
}