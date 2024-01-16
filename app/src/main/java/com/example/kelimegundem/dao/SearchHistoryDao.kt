package com.example.kelimegundem.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.kelimegundem.model.SearchHistoryItem

@Dao
interface SearchHistoryDao {
    @Insert
    suspend fun insertSearchQuery(searchHistoryItem: SearchHistoryItem)

    @Query("SELECT * FROM SearchHistoryItem ORDER BY id DESC")
    suspend fun getSearchHistory(): List<SearchHistoryItem>

    @Delete
    suspend fun deleteSearchQuery(searchHistoryItem: SearchHistoryItem)
}
