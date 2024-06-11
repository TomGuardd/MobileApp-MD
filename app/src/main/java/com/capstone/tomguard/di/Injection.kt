package com.capstone.tomguard.di

import android.content.Context
import com.capstone.tomguard.data.Repository
import com.capstone.tomguard.data.api.ApiConfig
import com.capstone.tomguard.data.pref.UserPreference
import com.capstone.tomguard.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(context.dataStore)
        return Repository.getInstance(apiService, pref)
    }
}