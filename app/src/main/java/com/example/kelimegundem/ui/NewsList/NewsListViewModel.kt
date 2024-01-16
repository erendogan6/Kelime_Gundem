package com.example.kelimegundem.ui.NewsList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelimegundem.model.Article
import com.example.kelimegundem.model.SearchHistoryItem
import com.example.kelimegundem.repository.NewsListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val repository: NewsListRepository) : ViewModel() {
    val newsItems = MutableLiveData<List<Article>>()
    private var currentPage = 1
    private var maxPage = 5
    private var totalResults = 0
    private lateinit var currentQuery: String
    var isLoading = MutableLiveData<Boolean>().apply { value = false }
    val searchHistory = MutableLiveData<List<SearchHistoryItem>>()


    fun setQuery(query: String) {
        currentQuery = query
        currentPage = 1
        loadNews( currentQuery,currentPage)
    }

    private fun loadNews(query: String, page: Int) {
        if (!isLoading.value!! && page <= maxPage) {
            isLoading.value = true
            viewModelScope.launch {
                try {
                    val response = repository.getSearch(query, page)
                    response?.let {
                        totalResults = Integer.parseInt(it.totalResults)
                        maxPage = minOf((totalResults + 19) / 20, 5)
                        val newNewsItems = it.articles
                        if (page == 1) {
                            newsItems.value = newNewsItems
                        } else {
                            newsItems.value = newsItems.value.orEmpty() + newNewsItems
                        }
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    isLoading.value = false
                }
            }
        }
    }

    fun loadMoreNews() {
        if (!isLoading.value!!) {
            loadNews(currentQuery,++currentPage)
        }
    }
    fun addSearchQueryToHistory(query: String) {
        viewModelScope.launch {
            repository.insertSearchQuery(query)
            getSearchHistory()
        }
    }

    fun getSearchHistory() {
        viewModelScope.launch {
            searchHistory.value = repository.getSearchHistory()
        }
    }


    fun deleteSearchQuery(searchHistoryItem: SearchHistoryItem) {
        viewModelScope.launch {
            repository.deleteSearchQuery(searchHistoryItem)
            getSearchHistory()
        }
    }


}
