package com.example.martclinic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.martclinic.domain.model.Mtr
import com.example.martclinic.domain.repository.MtrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MtrUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val mtrList: List<Mtr> = emptyList(),
    val selectedMtr: Mtr? = null
)

@HiltViewModel
class MtrViewModel @Inject constructor(
    private val repository: MtrRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MtrUiState())
    val uiState: StateFlow<MtrUiState> = _uiState.asStateFlow()

    fun loadAll() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.getAll()
                    .collect { mtrList ->
                        _uiState.update { it.copy(
                            isLoading = false,
                            mtrList = mtrList
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

    fun loadByVisitDate(visidate: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.getByVisitDate(visidate)
                    .collect { mtrList ->
                        _uiState.update { it.copy(
                            isLoading = false,
                            mtrList = mtrList
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

    fun loadByPcode(pcode: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.getByPcode(pcode)
                    .collect { mtr ->
                        _uiState.update { it.copy(
                            isLoading = false,
                            selectedMtr = mtr
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

    fun createMtr(mtr: Mtr) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.create(mtr)
                    .collect {
                        // After successful creation, reload the list for the current date
                        loadByVisitDate(mtr.visidate)
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                ) }
            }
        }
    }

    fun updateMtr(pcode: Int, mtr: Mtr) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.update(pcode, mtr)
                    .collect {
                        loadByVisitDate(mtr.visidate)
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                ) }
            }
        }
    }

    fun deleteMtr(pcode: Int, visidate: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.delete(pcode)
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