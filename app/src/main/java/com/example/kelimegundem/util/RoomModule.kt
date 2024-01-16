package com.example.kelimegundem.util

import android.content.Context
import androidx.room.Room
import com.example.kelimegundem.dao.ArticleDao
import com.example.kelimegundem.dao.SearchHistoryDao
import com.example.kelimegundem.room.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): NewsDatabase {
        return Room.databaseBuilder(
            appContext,
            NewsDatabase::class.java,
            "news_database"
        ).build()
    }

    @Provides
    fun provideArticleDao(database: NewsDatabase): ArticleDao {
        return database.articleDao()
    }

    @Provides
    fun provideSearchHistoryDao(database: NewsDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
}