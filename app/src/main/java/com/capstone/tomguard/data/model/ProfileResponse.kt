package com.capstone.tomguard.data.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("profilePictureUrl")
    val profilePictureUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("message")
    val message: String? = null

)