package com.tridev.articles.repository

import androidx.lifecycle.LiveData
import com.tridev.articles.local.ArticleDao
import com.tridev.articles.model.Article
import com.tridev.articles.model.ArticleResponse
import com.tridev.articles.remote.ArticleApiInterface
import retrofit2.Response
import javax.inject.Inject

class ArticleRepository@Inject constructor(private val apiInterface: ArticleApiInterface,private val dao:ArticleDao) {

    suspend fun getArticles():Response<ArticleResponse>{
       return apiInterface.getArticles()
    }

    suspend fun insert(article: Article){
        dao.insert(article)
    }

    fun getSavedArticle(): LiveData<List<Article>> {
        return dao.getAllArticles()
    }

    suspend fun deleteArticles(article: Article) {
        dao.deleteArticle(article)
    }
}