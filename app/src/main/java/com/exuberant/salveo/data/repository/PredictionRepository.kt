package com.exuberant.salveo.data.repository

import androidx.lifecycle.LiveData
import com.exuberant.salveo.data.entities.BiologicalData
import com.exuberant.salveo.data.network.response.PredictionResponse

interface PredictionRepository {

    val prediction: LiveData<PredictionResponse>

    suspend fun getPrediction(
        biologicalData: BiologicalData
    )

}