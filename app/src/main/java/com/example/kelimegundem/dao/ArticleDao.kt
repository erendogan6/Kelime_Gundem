package com.example.kelimegundem.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kelimegundem.model.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("SELECT * FROM Article")
    suspend fun getAllArticles(): List<Article>

    @Query("SELECT * FROM Article WHERE url = :url")
    suspend fun getArticleByUrl(url: String): Article?
}
