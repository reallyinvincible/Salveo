package com.exuberant.salveo.data.network

import com.exuberant.salveo.BuildConfig
import com.exuberant.salveo.data.network.response.PredictionResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface AwsPredictionService {

    @POST("prod/")
    suspend fun getPrediction(
        @Query("age") age: String,
        @Query("gender") gender: String,
        @Query("chest_pain") chestPain: String,
        @Query("blood_pressure") bloodPressure: String,
        @Query("cholesterol_level") cholesterolLevel: String,
        @Query("max_heart_rate") maxHeartRate: String
    ) : Response<PredictionResponse>

    companion object {
        operator fun invoke(): AwsPredictionService {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AwsPredictionService::class.java)
        }
    }

}