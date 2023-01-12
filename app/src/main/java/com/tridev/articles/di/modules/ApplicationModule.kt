package com.tridev.articles.di.modules

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.tridev.articles.local.ArticleDao
import com.tridev.articles.local.ArticleDatabase
import com.tridev.articles.remote.ArticleApiInterface
import com.tridev.articles.remote.AuthInterceptor
import com.tridev.articles.remote.LoggingInterceptor
import com.tridev.articles.repository.ArticleRepository
import com.tridev.articles.utils.ApiConstants
import com.tridev.articles.utils.ApiConstants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {


    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(loggingInterceptor: LoggingInterceptor): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(loggingInterceptor).also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    /*
    * We can inject directly wherever apiInterface with okHttpClient is required.
    */

    /*@Provides
    @Singleton
    fun provideApiInterfaceWithClient(
        builder: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): ArticleApiInterface {
        return builder
            .client(okHttpClient)
            .build()
            .create(OtherApiInterface::class.java)
    }*/

    @Provides
    @Singleton
    fun provideApiInterface(
        builder: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): ArticleApiInterface {
        return builder
            .client(okHttpClient)
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

    @Provides
    @Singleton
    fun provideArticleRepository(
        apiInterface: ArticleApiInterface,
        articleDao: ArticleDao,
    ): ArticleRepository {
        return ArticleRepository(apiInterface, articleDao)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}