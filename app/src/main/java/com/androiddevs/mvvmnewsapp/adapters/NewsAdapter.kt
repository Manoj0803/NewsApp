package com.androiddevs.mvvmnewsapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.databinding.ItemArticlePreviewBinding
import com.androiddevs.mvvmnewsapp.models.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

//    class NewsDiffUtilCallback : DiffUtil.ItemCallback<Article>(){
//        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
//            return oldItem.url == newItem.url
//        }
//
//        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
//            return oldItem == newItem
//        }
//    }

    private val diffCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    class ArticleViewHolder(var binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(article: Article) {
                binding.apply {
                    property = article
                    executePendingBindings()
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context))
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)

        holder.itemView.setOnClickListener{
            onItemClickedListener?.let {
                it(article)
                Log.i("RecyclerView","Item Clicked.")
            }
        }
    }

    private var onItemClickedListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickedListener = listener
    }

    override fun getItemCount(): Int = differ.currentList.size

}