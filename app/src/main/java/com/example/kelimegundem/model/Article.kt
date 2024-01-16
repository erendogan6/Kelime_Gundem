package com.example.kelimegundem.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Entity
@Parcelize
data class Article(
    @Embedded(prefix = "source_") val source: Source?,
    val title: String?,
    val description: String?,
    val content: String?,
    val author: String?,
    val publishedAt: String?,
    @PrimaryKey val url: String,
    val urlToImage: String?,
) :Parcelable
