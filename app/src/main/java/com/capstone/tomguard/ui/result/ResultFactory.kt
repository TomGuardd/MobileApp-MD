package com.capstone.tomguard.ui.result

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ResultFactory private constructor(private val mApplication: Application) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ResultFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ResultFactory {
            if (INSTANCE == null) {
                synchronized(ResultFactory::class.java) {
                    INSTANCE = ResultFactory(application)
                }
            }
            return INSTANCE as ResultFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}