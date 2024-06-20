package com.capstone.tomguard.ui.detail

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.capstone.tomguard.R
import com.capstone.tomguard.data.model.DetectionsItem
import com.capstone.tomguard.databinding.ActivityDetailBinding
import com.capstone.tomguard.ui.dashboard.HistoryListAdapter
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var detailHistory: DetectionsItem

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.title_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            detailHistory = intent.getParcelableExtra(HistoryListAdapter.DETAIL_DATA, DetectionsItem::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            detailHistory = intent.getParcelableExtra(HistoryListAdapter.DETAIL_DATA)!!
        }

        with(binding) {
            Glide
                .with(binding.root)
                .load(detailHistory.imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .into(imgItemPhotoDetail)
            tvDiseaseResult.text = detailHistory.disease.name
            tvItemPercentageDetail.text = "${detailHistory.confidenceLevel}%"
            tvItemDescriptionDetail.text = detailHistory.disease.description

            val originalFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val targetFormat = SimpleDateFormat("M/d/yyyy HH:mm", Locale.getDefault())
            val date = originalFormat.parse(detailHistory.uploadDate)
            val formattedDate = targetFormat.format(date)

            tvItemDate.text = formattedDate

            tvItemRecomendation.text = detailHistory.disease.recommendations.toString()
        }
    }
}