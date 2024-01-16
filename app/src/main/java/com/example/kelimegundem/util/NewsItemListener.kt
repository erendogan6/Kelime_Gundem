package com.example.kelimegundem.util

import com.example.kelimegundem.model.Article

interface NewsItemListener {
    fun onNewsItemClicked(article: Article)
}