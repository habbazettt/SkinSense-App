package com.capstone.skinsense.data.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("status") val status: Int,
    @SerializedName("data") val data: T,
    @SerializedName("message") val message: String
)
