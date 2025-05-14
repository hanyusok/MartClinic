package com.example.martclinic.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.martclinic.domain.model.Person
import com.example.martclinic.ui.components.PersonCard
import com.example.martclinic.ui.viewmodel.SearchViewModel
import com.example.martclinic.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPcodeScreen(
    onNavigateBack: () -> Unit,
    onPersonClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search by PCODE") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { 
                    // Only allow numeric input
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        searchQuery = it
                    }
                },
                label = { Text("Enter PCODE") },
                leadingIcon = { 
                    IconButton(
                        onClick = { 
                            searchQuery.toIntOrNull()?.let { pcode ->
                                viewModel.searchByPcode(pcode)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Search, "Search")
                    }
                },
                trailingIcon = if (searchQuery.isNotEmpty()) {
                    {
                        IconButton(onClick = { 
                            searchQuery = ""
                            viewModel.clearSearchResults()
                        }) {
                            Icon(Icons.Default.Close, "Clear search")
                        }
                    }
                } else null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        searchQuery.toIntOrNull()?.let { pcode ->
                            viewModel.searchByPcode(pcode)
                        }
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Show search history when no query and no results
            if (searchQuery.isEmpty() && (uiState.value as? List<Person>)?.isEmpty() == true) {
                Text(
                    text = "Enter PCODE to begin search",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }
                else -> {
                    val persons = uiState.value as? List<Person> ?: emptyList()
                    if (persons.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(persons) { person ->
                                PersonCard(
                                    person = person,
                                    onPersonClick = { onPersonClick(person.pcode.toString()) }
                                )
                            }
                        }
                    } else if (searchQuery.isNotEmpty()) {
                        Text(
                            text = "No person found with PCODE: $searchQuery",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
} 