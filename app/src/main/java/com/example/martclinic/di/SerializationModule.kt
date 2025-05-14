package com.example.martclinic.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import javax.inject.Singleton

/**
 * Module that provides JSON serialization dependencies.
 * All serialization components are scoped to the application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object SerializationModule {

    private const val JSON_MEDIA_TYPE = "application/json"

    /**
     * Provides a configured Json instance with lenient parsing and other optimizations.
     */
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        explicitNulls = false
        coerceInputValues = true
        prettyPrint = true
        allowStructuredMapKeys = true
        allowSpecialFloatingPointValues = true
        useArrayPolymorphism = true
    }

    /**
     * Provides a Retrofit converter factory for JSON serialization.
     */
    @Provides
    @Singleton
    fun provideConverterFactory(json: Json): Converter.Factory {
        val contentType = JSON_MEDIA_TYPE.toMediaType()
        return json.asConverterFactory(contentType)
    }
} 