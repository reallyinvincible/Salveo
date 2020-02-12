package com.exuberant.salveo.ui.predict

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exuberant.salveo.data.entities.BiologicalData
import com.exuberant.salveo.data.network.response.PredictionResponse
import com.exuberant.salveo.data.repository.PredictionRepository

class PredictViewModel(
    private val predictionRepository: PredictionRepository
) : ViewModel() {

    val prediction = MutableLiveData<PredictionResponse>()

    init {
        predictionRepository.prediction.observeForever {
            prediction.postValue(it)
        }
    }

    suspend fun getPrediction(
        biologicalData: BiologicalData
    ) {
        predictionRepository.getPrediction(biologicalData)
    }

}