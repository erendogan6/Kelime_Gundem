package com.example.kelimegundem.model

data class NewsResponse(val status: String,
                        val totalResults:String,
                        val articles: List<Article>
)