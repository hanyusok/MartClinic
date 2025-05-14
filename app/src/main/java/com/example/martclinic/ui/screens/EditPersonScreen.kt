package com.example.martclinic.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.martclinic.ui.viewmodel.PersonViewModel
import com.example.martclinic.ui.components.error.ErrorContent
import com.example.martclinic.ui.components.error.NavigationErrorHandler
import com.example.martclinic.ui.components.PersonDropdownField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPersonScreen(
    pcode: Int,
    onNavigateBack: () -> Unit,
    viewModel: PersonViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    LaunchedEffect(pcode) {
        viewModel.getPerson(pcode)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Person") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            formState?.let { person ->
                                viewModel.updatePerson(person)
                                onNavigateBack()
                            }
                        },
                        enabled = !uiState.isLoading && formState != null
                    ) {
                        Icon(Icons.Default.Check, "Save")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.errorMessage != null -> {
                    ErrorContent(
                        message = uiState.errorMessage!!,
                        onRetry = { viewModel.getPerson(pcode) }
                    )
                }
                formState != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Read-only fields
                        PersonDetailField(
                            label = "PCODE",
                            value = formState!!.pcode?.toString() ?: "",
                            isEditing = false,
                            onValueChange = { },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        PersonDetailField(
                            label = "FCODE",
                            value = formState!!.fcode?.toString() ?: "",
                            isEditing = false,
                            onValueChange = { },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        // Editable fields
                        PersonDetailField(
                            label = "PNAME",
                            value = formState!!.pname ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("pname", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        PersonDetailField(
                            label = "PBIRTH",
                            value = formState!!.pbirth ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("pbirth", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        PersonDetailField(
                            label = "PIDNUM",
                            value = formState!!.pidnum ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("pidnum", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        PersonDetailField(
                            label = "PIDNUM2",
                            value = formState!!.pidnum2 ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("pidnum2", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        PersonDetailField(
                            label = "OLDIDNUM",
                            value = formState!!.oldidnum ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("oldidnum", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        
                        // SEX dropdown (3 for male, 4 for female)
                        PersonDropdownField(
                            label = "SEX",
                            value = formState!!.sex ?: "",
                            options = listOf(
                                "3" to "Male",
                                "4" to "Female"
                            ),
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("sex", it) }
                        )
                        
                        // RELATION dropdown
                        PersonDropdownField(
                            label = "RELATION",
                            value = formState!!.relation ?: "",
                            options = listOf(
                                "1" to "Primary",
                                "2" to "Secondary",
                                "3" to "Family Member"
                            ),
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("relation", it) }
                        )
                        
                        PersonDetailField(
                            label = "RELATION2",
                            value = formState!!.relation2 ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("relation2", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        
                        // CRIPPLED dropdown (0 for no, 1 for yes)
                        PersonDropdownField(
                            label = "CRIPPLED",
                            value = formState!!.crippled ?: "",
                            options = listOf(
                                "0" to "No",
                                "1" to "Yes"
                            ),
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("crippled", it) }
                        )
                        
                        PersonDetailField(
                            label = "VINFORM",
                            value = formState!!.vinform ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("vinform", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        PersonDetailField(
                            label = "AGREE",
                            value = formState!!.agree ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("agree", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        PersonDetailField(
                            label = "PERINFO",
                            value = formState!!.perinfo ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("perinfo", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        PersonDetailField(
                            label = "JAEHAN",
                            value = formState!!.jaehan ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("jaehan", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        PersonDetailField(
                            label = "SEARCHID",
                            value = formState!!.searchId ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("searchid", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        PersonDetailField(
                            label = "PCCHECK",
                            value = formState!!.pccheck ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("pccheck", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        PersonDetailField(
                            label = "PSNIDT",
                            value = formState!!.psnidt ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("psnidt", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        PersonDetailField(
                            label = "PSNID",
                            value = formState!!.psnid ?: "",
                            isEditing = true,
                            onValueChange = { viewModel.updateFormField("psnid", it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }

            NavigationErrorHandler(
                error = uiState.errorMessage,
                onErrorShown = { viewModel.clearError() }
            )
        }
    }
} 