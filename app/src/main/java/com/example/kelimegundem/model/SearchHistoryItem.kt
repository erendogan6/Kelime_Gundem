package com.example.kelimegundem.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val query: String
)
