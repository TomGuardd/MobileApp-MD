package com.capstone.tomguard.ui.predict

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.tomguard.data.UserRepository
import com.capstone.tomguard.data.model.UserModel
import java.io.File

class UploadViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun uploadStory(token: String, file: File) =
        userRepository.uploadImage(token, file)

}