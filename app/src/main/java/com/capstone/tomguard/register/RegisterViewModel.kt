package com.capstone.tomguard.register

import androidx.lifecycle.ViewModel
import com.capstone.tomguard.data.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) =
        repository.register(name, email, password)
}
