package com.example.newsmvvmdemo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsmvvmdemo.R
import com.example.newsmvvmdemo.adapter.NewsAdapter
import com.example.newsmvvmdemo.databinding.FragmentSavedNewsBinding
import com.example.newsmvvmdemo.model.Article
import com.example.newsmvvmdemo.ui.MainActivity
import com.example.newsmvvmdemo.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment() {

    lateinit var viewModel: NewsViewModel
    /*
    * Class FragmentSavedNewsBinding se duoc gen tu dong khi nguoi dung khai bao lop layout boc het file xml
    * vi du ta co fragment_search_new.xml voi layout la constrainlayout
    * ta boc constrainlayout trong the <layout></layout> luc nay android studio se tu dong tao class cho ta
    * fragment_search_new.xml -> class name: FragmentSearchNews se duoc gen ra
    * Luu y phai enable dataBinding trong grade build
    * */
    lateinit var binding:FragmentSavedNewsBinding
    lateinit var newsAdapter:NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_saved_news,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setUpRecycleView()
        newsAdapter.setOnItemClicklistener {
            onClickArticle(it)
        }
        var itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                /*
                * chung ta chi muon no chuyen dong nen set thanh true
                * */
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                /*
                * sau khi delete t hien thong bao va nut undo
                * tim hieu snack bar di maiphen
                * */
                Snackbar.make(binding.root,"Deleted Successfully",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.upsertArticle(article)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }
    }

    fun setUpRecycleView(){
        newsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
        }
        displayRecycleView()

    }
    fun displayRecycleView(){
        viewModel.savedArticle.observe(viewLifecycleOwner, Observer {
            newsAdapter.differ.submitList(it)
        })
    }
    fun onClickArticle(article: Article){
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
        val goToFragmentArticle = SavedNewsFragmentDirections.actionSavedNewsFragmentToFragmentArticle(article)
        findNavController().navigate(goToFragmentArticle)
    }
}