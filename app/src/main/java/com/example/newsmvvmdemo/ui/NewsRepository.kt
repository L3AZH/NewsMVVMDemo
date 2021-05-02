package com.example.newsmvvmdemo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsmvvmdemo.api.RetrofitInstance
import com.example.newsmvvmdemo.db.ArticleDAO
import com.example.newsmvvmdemo.db.ArticleDatabse
import com.example.newsmvvmdemo.model.Article
import retrofit2.Retrofit

class NewsRepository(val dao:ArticleDAO) {
    suspend fun getBreakingNew(country:String,pageNumber:Int) =
            RetrofitInstance.api.getBreakingNews(country, pageNumber)
    suspend fun getSearchNews(q:String,pageNumber: Int) =
            RetrofitInstance.api.searchForNews(q,pageNumber)

    suspend fun upsertArticle(article:Article){
        dao.upsert(article)
    }

    suspend fun deleteArticle(article: Article){
        dao.deleteArticle(article)
    }

    fun getAllSavedArticle(): LiveData<List<Article>> {
        return dao.getAllArticle()
    }
}