package com.tridev.articles.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tridev.articles.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteArticle(article: Article)

    @Query("UPDATE article SET isFavorite=NOT isFavorite WHERE id=:id")
    suspend fun updateFavoriteArticle(id: Int)

    @Query("Select * from article where isFavorite=1")
    fun getAllFavoriteArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSyncArticles(list: List<Article>)

    @Query("Select * from article")
    fun getAllSyncArticles(): List<Article>
}