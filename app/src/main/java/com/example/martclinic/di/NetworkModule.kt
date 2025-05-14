package com.example.martclinic.di

import com.example.martclinic.data.remote.service.MtsWaitApiService
import com.example.martclinic.data.remote.service.PersonApiService
import com.example.martclinic.data.remote.service.MtrApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Module that provides network-related dependencies.
 * All network components are scoped to the application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
// android phone:
//    private const val BASE_URL = "http://10.0.2.2:3000/api/"
// tablet: wireless or usb-connected under same wifi network
// make sure to check ipconfig via cmd "ipconfig" to get ip address
    private const val BASE_URL = "http://192.168.219.143:3000/api/"
    private const val TIMEOUT_SECONDS = 30L

    /**
     * Provides a configured OkHttpClient with logging and timeouts.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * Provides a configured Retrofit instance with the base URL and converter factory.
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    /**
     * Provides the PersonApiService implementation.
     */
    @Provides
    @Singleton
    fun providePersonApiService(retrofit: Retrofit): PersonApiService {
        return retrofit.create(PersonApiService::class.java)
    }

    /**
     * Provides the MtsWaitApiService implementation.
     */
    @Provides
    @Singleton
    fun provideMtsWaitApiService(retrofit: Retrofit): MtsWaitApiService {
        return retrofit.create(MtsWaitApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMtrApiService(retrofit: Retrofit): MtrApiService {
        return retrofit.create(MtrApiService::class.java)
    }
} 