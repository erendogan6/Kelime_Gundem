package com.example.kelimegundem.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelimegundem.R
import com.example.kelimegundem.databinding.ItemNewsBinding
import com.example.kelimegundem.model.Article
import com.example.kelimegundem.util.NewsItemListener
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class NewsListAdaptor(private var newsList: List<Article>, private val listener: NewsItemListener) : RecyclerView.Adapter<NewsListAdaptor.NewsViewHolder>() {

    class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            
            if (article.source?.name !=null){
                binding.textViewAuthor.text = article.source.name
                binding.imagePen.visibility = View.VISIBLE
            }
            else if (article.author != null) {
                    binding.textViewAuthor.text = article.author
                    binding.imagePen.visibility = View.VISIBLE
            } else {
                binding.textViewAuthor.text = ""
                binding.imagePen.visibility = View.INVISIBLE
            }

            binding.textViewpublishedAt.text = article.publishedAt?.let { formatNewsDate(it) }
            binding.textViewTitle.text = article.title

            if ((article.description != null) && (article.description.length > 1)) {
                val cleanDescription = article.description.replace("\n", "").replace("\t", "")
                binding.textViewDescription.text = cleanDescription
            }
            else{
                binding.textViewDescription.visibility = View.GONE
            }

            if(article.urlToImage != null) {
                Glide.with(binding.newsImage.context)
                    .load(article.urlToImage)
                    .into(binding.newsImage)
            }
            else{
                binding.newsImage.setImageResource(R.drawable.newsplaceholder)
                binding.newsImage.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }
        private fun formatNewsDate(apiDate: String): String {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val newsDate = LocalDateTime.parse(apiDate, formatter)
            val currentDate = LocalDateTime.now(ZoneId.of("UTC"))

            return if (newsDate.toLocalDate() == currentDate.toLocalDate()) {
                newsDate.format(DateTimeFormatter.ofPattern("HH:mm"))
            } else {
                newsDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNewsBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = newsList[position]
        holder.bind(article)
        holder.itemView.setOnClickListener { listener.onNewsItemClicked(article) }
    }

    override fun getItemCount() = newsList.size

    fun updateNews(newNewsList: List<Article>) {
        newsList = newNewsList
        notifyDataSetChanged()
    }
}

