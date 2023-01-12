package com.tridev.articles.remote

import com.tridev.articles.BuildConfig
import com.tridev.articles.model.ArticleResponse
import com.tridev.articles.utils.ApiConstants
import com.tridev.articles.utils.ApiConstants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApiInterface {

    @GET("top-headlines")
    suspend fun getArticles(
        @Query("country") countryCode: String = "us",
        @Query("page") pageNumber: Int = 1,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY,
    ): Response<ArticleResponse>
}