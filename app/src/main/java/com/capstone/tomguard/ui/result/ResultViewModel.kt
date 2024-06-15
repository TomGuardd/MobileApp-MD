package com.capstone.tomguard.ui.result

import android.app.Application
import androidx.lifecycle.ViewModel
import com.capstone.tomguard.data.PredictionRepository
import com.capstone.tomguard.data.database.Prediction

class ResultViewModel(application: Application) : ViewModel() {
    private val mPredictionRepository: PredictionRepository = PredictionRepository(application)

    var currentPrediction: Prediction? = null

    fun insert(prediction: Prediction) {
        mPredictionRepository.insert(prediction)
    }

}