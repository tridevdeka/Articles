package com.tridev.articles.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tridev.articles.repository.ArticleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


@HiltWorker
class ArticleWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted parameters: WorkerParameters,
    private val repository: ArticleRepository,
) : CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        Log.d("WORKMANAGER:", "CALLED")
        CoroutineScope(IO).launch {
                repository.syncArticlesWithWorkManager()
        }
        return Result.success()
    }


}
