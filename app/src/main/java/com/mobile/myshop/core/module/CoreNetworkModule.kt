package com.mobile.myshop.core.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mobile.myshop.BuildConfig
import com.mobile.myshop.datamodel.constant.AppConstant
import com.mobile.myshop.datamodel.remote.ApiManager
import com.mobile.myshop.datamodel.remote.RajaOngkirApiManager
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Lukas Dylan Adisurya <lukas.adisurya@ovo.id}>
 * on 4/26/2018.
 */
@Module
class CoreNetworkModule {

    @Provides
    @Singleton
    @Named("non-cached")
    fun provideHttpClient(logger: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient().newBuilder().apply {
            retryOnConnectionFailure(true)
            addInterceptor(logger)
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    @Named("cached")
    fun provideHttpClientCached(logger: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
        val builder = OkHttpClient().newBuilder().apply {
            retryOnConnectionFailure(true)
            cache(cache)
            addInterceptor(logger)
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideCache(file: File): Cache {
        return Cache(file, 10 * 1024 * 1024)
    }

    @Provides
    @Singleton
    fun provideCacheFile(context: Context): File {
        return context.filesDir
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideGsonClient(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideRxAdapter(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    @Provides
    @Singleton
    fun provideApiManager(@Named("non-cached") okHttpClient: OkHttpClient,
                          gsonConverterFactory: GsonConverterFactory,
                          rxJava2CallAdapterFactory: RxJava2CallAdapterFactory): ApiManager {
        val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstant.API_BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build()

        return retrofit.create(ApiManager::class.java)
    }

    @Provides
    @Singleton
    fun provideRajaOngkirManager(@Named("cached") okHttpClient: OkHttpClient,
                                 gsonConverterFactory: GsonConverterFactory,
                                 rxJava2CallAdapterFactory: RxJava2CallAdapterFactory): RajaOngkirApiManager {
        val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstant.RAJA_ONGKIR_API_BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build()

        return retrofit.create(RajaOngkirApiManager::class.java)
    }
}