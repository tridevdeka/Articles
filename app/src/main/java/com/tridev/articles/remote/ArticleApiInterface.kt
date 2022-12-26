package com.tridev.articles.remote

import com.tridev.articles.model.ArticleResponse
import com.tridev.articles.utils.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApiInterface {

    //https://newsapi.org/v2/top-headlines?sources=techcrunch&apiKey=333f53686b2447149acc0e249c76a70d

    @GET("top-headlines")
    suspend fun getArticles(
        @Query("sources") sourceName: String = "techcrunch",
        @Query("apiKey") apiKey: String = ApiConstants.API_KEY,
    ): Response<ArticleResponse>
}