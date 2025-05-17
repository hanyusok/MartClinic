package com.example.martclinic.ui.viewmodel

import androidx.compose.runtime.Composable
import com.example.martclinic.domain.model.Person

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()

    val isLoading: Boolean
        get() = this is Loading

    val errorMessage: String?
        get() = when (this) {
            is Error -> message
            else -> null
        }

    val value: T?
        get() = when (this) {
            is Success -> data
            else -> null
        }
}

// Type aliases for common use cases
typealias PersonListUiState = UiState<List<Person>>
typealias SinglePersonUiState = UiState<Person>
typealias UnitUiState = UiState<Unit>

// Extension functions for common operations
@Composable
fun <T> UiState<T>.onSuccess(block: @Composable (T) -> Unit) {
    if (this is UiState.Success) {
        block(value!!)
    }
}

@Composable
fun <T> UiState<T>.onError(block: @Composable (String) -> Unit) {
    if (this is UiState.Error) {
        block(errorMessage!!)
    }
}

@Composable
fun <T> UiState<T>.onLoading(block: @Composable () -> Unit) {
    if (this is UiState.Loading) {
        block()
    }
} 