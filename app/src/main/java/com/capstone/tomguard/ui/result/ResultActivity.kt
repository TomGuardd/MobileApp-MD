package com.capstone.tomguard.ui.result

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.capstone.tomguard.R
import com.capstone.tomguard.databinding.ActivityResultBinding
import com.capstone.tomguard.ui.predict.utils.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class ResultActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener {

    private lateinit var binding: ActivityResultBinding

    // NOT YET USED
    //    private val viewModel by viewModels<ResultViewModel> {
    //        ViewModelFactory.getInstance(this)
    //    }

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.title_prediction_result)
        initImageClassification()
    }

    private fun initImageClassification() {
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        Log.d("Debug", "initImageClassification: imageUri $imageUri")
        imageUri?.let {
            binding.ivResult.setImageURI(it)

            imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = this
            )
            imageClassifierHelper.classifyStaticImage(it)
        }
    }

    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
//        results?.let {
//            showResults(it)
//            Log.d("Debug", "onResult: inferenceTime: $inferenceTime ms")
//            Log.d("Debug", "onResult: List<Classifications>: $it")
//        } ?: run {
//            showToast("No result found")
//        }

        results?.let { it ->
            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                println(it)
                val sortedCategories =
                    it[0].categories.sortedByDescending { it?.score }
                val displayResult =
                    sortedCategories.joinToString("\n") {
                        "${it.label} " + NumberFormat.getPercentInstance()
                            .format(it.score).trim()
                    }
                binding.tvResult.text = displayResult
                binding.tvInference.text = "$inferenceTime ms"
            } else {
                binding.tvResult.text = ""
                binding.tvInference.text = ""
            }
        }
    }

    private fun showResults(results: List<Classifications>) {
        val result = results[0]
        val title = result.categories[0].label
        val confidence = result.categories[0].score
        val prediction = "$title: ${(confidence * 100).toInt()}%"

        binding.tvResult.text = prediction
    }

    override fun onError(error: String) {
        showToast(error)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}