package com.example.newsmvvmdemo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.navArgs
import com.example.newsmvvmdemo.R
import com.example.newsmvvmdemo.adapter.NewsAdapter
import com.example.newsmvvmdemo.databinding.FragmentArticleBinding
import com.example.newsmvvmdemo.model.Article
import com.example.newsmvvmdemo.ui.MainActivity
import com.example.newsmvvmdemo.ui.NewsViewModel

class FragmentArticle : Fragment() {

    lateinit var viewModel: NewsViewModel
    /*
    * Class FragmentSavedNewsBinding se duoc gen tu dong khi nguoi dung khai bao lop layout boc het file xml
    * vi du ta co fragment_search_new.xml voi layout la constrainlayout
    * ta boc constrainlayout trong the <layout></layout> luc nay android studio se tu dong tao class cho ta
    * fragment_search_new.xml -> class name: FragmentSearchNews se duoc gen ra
    * Luu y phai enable dataBinding trong grade build
    * */
    lateinit var binding: FragmentArticleBinding
    val arg:FragmentArticleArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_article,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val article = arg.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)

        }
        binding.fab.setOnClickListener {
            viewModel.upsertArticle(article)
            Toast.makeText(activity, "Article saved", Toast.LENGTH_SHORT).show()
        }
    }



}