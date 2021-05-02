package com.example.newsmvvmdemo.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsmvvmdemo.model.Article

@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article):Long

    @Query("select * from article_table")
    fun getAllArticle():LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article):Int

}