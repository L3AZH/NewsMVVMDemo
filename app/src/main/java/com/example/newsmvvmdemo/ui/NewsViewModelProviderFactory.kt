package com.example.newsmvvmdemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class NewsViewModelProviderFactory(private val repository: NewsRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewsViewModel::class.java)){
            return NewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unkown View Model")
    }
}