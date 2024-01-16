package com.example.kelimegundem.ui.NewsFavorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelimegundem.model.Article
import com.example.kelimegundem.repository.NewsFavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsFavoritesViewModel @Inject constructor( private val repository: NewsFavoritesRepository) : ViewModel() {

    val newsFavoritesItems = MutableLiveData<List<Article>>()

    fun isFavorite(article: Article): LiveData<Boolean> {
        val isFavorite = MutableLiveData<Boolean>()
        viewModelScope.launch {
            isFavorite.value = repository.isArticleFavorite(article.url)
        }
        return isFavorite
    }

    fun getFavorite()  {
        viewModelScope.launch {
            newsFavoritesItems.value = repository.getFavoriteArticles()
        }
    }

    fun addFavorite(article: Article) {
        viewModelScope.launch {
            repository.insertArticle(article)
        }
    }

    fun removeFavorite(article: Article) {
        viewModelScope.launch {
            repository.deleteArticle(article)
        }
    }
}