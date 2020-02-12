package com.exuberant.salveo.data.entities

import com.google.gson.annotations.SerializedName

data class BiologicalData(
    @SerializedName("age")
    val age: String,
    @SerializedName("blood_pressure")
    val bloodPressure: String,
    @SerializedName("chest_pain")
    val chestPain: String,
    @SerializedName("cholesterol_level")
    val cholesterolLevel: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("max_heart_rate")
    val maxHeartRate: String
)