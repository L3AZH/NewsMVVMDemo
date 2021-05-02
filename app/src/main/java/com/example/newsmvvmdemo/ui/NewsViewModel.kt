package com.example.newsmvvmdemo.ui

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsmvvmdemo.model.Article
import com.example.newsmvvmdemo.model.NewsResponse
import com.example.newsmvvmdemo.util.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import retrofit2.Response

class NewsViewModel(val repository: NewsRepository) :ViewModel(),Observable{
    val breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var savedArticle: LiveData<List<Article>> = MutableLiveData()
    @Bindable
    val inputInfor : MutableLiveData<String> = MutableLiveData()
    val breakingPage = 1
    val searchPage = 1
    init {
        getBreakingNews("us")
        getAllSavedArticle()
    }

    fun getBreakingNews(country:String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = repository.getBreakingNew(country,breakingPage)
        breakingNews.postValue(handlerBreakingNews(response))
    }

    private fun handlerBreakingNews(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultRespone ->
                return Resource.Success(resultRespone)
            }
        }
        return Resource.Error(response.message())
    }


    fun getSearchNews(q:String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        inputInfor.value = q
        val response = repository.getSearchNews(inputInfor.value!!,searchPage)
        searchNews.postValue(handlerSearchNews(response))

    }
    private fun handlerSearchNews(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultRespone ->
                return Resource.Success(resultRespone)
            }
        }
        return Resource.Error(response.message())
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun upsertArticle(article: Article){
        viewModelScope.launch {
            repository.upsertArticle(article)
        }
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    fun getAllSavedArticle(){
        savedArticle = repository.getAllSavedArticle()
    }
}