package com.tridev.articles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tridev.articles.model.ArticleResponse
import com.tridev.articles.model.Source
import com.tridev.articles.repository.ArticleRepository
import com.tridev.articles.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(private val repository: ArticleRepository) :
    ViewModel() {

    private val _articles = MutableLiveData<Resource<ArticleResponse>>()
    var articles: LiveData<Resource<ArticleResponse>> = _articles


    init {
        getArticles()
    }

    private fun getArticles() {
        viewModelScope.launch {
            _articles.postValue(Resource.Loading())
            val response = repository.getArticles()
            _articles.postValue(handleArticleResponse(response))
        }
    }

    private fun handleArticleResponse(response: Response<ArticleResponse>): Resource<ArticleResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}