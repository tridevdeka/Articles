package com.tridev.articles.application

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.tridev.articles.workmanager.ArticleWorker
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workManager: WorkManager

    private val constraint by lazy {
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HiltWorkerFactoryEntryPoint {
        fun workerFactory(): HiltWorkerFactory
    }


    private val workRequest by lazy {
        PeriodicWorkRequest.Builder(ArticleWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraint)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        workManager.enqueueUniquePeriodicWork("unique_periodic_work",
            ExistingPeriodicWorkPolicy.KEEP, workRequest)
//        workManager.beginUniqueWork("unique_work",ExistingWorkPolicy.REPLACE,workRequest).enqueue()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setExecutor(Dispatchers.Default.asExecutor())
            .setWorkerFactory(EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java)
                .workerFactory())
            .setTaskExecutor(Dispatchers.Default.asExecutor())
            .setMaxSchedulerLimit(50)
            .build()
    }

}