package com.capstone.tomguard.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.tomguard.data.datastore.SettingPreference
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingPref: SettingPreference): ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return settingPref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            settingPref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getLocaleSetting(): LiveData<String> = settingPref.getLocaleSetting().asLiveData()

    fun saveLocaleSetting(localeName: String) {
        viewModelScope.launch {
            settingPref.saveLocaleSetting(localeName)
        }
    }
}