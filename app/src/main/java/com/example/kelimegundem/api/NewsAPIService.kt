package com.example.kelimegundem.api

import com.example.kelimegundem.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIService {
    @GET("v2/everything")
    suspend fun getSearch(@Query("q") q: String,
                          @Query("page") page: Int,
                          @Query("apiKey") apiKey: String,
                          @Query ("pageSize") pageSize: Int
    ): Response<NewsResponse>
}
