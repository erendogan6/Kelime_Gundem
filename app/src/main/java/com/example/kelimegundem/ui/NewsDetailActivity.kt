package com.example.kelimegundem.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.kelimegundem.R
import com.example.kelimegundem.databinding.ActivityNewsDetailBinding
import com.example.kelimegundem.model.Article
import com.example.kelimegundem.ui.NewsFavorites.NewsFavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class NewsDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsDetailBinding
    private lateinit var article: Article
    private val viewModel: NewsFavoritesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        getNavigationArgs()
        viewBinding()
        setContentView(binding.root)
    }
    private fun getNavigationArgs(){
        val args: NewsDetailActivityArgs by navArgs()
        article = args.article
    }
    private fun viewBinding(){
        if (article.author != null) {
            binding.textViewAuthor.text = article.author
            binding.imagePen.visibility = View.VISIBLE
        }
        else {
            binding.textViewAuthor.text = ""
            binding.imagePen.visibility = View.INVISIBLE
        }

        binding.textViewpublishedAt.text = article.publishedAt?.let { formatNewsDate(it) }
        binding.textViewTitle.text = article.title

        if ((article.content != null) && (article.content!!.length > 1)) {
            var cleanDescription = article.content!!.replace("\n", "").replace("\t", "")
            val regex = Regex("\\[\\+\\d+ chars]")
            cleanDescription = cleanDescription.replace(regex, "")
            binding.textViewContent.text = cleanDescription
        }
        else{
            binding.textViewContent.visibility = View.GONE
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

        updateFavoriteIcon()


        binding.buttonReadMore.setOnClickListener {
            val intent = Intent(this,WebviewActivity::class.java)
            intent.putExtra("url",article.url)
            startActivity(intent)
        }
        binding.closeButton.setOnClickListener { finish() }

        binding.iconHeart.setOnClickListener {
            viewModel.isFavorite(article).observe(this) { isFav ->
                if (isFav) {
                    viewModel.removeFavorite(article)
                    binding.iconHeart.setImageResource(R.drawable.heart)
                } else {
                    viewModel.addFavorite(article)
                    binding.iconHeart.setImageResource(R.drawable.heart2)
                }
            }
        }

        binding.iconShare.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, article.url)
                type = "text/plain"
            }
            val intent = Intent.createChooser(shareIntent, "Haberi Paylaş")
            startActivity(intent)
        }

    }

    private fun updateFavoriteIcon() {
        viewModel.isFavorite(article).observe(this) { isFav ->
            if (isFav) {
                binding.iconHeart.setImageResource(R.drawable.heart2)
            } else {
                binding.iconHeart.setImageResource(R.drawable.heart)
            }
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