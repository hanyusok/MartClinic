package com.example.martclinic.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.martclinic.domain.model.Person
import com.example.martclinic.ui.components.PersonCard
import com.example.martclinic.ui.viewmodel.SearchViewModel
import com.example.martclinic.ui.viewmodel.UiState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPersonScreen(
    onNavigateBack: () -> Unit,
    onPersonClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showScrollToBottom by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    val scrollToBottomAlpha by animateFloatAsState(
        targetValue = if (showScrollToBottom) 1f else 0f,
        label = "scrollToBottomAlpha"
    )

    // Debounced search effect
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            kotlinx.coroutines.delay(300) // Debounce for 300ms
            viewModel.searchByPname(searchQuery)
        } else {
            viewModel.clearSearchResults()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("돌아가기 Back") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (showScrollToBottom) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .alpha(scrollToBottomAlpha)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Scroll to bottom",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Fixed search area
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { 
                            Text(
                                "이름 name",
                                style = MaterialTheme.typography.headlineMedium
                            ) 
                        },
                        leadingIcon = { 
                            IconButton(
                                onClick = { 
                                    if (searchQuery.isNotEmpty()) {
                                        viewModel.searchByPname(searchQuery)
                                        keyboardController?.hide()
                                    }
                                },
                                modifier = Modifier.size(64.dp).padding(horizontal = 16.dp)
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    "Search",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        },
                        trailingIcon = if (searchQuery.isNotEmpty()) {
                            {
                                IconButton(
                                    onClick = { searchQuery = "" },
                                    modifier = Modifier.size(64.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        "Clear search",
                                        modifier = Modifier.size(48.dp)
                                    )
                                }
                            }
                        } else null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (searchQuery.isNotEmpty()) {
                                    viewModel.searchByPname(searchQuery)
                                    keyboardController?.hide()
                                }
                            }
                        ),
                        textStyle = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )

                    // Button Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalArrangement = Arrangement.spacedBy(96.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { 
                                if (searchQuery.isNotEmpty()) {
                                    viewModel.searchByPname(searchQuery)
                                    keyboardController?.hide()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                "검색 Search",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }

                        Button(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text(
                                "취소 Cancel",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
                }

                // Scrollable results area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    when {
                        uiState.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(16.dp)
                            )
                        }
                        uiState.errorMessage != null -> {
                            Text(
                                text = uiState.errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(16.dp)
                            )
                        }
                        else -> {
                            val persons = uiState.value as? List<Person> ?: emptyList()
                            if (persons.isNotEmpty()) {
                                LazyColumn(
                                    state = listState,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 48.dp)
                                ) {
                                    // Sort persons by birthdate (younger to older)
                                    val sortedPersons = persons.sortedByDescending { it.pbirth }
                                    
                                    // First row
                                    item {
                                        Box(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            val firstRowState = rememberLazyListState()
                                            val showFirstRowScroll by remember {
                                                derivedStateOf {
                                                    firstRowState.firstVisibleItemIndex > 0
                                                }
                                            }
                                            
                                            LazyRow(
                                                state = firstRowState,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 16.dp),
                                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                                            ) {
                                                items(sortedPersons.filterIndexed { index, _ -> index % 2 == 0 }) { person ->
                                                    PersonCard(
                                                        person = person,
                                                        onPersonClick = { onPersonClick(person.pcode.toString()) },
                                                        modifier = Modifier.width(300.dp)
                                                    )
                                                }
                                            }
                                            
                                            if (showFirstRowScroll) {
                                                FloatingActionButton(
                                                    onClick = {
                                                        coroutineScope.launch {
                                                            firstRowState.animateScrollToItem(0)
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .align(Alignment.CenterEnd)
                                                        .padding(end = 8.dp),
                                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                                ) {
                                                    Icon(
                                                        Icons.Default.KeyboardArrowRight,
                                                        contentDescription = "Scroll right",
                                                        modifier = Modifier.size(32.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    
                                    // Second row
                                    item {
                                        Box(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            val secondRowState = rememberLazyListState()
                                            val showSecondRowScroll by remember {
                                                derivedStateOf {
                                                    secondRowState.firstVisibleItemIndex > 0
                                                }
                                            }
                                            
                                            LazyRow(
                                                state = secondRowState,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 32.dp),
                                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                                            ) {
                                                items(sortedPersons.filterIndexed { index, _ -> index % 2 == 1 }) { person ->
                                                    PersonCard(
                                                        person = person,
                                                        onPersonClick = { onPersonClick(person.pcode.toString()) },
                                                        modifier = Modifier.width(300.dp)
                                                    )
                                                }
                                            }
                                            
                                            if (showSecondRowScroll) {
                                                FloatingActionButton(
                                                    onClick = {
                                                        coroutineScope.launch {
                                                            secondRowState.animateScrollToItem(0)
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .align(Alignment.CenterEnd)
                                                        .padding(end = 8.dp),
                                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                                ) {
                                                    Icon(
                                                        Icons.Default.KeyboardArrowRight,
                                                        contentDescription = "Scroll right",
                                                        modifier = Modifier.size(32.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (searchQuery.isNotEmpty()) {
                                Text(
                                    text = "기록 없음. 안내 데스크 접수하세요. No results found. please contact the staff desk.",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 