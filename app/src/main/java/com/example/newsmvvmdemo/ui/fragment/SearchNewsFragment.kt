package com.example.newsmvvmdemo.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavArgument
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsmvvmdemo.R
import com.example.newsmvvmdemo.adapter.NewsAdapter
import com.example.newsmvvmdemo.databinding.FragmentSearchNewsBinding
import com.example.newsmvvmdemo.model.Article
import com.example.newsmvvmdemo.ui.MainActivity
import com.example.newsmvvmdemo.ui.NewsViewModel
import com.example.newsmvvmdemo.util.Constants.Companion.SEARCH_TIME_DELAY
import com.example.newsmvvmdemo.util.Resource
import kotlinx.coroutines.*

class SearchNewsFragment : Fragment() {

    /*
    * Class FragmentSavedNewsBinding se duoc gen tu dong khi nguoi dung khai bao lop layout boc het file xml
    * vi du ta co fragment_search_new.xml voi layout la constrainlayout
    * ta boc constrainlayout trong the <layout></layout> luc nay android studio se tu dong tao class cho ta
    * fragment_search_new.xml -> class name: FragmentSearchNews se duoc gen ra
    * Luu y phai enable dataBinding trong grade build
    * */
    lateinit var binding:FragmentSearchNewsBinding
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter:NewsAdapter
    private val TAG = "SEARCHNEWFRAGMENT_TAG"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_news,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        binding.myViewModel = viewModel
        setUpRecycleView()
        var job: Job?=null
        binding.etSearch.addTextChangedListener {editable->
            job?.cancel()
            job = CoroutineScope(Dispatchers.Main).launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.getSearchNews(editable.toString())
                    }
                }
            }
        }
        viewModel.searchNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success -> {
                    hideProgessBar()
                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error ->{
                    hideProgessBar()
                    response.message?.let { message->
                        Log.i(TAG,message)
                    }
                }
                is Resource.Loading ->{
                    showProgessbar()
                }
            }
        })
        newsAdapter.setOnItemClicklistener {
            onClickArticle(it)
        }
    }

    fun setUpRecycleView(){
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
        }
    }
    fun hideProgessBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }
    fun showProgessbar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }
    fun onClickArticle(article:Article){
        /*
        * class SaveNewsFragmentDirections se duoc android gen tu dong
        * class name SavedNewsFragment - > SavedNewsFragmentDirections
        * Dieu kien de tao dc class
        * them classpath trong gradebuild , enable safe nav => search google de biet them thong tin
        * **luu y de truyen du lieu tu fragment -> fragment trong navigation ta co 2 cach
        * cach1: navigation theo id va du sung bundle:
        * vao nav_graph click chon atrribute ben phia tay phai phan doc nho
        * sau do them argument, chon name = "ten cua argument, giong voi key string trong intent",
        * argtype = "com.yourpackeg.your_class_object" -> done -> cach nay duong sd trong BreakingNewFragment
        * cach2: dung safe navigation, thiet lap argument nhu cach 1 cach 2 duoc su dung duoi day
        * */
        /*
        * cach 2 su dung safe navigation . cach 1 ben breaking news fragmnet qua do xem
        * */
        val goToArticleFragment = SearchNewsFragmentDirections.actionSearchNewsFragmentToFragmentArticle(article)
        findNavController().navigate(goToArticleFragment)
    }
}