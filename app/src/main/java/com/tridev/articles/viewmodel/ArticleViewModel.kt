package com.tridev.articles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tridev.articles.model.Article
import com.tridev.articles.repository.ArticleRepository
import com.tridev.articles.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(private val repository: ArticleRepository) :
    ViewModel() {

    /*private val _articles = MutableLiveData<Resource<ArticleResponse>>()
    var articles: LiveData<Resource<ArticleResponse>> = _articles*/
    private val _articles = MutableLiveData<Resource<List<Article>>>()
    var articles: LiveData<Resource<List<Article>>> = _articles

    private val _sharedArticle = MutableLiveData<Article>()
    val sharedArticle = _sharedArticle


    init {
        getArticles()
    }

    fun shareArticle(article: Article) {
        _sharedArticle.postValue(article)
    }

    private fun getArticles() {
        viewModelScope.launch {
            _articles.postValue(Resource.Loading())
            val response = repository.getArticles()
            _articles.postValue(response)
        }
    }

    /*private fun handleArticleResponse(response: Response<ArticleResponse>): Resource<ArticleResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }*/


    fun updateArticle(id:Int) {
        viewModelScope.launch {
            repository.updateFavoriteArticle(id)
        }
    }
    fun insertFavoriteArticle(article: Article) {
        viewModelScope.launch {
            repository.insertFavoriteArticle(article)
        }
    }

    fun getSavedArticles(): LiveData<List<Article>> {
        return repository.getSavedArticle()
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteArticles(article)
        }
    }
}