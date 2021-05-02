package com.example.newsmvvmdemo.adapter

import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsmvvmdemo.R
import com.example.newsmvvmdemo.databinding.ItemArticleReviewBinding
import com.example.newsmvvmdemo.model.Article

class NewsAdapter:RecyclerView.Adapter<ArticleViewHolder>() {

    /*
    * Tạo 1 lamba funtion sau đó tạo hàm set cho nó
    * thiết lập phương thức bind trong viewhold với tham số truyền vào là artical và lamba funtion
    * trong bindviewholder gọi hàm bind rồi truyền biến này vào
    * */
    private var onItemClickListener: ((Article)->Unit)?=null
    fun setOnItemClicklistener(listener: (Article)->Unit){
        onItemClickListener = listener
    }

    /*
    * DiffUtl là phần để kiểm tra danh sách có sự thay đổi google đi nha ng ae
    * */
    val differCallback = object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding:ItemArticleReviewBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.item_article_review,parent,false)
        return ArticleViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(differ.currentList[position],onItemClickListener)
    }

}

class ArticleViewHolder(val itemBinding:ItemArticleReviewBinding):RecyclerView.ViewHolder(itemBinding.root){
    fun bind(article: Article,listener: ((Article) -> Unit)?){
        itemBinding.apply {
            tvSource.text = article.source.name
            tvTitle.text = article.title
            tvPublishedAt.text = article.title
        }
        Glide.with(itemView).load(article.urlToImage).into(itemBinding.ivArticleImage)
        //set on click với lamba funtion
        itemView.setOnClickListener {
            listener?.let { it(article) }
        }
    }
}