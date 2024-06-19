package com.capstone.tomguard.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.tomguard.data.UserRepository
import com.capstone.tomguard.data.model.DetectionsItem
import com.capstone.tomguard.data.model.UserModel
import com.capstone.tomguard.data.Result

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _historyList = MediatorLiveData<Result<List<DetectionsItem>>>()
    val historyList: LiveData<Result<List<DetectionsItem>>> = _historyList

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun getHistories(token: String) {
        val liveData = userRepository.getHistories(token)
        _historyList.addSource(liveData) { result ->
            _historyList.value = result
        }
    }

}