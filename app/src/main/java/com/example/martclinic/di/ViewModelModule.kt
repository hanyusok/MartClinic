package com.example.martclinic.di

import com.example.martclinic.domain.repository.PersonRepository
import com.example.martclinic.ui.viewmodel.CreatePersonViewModel
import com.example.martclinic.ui.viewmodel.PersonViewModel
import com.example.martclinic.ui.viewmodel.SearchViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideCreatePersonViewModel(
        repository: PersonRepository
    ): CreatePersonViewModel {
        return CreatePersonViewModel(repository)
    }

    @Provides
    @ViewModelScoped
    fun providePersonViewModel(
        repository: PersonRepository
    ): PersonViewModel {
        return PersonViewModel(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideSearchViewModel(
        repository: PersonRepository
    ): SearchViewModel {
        return SearchViewModel(repository)
    }
} 