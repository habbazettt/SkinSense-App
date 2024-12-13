package com.capstone.skinsense.data.response

import com.google.gson.annotations.SerializedName

data class Skin(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("suggestion")
	val suggestion: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

// Alias untuk respons pengguna
typealias SkinListResponse = ApiResponse<List<Skin>>
typealias SkinDetailResponse = ApiResponse<Skin>