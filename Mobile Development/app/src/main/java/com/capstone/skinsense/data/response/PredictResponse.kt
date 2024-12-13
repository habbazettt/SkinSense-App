package com.capstone.skinsense.data.response

import com.google.gson.annotations.SerializedName

data class Predict(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("confidenceScore")
	val confidenceScore: Float? = null,

	@field:SerializedName("suggestion")
	val suggestion: String? = null,

	@field:SerializedName("label")
	val label: String? = null
)

typealias PredictResponse = ApiResponse<Predict>
