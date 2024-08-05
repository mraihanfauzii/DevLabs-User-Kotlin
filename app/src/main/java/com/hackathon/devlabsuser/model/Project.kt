package com.hackathon.devlabsuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectRequest(
    @field:SerializedName("project_name")
    val projectName: String,
    val name: String,
    val lat: Double,
    val long: Double,
    val type: String,
    @field:SerializedName("buildingtype")
    val buildingType: String,
    val area: Int,
    @field:SerializedName("numperson")
    val numPerson: Int,
    @field:SerializedName("numroom")
    val numRoom: Int,
    @field:SerializedName("numbathroom")
    val numBathroom: Int,
    @field:SerializedName("numfloor")
    val numFloor: Int,
    val theme: String,
    val budget: String,
    @field:SerializedName("buildingtime")
    val buildingTime: String,
    val notes: String
) : Parcelable

data class ProjectResponse(
    val status: String,
    val message: String,
    val data: Any?
)