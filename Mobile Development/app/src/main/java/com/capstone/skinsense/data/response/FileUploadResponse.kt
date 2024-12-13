package com.capstone.skinsense.data.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class Data(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("result")
    val result: String? = null,

    @field:SerializedName("confidenceScore")
    val confidenceScore: Any? = null,

    @field:SerializedName("isAboveThreshold")
    val isAboveThreshold: Boolean? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null
)
