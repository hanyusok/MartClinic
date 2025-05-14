package com.example.martclinic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.martclinic.domain.model.Person
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BasePersonViewModel<T> : ViewModel() {
    protected val _uiState = MutableStateFlow<UiState<T>>(UiState.Idle)
    val uiState: StateFlow<UiState<T>> = _uiState.asStateFlow()

    protected val _currentPerson = MutableStateFlow<Person?>(null)
    val currentPerson: StateFlow<Person?> = _currentPerson.asStateFlow()

    protected val _formState = MutableStateFlow<Person?>(null)
    val formState: StateFlow<Person?> = _formState.asStateFlow()

    fun handleError(e: Exception, defaultMessage: String = "An error occurred") {
        _uiState.value = UiState.Error(e.message ?: defaultMessage)
    }

    fun clearError() {
        _uiState.value = UiState.Idle
    }

    open fun updateFormField(field: String, value: Any?) {
        val currentForm = _formState.value ?: return
        val updatedPerson = when (field) {
            "pcode" -> currentForm.copy(pcode = value as? Int)
            "fcode" -> currentForm.copy(fcode = value as? Int)
            "pname" -> currentForm.copy(pname = value as? String)
            "pbirth" -> currentForm.copy(pbirth = value as? String)
            "pidnum" -> currentForm.copy(pidnum = value as? String)
            "pidnum2" -> currentForm.copy(pidnum2 = value as? String)
            "oldidnum" -> currentForm.copy(oldidnum = value as? String)
            "sex" -> currentForm.copy(sex = value as? String)
            "relation" -> currentForm.copy(relation = value as? String)
            "relation2" -> currentForm.copy(relation2 = value as? String)
            "crippled" -> currentForm.copy(crippled = value as? String)
            "vinform" -> currentForm.copy(vinform = value as? String)
            "agree" -> currentForm.copy(agree = value as? String)
            "perinfo" -> currentForm.copy(perinfo = value as? String)
            "jaehan" -> currentForm.copy(jaehan = value as? String)
            "searchId" -> currentForm.copy(searchId = value as? String)
            "pccheck" -> currentForm.copy(pccheck = value as? String)
            "psnidt" -> currentForm.copy(psnidt = value as? String)
            "psnid" -> currentForm.copy(psnid = value as? String)
            else -> currentForm
        }
        _formState.value = updatedPerson
    }
} 