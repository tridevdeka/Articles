package com.tridev.articles.remote

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            val token = "token"
            addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(request.build())
    }
}