package com.exuberant.salveo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exuberant.salveo.data.PredictionDataSource
import com.exuberant.salveo.data.entities.BiologicalData
import com.exuberant.salveo.data.network.response.PredictionResponse

class PredictionRepositoryImpl(
    private val predictionDataSource: PredictionDataSource
) : PredictionRepository {

    private val _fetchedPrediction = MutableLiveData<PredictionResponse>()

    init {
        predictionDataSource.prediction.observeForever {
            _fetchedPrediction.postValue(it)
        }
    }

    override val prediction: LiveData<PredictionResponse>
        get() = _fetchedPrediction

    override suspend fun getPrediction(
        biologicalData: BiologicalData
    ) {
        predictionDataSource.fetchPrediction(biologicalData)
    }


}