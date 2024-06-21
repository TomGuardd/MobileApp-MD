package com.capstone.tomguard.data.model

data class Users(
	val name: String,
	val email: String,
	val password: String
)

data class UserResponse(
	val success: Boolean,
	val message: String
)
