package com.exuberant.salveo.data

import androidx.lifecycle.LiveData
import com.exuberant.salveo.data.entities.BiologicalData
import com.exuberant.salveo.data.network.response.PredictionResponse

interface PredictionDataSource {

    val prediction: LiveData<PredictionResponse>

    suspend fun fetchPrediction(
        biologicalData: BiologicalData
    )

}