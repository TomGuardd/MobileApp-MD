package com.capstone.tomguard.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Prediction")
@Parcelize
data class Prediction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "imageUri")
    var imageUri: String? = null,
    @ColumnInfo(name = "result")
    var result: String? = null,
    @ColumnInfo(name = "inferenceTime")
    var inferenceTime: Long? = null

) : Parcelable