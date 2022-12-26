package com.tridev.articles.repository

import com.tridev.articles.model.ArticleResponse
import com.tridev.articles.model.Source
import com.tridev.articles.remote.ArticleApiInterface
import retrofit2.Response
import javax.inject.Inject

class ArticleRepository@Inject constructor(private val apiInterface: ArticleApiInterface) {
    suspend fun getArticles():Response<ArticleResponse>{
       return apiInterface.getArticles()
    }
}