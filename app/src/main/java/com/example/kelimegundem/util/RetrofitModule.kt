package com.example.kelimegundem.util

import com.example.kelimegundem.api.NewsAPIService
import com.example.kelimegundem.api.RetrofitClient
import com.example.kelimegundem.dao.SearchHistoryDao
import com.example.kelimegundem.repository.NewsListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Provides
    fun provideRetrofit(): Retrofit = RetrofitClient.retrofit

    @Singleton
    @Provides
    fun provideNewsAPIService(retrofit: Retrofit): NewsAPIService {
        return retrofit.create(NewsAPIService::class.java)
    }

    @Provides
    fun provideNewsRepository(apiService: NewsAPIService, searchHistoryDao: SearchHistoryDao): NewsListRepository {
        return NewsListRepository(apiService,searchHistoryDao)
    }
}
