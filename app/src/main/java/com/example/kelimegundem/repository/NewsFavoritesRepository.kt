package com.example.kelimegundem.repository

import com.example.kelimegundem.dao.ArticleDao
import com.example.kelimegundem.model.Article
import javax.inject.Inject

class NewsFavoritesRepository @Inject constructor(private val articleDao: ArticleDao) {
    suspend fun insertArticle(article: Article) {
        articleDao.insertArticle(article)
    }

    suspend fun deleteArticle(article: Article) {
        articleDao.deleteArticle(article)
    }

    suspend fun getFavoriteArticles(): List<Article> {
        return articleDao.getAllArticles()
    }

    suspend fun isArticleFavorite(url: String): Boolean {
        return articleDao.getArticleByUrl(url) != null
    }
}
