package com.example.martclinic.di

import com.example.martclinic.domain.repository.PersonRepository
import com.example.martclinic.data.repository.PersonRepositoryImpl
import com.example.martclinic.domain.repository.MtsWaitRepository
import com.example.martclinic.data.repository.MtsWaitRepositoryImpl
import com.example.martclinic.domain.repository.MtrRepository
import com.example.martclinic.data.repository.MtrRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module that provides repository implementations.
 * All repositories are scoped to the application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Provides the implementation of PersonRepository.
     * Scoped to the application lifecycle to maintain a single instance.
     */
    @Binds
    @Singleton
    abstract fun bindPersonRepository(
        personRepositoryImpl: PersonRepositoryImpl
    ): PersonRepository

    /**
     * Provides the implementation of MtsWaitRepository.
     * Scoped to the application lifecycle to maintain a single instance.
     */
    @Binds
    @Singleton
    abstract fun bindMtsWaitRepository(
        mtsWaitRepositoryImpl: MtsWaitRepositoryImpl
    ): MtsWaitRepository

    @Binds
    @Singleton
    abstract fun bindMtrRepository(
        mtrRepositoryImpl: MtrRepositoryImpl
    ): MtrRepository
} 