package com.exuberant.salveo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exuberant.salveo.data.entities.BiologicalData
import com.exuberant.salveo.data.network.AwsPredictionService
import com.exuberant.salveo.data.network.response.PredictionResponse

class PredictionDataSourceImpl(
    private val awsPredictionService: AwsPredictionService
) : PredictionDataSource {

    private val _downloadedPrediction = MutableLiveData<PredictionResponse>()

    override val prediction: LiveData<PredictionResponse>
        get() = _downloadedPrediction

    override suspend fun fetchPrediction(
        biologicalData: BiologicalData
    ) {
        val fetchedPrediction = awsPredictionService
            .getPrediction(
                biologicalData.age,
                biologicalData.gender,
                biologicalData.chestPain,
                biologicalData.bloodPressure,
                biologicalData.cholesterolLevel,
                biologicalData.maxHeartRate
            )
        _downloadedPrediction.postValue(fetchedPrediction.body())
    }
}