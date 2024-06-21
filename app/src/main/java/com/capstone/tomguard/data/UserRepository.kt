package com.capstone.tomguard.data

import android.util.Log
import androidx.lifecycle.liveData
import com.capstone.tomguard.data.network.ApiService
import com.capstone.tomguard.data.model.UserModel
import com.capstone.tomguard.data.datastore.UserPreference
import com.capstone.tomguard.data.model.LoginResponse
import com.capstone.tomguard.data.model.ProfileResponse
import com.capstone.tomguard.data.model.UploadResponse
import com.capstone.tomguard.data.model.UserResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.HttpException
import java.io.File

class UserRepository(private val apiService: ApiService, private val userPreference: UserPreference) {

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.login(email, password)
            val userModel = UserModel(
                email = email,
                token = successResponse.token,
                isLogin = true
            )
            saveSession(userModel)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    fun register(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse: UserResponse = apiService.registerUser(name, email, password)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UserResponse::class.java)
            emit(Result.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun getProfile(token: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getProfile("Bearer $token")
            Log.d("Debug", "UserRepository token : $token")
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ProfileResponse::class.java)
            emit(errorResponse.message?.let { Result.Error(it) })
        }
    }

    fun uploadImage(token: String, imageFile: File) = liveData {
        emit(Result.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "picture",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse =
                apiService.uploadImage("Bearer $token", multipartBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(
                errorBody, UploadResponse
                ::class.java
            )
            emit(errorResponse.message?.let { Result.Error(it) })
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
