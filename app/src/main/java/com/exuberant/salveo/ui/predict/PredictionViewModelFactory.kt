package com.exuberant.salveo.ui.predict

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exuberant.salveo.data.repository.PredictionRepository

@Suppress("UNCHECKED_CAST")
class PredictionViewModelFactory(
    private val predictionRepository: PredictionRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PredictViewModel(predictionRepository) as T
    }
}