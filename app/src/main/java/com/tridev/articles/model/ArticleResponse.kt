package com.tridev.articles.model

data class ArticleResponse(
    val articles: ArrayList<Article>,
    val status: String,
    val totalResults: Int
)