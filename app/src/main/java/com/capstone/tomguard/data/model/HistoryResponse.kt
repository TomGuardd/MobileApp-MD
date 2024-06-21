package com.capstone.tomguard.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryResponse(

    @field:SerializedName("data")
    val data: HistoryData,

    @field:SerializedName("message")
    val message: String
) : Parcelable

@Parcelize
data class HistoryData(

    @field:SerializedName("totalItems")
    val totalItems: Int,

    @field:SerializedName("totalPages")
    val totalPages: Int,

    @field:SerializedName("currentPage")
    val currentPage: Int,

    @field:SerializedName("detections")
    val detections: List<DetectionsItem>
) : Parcelable


@Parcelize
data class DetectionsItem(

    @field:SerializedName("confidenceLevel")
    val confidenceLevel: Float,

    @field:SerializedName("imageId")
    val imageId: String,

    @field:SerializedName("disease")
    val disease: Disease,

    @field:SerializedName("uploadDate")
    val uploadDate: String,

    @field:SerializedName("imageUrl")
    val imageUrl: String,

    @field:SerializedName("actionRequired")
    val actionRequired: Boolean
) : Parcelable

@Parcelize
data class Disease(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("recommendations")
    val recommendations: List<String>,

    @field:SerializedName("articles")
    val articles: List<HistoryArticlesItem>
) : Parcelable

@Parcelize
data class HistoryArticlesItem(

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("content")
    val content: String,

    @field:SerializedName("url")
    val url: String
) : Parcelable