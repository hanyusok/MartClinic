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

data class CreatePersonFormState(
    val pcode: Int = 0,
    val fcode: String? = null,
    val pname: String = "",
    val pbirth: String = "",
    val pidnum: String = "",
    val pidnum2: String? = null,
    val oldidnum: String? = null,
    val sex: String = "",
    val relation: String? = null,
    val relation2: String? = null,
    val crippled: String? = null,
    val vinform: String? = null,
    val agree: String? = null,
    val lastcheck: String? = null,
    val perinfo: String? = null,
    val cardcheck: String? = null,
    val jaehan: String? = null,
    val searchid: String? = null,
    val pccheck: String? = null,
    val psnidt: String? = null,
    val psnid: String? = null
)

sealed class ValidationError {
    data class FieldEmpty(val fieldName: String) : ValidationError()
    data class FieldTooLong(val fieldName: String, val maxLength: Int) : ValidationError()
    data class InvalidFormat(val fieldName: String, val expectedFormat: String) : ValidationError()
    data class InvalidValue(val fieldName: String, val reason: String) : ValidationError()
    data class DuplicateValue(val fieldName: String, val value: String) : ValidationError()
} 