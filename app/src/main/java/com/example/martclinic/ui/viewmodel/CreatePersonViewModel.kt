package com.example.martclinic.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.martclinic.domain.model.Person
import com.example.martclinic.domain.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePersonViewModel @Inject constructor(
    private val repository: PersonRepository
) : BasePersonViewModel<Person>() {

    init {
        _formState.value = Person()
    }

    override fun updateFormField(field: String, value: Any?) {
        when (field) {
            "pname" -> _formState.value = _formState.value?.copy(pname = value as? String)
            "pbirth" -> _formState.value = _formState.value?.copy(pbirth = value as? String)
            "oldidnum" -> _formState.value = _formState.value?.copy(oldidnum = value as? String)
            "memo1" -> _formState.value = _formState.value?.copy(memo1 = value as? String)
            "agree" -> _formState.value = _formState.value?.copy(agree = value as? String)
            "searchId" -> _formState.value = _formState.value?.copy(searchId = value as? String)
            "birthDate" -> _formState.value = _formState.value?.copy(pbirth = value as? String)
        }
    }

    fun createPerson(person: Person) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.createPerson(person).collect { createdPerson ->
                    _uiState.value = UiState.Success(createdPerson)
                }
            } catch (e: Exception) {
                handleError(e, "Failed to create person")
            }
        }
    }
} 