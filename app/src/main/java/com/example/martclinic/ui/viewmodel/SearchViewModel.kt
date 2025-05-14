package com.example.martclinic.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.martclinic.domain.model.Person
import com.example.martclinic.domain.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: PersonRepository
) : BasePersonViewModel<List<Person>>() {

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: StateFlow<List<String>> = _searchHistory

    private val _searchSuggestions = MutableStateFlow<List<String>>(emptyList())
    val searchSuggestions: StateFlow<List<String>> = _searchSuggestions

    init {
        _uiState.value = UiState.Success(emptyList())
    }

    fun reset() {
        _uiState.value = UiState.Success(emptyList())
        _searchSuggestions.value = emptyList()
    }

    fun searchByPcode(pcode: Int) {
        if (pcode <= 0) {
            clearSearchResults()
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.getPerson(pcode).collect { person ->
                    _uiState.value = UiState.Success(listOf(person))
                    _searchHistory.value = (_searchHistory.value + pcode.toString()).distinct().take(10)
                }
            } catch (e: Exception) {
                handleError(e, "Failed to search by PCODE")
            }
        }
    }

    fun searchBySearchId(searchId: String) {
        if (searchId.isBlank()) {
            clearSearchResults()
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.getPersonsBySearchId(searchId).collect { persons ->
                    _uiState.value = UiState.Success(persons)
                    _searchHistory.value = (_searchHistory.value + searchId).distinct().take(10)
                }
            } catch (e: Exception) {
                handleError(e, "Failed to search by Search ID")
            }
        }
    }

    fun searchByPname(pname: String) {
        if (pname.isBlank()) {
            clearSearchResults()
            return
        }
        
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.getPersonsByName(pname).collect { persons ->
                    _uiState.value = UiState.Success(persons)
                    if (pname.isNotBlank()) {
                        _searchHistory.value = (_searchHistory.value + pname).distinct().take(10)
                    }
                }
            } catch (e: Exception) {
                handleError(e, "Failed to search by name")
            }
        }
    }

    fun updateSearchSuggestions(query: String) {
        if (query.isBlank()) {
            _searchSuggestions.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                repository.getPersonsByName(query).collect { persons ->
                    _searchSuggestions.value = persons
                        .mapNotNull { it.pname }
                        .distinct()
                        .take(15)
                }
            } catch (e: Exception) {
                // Silently handle suggestion errors
                _searchSuggestions.value = emptyList()
            }
        }
    }

    fun clearSearchResults() {
        _uiState.value = UiState.Success(emptyList())
        _searchSuggestions.value = emptyList()
    }
} 