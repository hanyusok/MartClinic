package com.example.martclinic.ui.components.error

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NavigationErrorHandler(
    error: StateFlow<String?>,
    snackbarHostState: SnackbarHostState,
    onErrorShown: () -> Unit
) {
    LaunchedEffect(error) {
        if (error.value != null) {
            snackbarHostState.showSnackbar(
                message = error.value ?: "An error occurred",
                duration = SnackbarDuration.Short
            )
            onErrorShown()
        }
    }
}

@Composable
fun NavigationErrorHandler(
    error: String?,
    onErrorShown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        if (error != null) {
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            onErrorShown()
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier
    ) { data ->
        Snackbar(
            snackbarData = data,
            modifier = modifier
        )
    }
} 