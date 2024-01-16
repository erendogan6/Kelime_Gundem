package com.example.kelimegundem.repository

import com.example.kelimegundem.api.NewsAPIService
import com.example.kelimegundem.dao.SearchHistoryDao
import com.example.kelimegundem.model.NewsResponse
import com.example.kelimegundem.model.SearchHistoryItem
import javax.inject.Inject

class NewsListRepository @Inject constructor(private val apiService: NewsAPIService,
                                             private val searchHistoryDao: SearchHistoryDao
) {
    private val apiKey = "efdffc2bc9d04a1a8a79c22a8f54c58e"

    suspend fun getSearch(q: String, page: Int): NewsResponse? {
        val response = apiService.getSearch(q, page, apiKey,20)
        println(response.raw())
        if (response.isSuccessful && response.body() !=null) {
            println(response.body()!!.totalResults)
            println(response.body()!!.articles.size)
            return response.body()
        } else {
            throw Exception("API Error: ${response.message()}")
        }
    }

    suspend fun insertSearchQuery(query: String) {
        val searchHistoryItem = SearchHistoryItem(query = query)
        searchHistoryDao.insertSearchQuery(searchHistoryItem)
    }

    suspend fun getSearchHistory(): List<SearchHistoryItem> {
        return searchHistoryDao.getSearchHistory()
    }

    suspend fun deleteSearchQuery(searchHistoryItem: SearchHistoryItem) {
        searchHistoryDao.deleteSearchQuery(searchHistoryItem)
    }
}
