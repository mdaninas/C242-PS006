package com.example.glusity.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("userId")
	val userId: Int
)