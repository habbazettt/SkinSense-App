package com.capstone.skinsense.data.response

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

// Alias untuk respons pengguna
typealias UserListResponse = ApiResponse<List<User>>
typealias UserDetailResponse = ApiResponse<User>
