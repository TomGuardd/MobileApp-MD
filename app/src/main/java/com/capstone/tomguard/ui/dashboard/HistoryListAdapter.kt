package com.capstone.tomguard.ui.dashboard

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.tomguard.R
import com.capstone.tomguard.data.model.DetectionsItem
import com.capstone.tomguard.databinding.LayoutItemPredictionBinding
import java.util.Locale

class HistoryListAdapter(
    private val historyList: List<DetectionsItem>
    ) : RecyclerView.Adapter<HistoryListAdapter.PredictionViewHolder>() {

    inner class PredictionViewHolder(private val binding: LayoutItemPredictionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(history: DetectionsItem) {
            with(binding) {
                Glide
                    .with(ivItemPhoto.context)
                    .load(history.imageUrl)
                    .placeholder(R.drawable.ic_place_holder)
                    .into(ivItemPhoto)
                tvItemResult.text = history.disease.name
                tvItemPercentage.text = "${history.confidenceLevel}%"

                val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val targetFormat = SimpleDateFormat("M/d/yyyy HH:mm", Locale.getDefault())
                val date = originalFormat.parse(history.uploadDate)
                val formattedDate = targetFormat.format(date)

                tvItemDate.text = formattedDate
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionViewHolder {
        val binding =
            LayoutItemPredictionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PredictionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: HistoryListAdapter.PredictionViewHolder, position: Int) {
        holder.bind(historyList[position])
    }
}