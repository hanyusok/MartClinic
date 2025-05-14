package com.example.martclinic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.martclinic.domain.model.Mtr
import com.example.martclinic.domain.repository.MtrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mtrRepository: MtrRepository
) : ViewModel() {

    private val _mtrList = MutableStateFlow<List<Mtr>>(emptyList())
    val mtrList: StateFlow<List<Mtr>> = _mtrList.asStateFlow()

    init {
        loadTodayMtrList()
        startPeriodicRefresh()
    }

    private fun startPeriodicRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(5000) // Refresh every 5 seconds
                loadTodayMtrList()
            }
        }
    }

    private fun loadTodayMtrList() {
        viewModelScope.launch {
            try {
                val today = LocalDate.now()
                val formattedDate = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                mtrRepository.getByVisitDate(formattedDate).collect { result ->
                    _mtrList.value = result
                }
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Cleanup if needed
    }
} 