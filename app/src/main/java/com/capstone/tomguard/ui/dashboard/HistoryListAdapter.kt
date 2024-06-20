package com.capstone.tomguard.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.tomguard.R
import com.capstone.tomguard.data.model.DetectionsItem
import com.capstone.tomguard.databinding.LayoutItemPredictionBinding
import com.capstone.tomguard.ui.detail.DetailActivity
import java.util.Locale

class HistoryListAdapter :
    ListAdapter<DetectionsItem, HistoryListAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = LayoutItemPredictionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class HistoryViewHolder(private val binding: LayoutItemPredictionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DetectionsItem) {
            with(binding) {
                Glide
                    .with(binding.root)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.ic_place_holder)
                    .into(ivItemPhoto)
                tvDiseaseResult.text = item.disease.name
                tvItemPercentage.text = "${item.confidenceLevel}%"
                tvItemDesc.text = item.disease.description

                val originalFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val targetFormat = SimpleDateFormat("M/d/yyyy HH:mm", Locale.getDefault())
                val date = originalFormat.parse(item.uploadDate)
                val formattedDate = targetFormat.format(date)

                tvItemDate.text = formattedDate

                root.setOnClickListener {
                    val context = binding.root.context
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DETAIL_DATA, item)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(binding.ivItemPhoto, "prediction_image"),
                            Pair(binding.tvDiseaseResult, "result"),
                            Pair(binding.tvItemPercentage, "percentage"),
                            Pair(binding.tvItemDate, "date"),
                            Pair(binding.tvItemDesc, "description")
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DetectionsItem>() {
            override fun areItemsTheSame(
                oldItem: DetectionsItem,
                newItem: DetectionsItem
            ): Boolean {
                return oldItem.imageId == newItem.imageId
            }

            override fun areContentsTheSame(
                oldItem: DetectionsItem,
                newItem: DetectionsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
        const val DETAIL_DATA = "detail_data"
    }
}