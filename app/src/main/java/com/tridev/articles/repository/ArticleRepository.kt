package com.tridev.articles.repository

import androidx.lifecycle.LiveData
import com.tridev.articles.local.ArticleDao
import com.tridev.articles.model.Article
import com.tridev.articles.remote.ArticleApiInterface
import com.tridev.articles.utils.Resource
import com.tridev.articles.utils.safeApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val apiInterface: ArticleApiInterface,
    private val dao: ArticleDao,
) {

    suspend fun getArticles(): Resource<List<Article>> {
        val safeCall = safeApiCall(IO) { apiInterface.getArticles() }
        val response = safeCall.data
        val arrayList = ArrayList<Article>()

        CoroutineScope(IO).launch {
            arrayList.addAll(dao.getAllSyncArticles())
        }
        try {
            if (response?.isSuccessful == true) {
                response.body()?.let {
                    arrayList.addAll(it.articles)
                    return Resource.Success(arrayList)
                }
            } else {
                return Resource.Success(arrayList)
            }
        } catch (e: Exception) {
            return Resource.Success(arrayList)
        }
        return Resource.Success(arrayList)
    }

    suspend fun updateFavoriteArticle(id: Int) {
        dao.updateFavoriteArticle(id)
    }

    suspend fun insertFavoriteArticle(article: Article) {
        dao.insertFavoriteArticle(article = article)
    }

    fun getSavedArticle(): LiveData<List<Article>> {
        return dao.getAllFavoriteArticles()
    }

    suspend fun deleteArticles(article: Article) {
        dao.deleteArticle(article)
    }

    private suspend fun insertAllSyncArticles(list: List<Article>) {
        return dao.insertAllSyncArticles(list)
    }

    suspend fun syncArticlesWithWorkManager() {
        val safeCall = safeApiCall(IO) { apiInterface.getArticles() }
        val response = safeCall.data
        if (response?.isSuccessful == true) {
            response.body()?.let {
                insertAllSyncArticles(it.articles)
            }
        }

    }
}