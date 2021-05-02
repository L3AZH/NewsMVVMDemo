package com.example.newsmvvmdemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsmvvmdemo.R
import com.example.newsmvvmdemo.databinding.MainActivityBinding
import com.example.newsmvvmdemo.db.ArticleDatabse

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    private lateinit var binding:MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.main_activity)
        val dao = ArticleDatabse(this).getArticleDAO()
        val newsRepository = NewsRepository(dao)
        val viewModelFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(NewsViewModel::class.java)
        binding.lifecycleOwner = this
        /*
        * Fragment is not "View" so you cannot access it like traditional view.
        * But, you can access it via fragment tag doing like this:
        * 1. add android:tag = "your tag"
        * 2. do this
        * */
        val navHostFragment = supportFragmentManager.findFragmentByTag("navHostFragment")
        binding.bottomNavigationView.setupWithNavController(navHostFragment!!.findNavController())

    }
}