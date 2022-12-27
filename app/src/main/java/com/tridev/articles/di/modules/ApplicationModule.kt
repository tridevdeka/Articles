package com.tridev.articles.di.modules

import android.content.Context
import androidx.room.Room
import com.tridev.articles.local.ArticleDao
import com.tridev.articles.local.ArticleDatabase
import com.tridev.articles.remote.ArticleApiInterface
import com.tridev.articles.utils.ApiConstants
import com.tridev.articles.utils.ApiConstants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Provides
    @Singleton
    fun provideApiInterface(builder: Retrofit.Builder): ArticleApiInterface {
        return builder
            .build()
            .create(ArticleApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun providesArticleDatabase(@ApplicationContext context: Context): ArticleDatabase {
        return Room.databaseBuilder(context, ArticleDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(articleDatabase: ArticleDatabase): ArticleDao {
        return articleDatabase.getArticleDao()
    }
}