package com.exuberant.salveo

import android.app.Application
import com.exuberant.salveo.data.PredictionDataSource
import com.exuberant.salveo.data.PredictionDataSourceImpl
import com.exuberant.salveo.data.network.AwsPredictionService
import com.exuberant.salveo.data.repository.PredictionRepository
import com.exuberant.salveo.data.repository.PredictionRepositoryImpl
import com.exuberant.salveo.ui.predict.PredictionViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class PredictionApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy{
        import(androidXModule(this@PredictionApplication))
        bind() from singleton { AwsPredictionService() }
        bind<PredictionDataSource>() with singleton { PredictionDataSourceImpl(instance()) }
        bind<PredictionRepository>() with singleton { PredictionRepositoryImpl(instance()) }
        bind() from provider { PredictionViewModelFactory(instance()) }
    }
}