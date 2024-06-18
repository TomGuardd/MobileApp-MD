package com.capstone.tomguard.di

import android.content.Context
import com.capstone.tomguard.data.UserRepository
import com.capstone.tomguard.data.network.ApiConfig
import com.capstone.tomguard.data.datastore.UserPreference
import com.capstone.tomguard.data.datastore.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, pref)
    }
}