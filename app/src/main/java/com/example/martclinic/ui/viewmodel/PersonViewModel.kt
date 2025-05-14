package com.example.martclinic.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.martclinic.domain.model.Person
import com.example.martclinic.domain.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val repository: PersonRepository
) : BasePersonViewModel<Person>() {

    private val _persons = MutableStateFlow<List<Person>>(emptyList())
    val persons: StateFlow<List<Person>> = _persons.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _pagination = MutableStateFlow(Pagination())
    val pagination: StateFlow<Pagination> = _pagination.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    init {
        getPersons()
    }

    override fun updateFormField(field: String, value: Any?) {
        super.updateFormField(field, value)
    }

    fun getPersons(page: Int = 1) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.getPersons(page).collect { response ->
                    _persons.value = response.data
                    _uiState.value = UiState.Success(Person())
                }
            } catch (e: Exception) {
                handleError(e, "Failed to fetch persons")
            }
        }
    }

    fun getPerson(pcode: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.getPerson(pcode).collect { person ->
                    _currentPerson.value = person
                    _formState.value = person
                    _uiState.value = UiState.Success(person)
                }
            } catch (e: Exception) {
                handleError(e, "Failed to fetch person")
            }
        }
    }

    fun createPerson(person: Person) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.createPerson(person).collect { createdPerson ->
                    _persons.value = _persons.value + createdPerson
                    _uiState.value = UiState.Success(createdPerson)
                }
            } catch (e: Exception) {
                handleError(e, "Failed to create person")
            }
        }
    }

    fun updatePerson(person: Person) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.updatePerson(person).collect { updatedPerson ->
                    _persons.value = _persons.value.map { 
                        if (it.pcode == updatedPerson.pcode) updatedPerson else it 
                    }
                    _currentPerson.value = updatedPerson
                    _formState.value = updatedPerson
                    _uiState.value = UiState.Success(updatedPerson)
                }
            } catch (e: Exception) {
                handleError(e, "Failed to update person")
            }
        }
    }

    fun deletePerson(pcode: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.deletePersonByPcode(pcode).collect {
                    _persons.value = _persons.value.filter { it.pcode != pcode }
                    _uiState.value = UiState.Success(Person())
                }
            } catch (e: Exception) {
                handleError(e, "Failed to delete person")
            }
        }
    }

    fun loadMore() {
        if (_isLoadingMore.value || _pagination.value.isLastPage) return
        
        viewModelScope.launch {
            _isLoadingMore.value = true
            try {
                val nextPage = _currentPage.value + 1
                repository.getPersons(nextPage).collect { response ->
                    val newPersons = response.data
                    _persons.value = _persons.value + newPersons
                    _currentPage.value = nextPage
                    _pagination.value = _pagination.value.copy(
                        currentPage = nextPage,
                        isLastPage = newPersons.isEmpty()
                    )
                }
            } catch (e: Exception) {
                handleError(e, "Failed to load more persons")
            } finally {
                _isLoadingMore.value = false
            }
        }
    }
}

data class Pagination(
    val currentPage: Int = 1,
    val isLastPage: Boolean = false
) 