package com.example.martclinic.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.martclinic.domain.model.Person
import com.example.martclinic.ui.components.PersonCard
import com.example.martclinic.ui.viewmodel.PersonViewModel
import com.example.martclinic.ui.viewmodel.UiState
import com.example.martclinic.ui.components.error.ErrorContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonListScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: PersonViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val persons by viewModel.persons.collectAsState()
    val pagination by viewModel.pagination.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.getPersons()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Persons") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, "Search")
                    }
                    IconButton(onClick = onNavigateToCreate) {
                        Icon(Icons.Default.Add, "Add Person")
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
                        onRetry = { viewModel.getPersons() }
                    )
                }
                persons.isNotEmpty() -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(persons) { person ->
                            PersonCard(
                                person = person,
                                onPersonClick = { onNavigateToDetail(person.pcode!!) }
                            )
                        }

                        if (!pagination.isLastPage && !isLoadingMore) {
                            item {
                                Button(
                                    onClick = { viewModel.loadMore() },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Load More")
                                }
                            }
                        }
                    }
                }
                else -> {
                    Text(
                        text = "No persons found",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

