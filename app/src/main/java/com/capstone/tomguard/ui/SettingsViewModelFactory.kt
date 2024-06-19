package com.capstone.tomguard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.tomguard.data.datastore.SettingPreference
import com.capstone.tomguard.ui.profile.SettingsViewModel

class SettingsViewModelFactory(private val settingPref: SettingPreference
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingPref) as T
        }
        throw IllegalArgumentException("Invalid reference view model class: " + modelClass.name)
    }
}