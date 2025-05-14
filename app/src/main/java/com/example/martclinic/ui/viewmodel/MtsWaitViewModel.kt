package com.example.martclinic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.martclinic.domain.model.MtsWait
import com.example.martclinic.domain.repository.MtsWaitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MtsWaitUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val mtsWaitList: List<MtsWait> = emptyList(),
    val selectedMtsWait: MtsWait? = null
)

@HiltViewModel
class MtsWaitViewModel @Inject constructor(
    private val repository: MtsWaitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MtsWaitUiState())
    val uiState: StateFlow<MtsWaitUiState> = _uiState.asStateFlow()

    fun loadByVisitDate(visidate: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.getByVisitDate(visidate)
                    .collect { mtsWaitList ->
                        _uiState.update { it.copy(
                            isLoading = false,
                            mtsWaitList = mtsWaitList
                        ) }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                ) }
            }
        }
    }

    fun loadById(pcode: Int, visidate: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.getById(pcode, visidate)
                    .collect { mtsWait ->
                        _uiState.update { it.copy(
                            isLoading = false,
                            selectedMtsWait = mtsWait
                        ) }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                ) }
            }
        }
    }

    fun createMtsWait(mtsWait: MtsWait) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.create(mtsWait)
                    .collect {
                        loadByVisitDate(mtsWait.visidate)
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                ) }
            }
        }
    }

    fun updateMtsWait(pcode: Int, visidate: String, mtsWait: MtsWait) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.update(pcode, visidate, mtsWait)
                    .collect {
                        loadByVisitDate(visidate)
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                ) }
            }
        }
    }

    fun deleteMtsWait(pcode: Int, visidate: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.delete(pcode, visidate)
                    .collect {
                        loadByVisitDate(visidate)
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                ) }
            }
        }
    }

    fun addMtsWait(
        pcode: Int,
        visidate: String,
        displayname: String,
        resid1: String,
        resid2: String,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val mtsWait = MtsWait(
                    pcode = pcode,
                    visidate = visidate,
                    displayname = displayname,
                    resid1 = resid1,
                    resid2 = resid2
                )
                repository.create(mtsWait)
                    .collect {
                        loadByVisitDate(visidate)
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                ) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
} 