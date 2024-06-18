package com.capstone.tomguard.data.model

import com.google.gson.annotations.SerializedName

data class UploadResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ArticlesItem(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class Data(

	@field:SerializedName("condition")
	val condition: String? = null,

	@field:SerializedName("confidence")
	val confidence: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("actionRequired")
	val actionRequired: Boolean? = null,

	@field:SerializedName("recommendations")
	val recommendations: List<String?>? = null,

	@field:SerializedName("articles")
	val articles: List<ArticlesItem?>? = null
)
