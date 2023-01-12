package com.tridev.articles.utils

import com.tridev.articles.utils.ApiConstants.NETWORK_ERROR_UNKNOWN
import com.tridev.articles.utils.ApiConstants.NETWORK_TIMEOUT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): Resource<T> {
    return withContext(dispatcher) {

        try {
            // throws TimeoutCancellationException
            withTimeout(NETWORK_TIMEOUT) {
                Resource.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()

            when (throwable) {

                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    Resource.Error(code.toString())
                }

                is IOException -> {
                    Resource.NetworkError()
                }

                is HttpException -> {
                    val code = throwable.code()
                    /*crashLog(code.toString())
                    if (code == 401) {
                        AppManager.logOutUser()
                    }*/
                    Resource.Error(code.toString())

                }

                else -> {
//                    crashLog(NETWORK_ERROR_UNKNOWN)
                    Resource.Error(NETWORK_ERROR_UNKNOWN)
                }
            }
        }
    }
}