package com.example.a20.data.retrofit

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataModel(
    @SerializedName("userId")
    val userId:String,
    @SerializedName("id")
    val id:String,
    @SerializedName("title")
    val title:String,
    @SerializedName("body")
    val body:String): Parcelable

data class DataModelsList(val listOfDataModel: List<DataModel>)