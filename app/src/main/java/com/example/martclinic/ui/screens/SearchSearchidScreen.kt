package com.example.martclinic.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.martclinic.domain.model.Person
import com.example.martclinic.ui.components.PersonCard
import com.example.martclinic.ui.viewmodel.SearchViewModel
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSearchidScreen(
    onNavigateBack: () -> Unit,
    onPersonClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()

    var frontNumber by remember { mutableStateOf(TextFieldValue("")) }
    var backNumber by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("주민등록번호 검색") },
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
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val focusManager = LocalFocusManager.current
                
                OutlinedTextField(
                    value = frontNumber,
                    onValueChange = {
                        if (it.text.length <= 6) frontNumber = it
                    },
                    label = { 
                        Text(
                            "첫6숫자",
                            style = MaterialTheme.typography.headlineMedium
                        ) 
                    },
                    leadingIcon = { 
                        IconButton(
                            onClick = { 
                                if (frontNumber.text.length == 6) {
                                    focusManager.moveFocus(FocusDirection.Next)
                                }
                            },
                            modifier = Modifier.size(64.dp).padding(horizontal = 16.dp)
                        ) {
                            Icon(
                                Icons.Default.Search,
                                "Search",
                                modifier = Modifier.size(76.dp)
                            )
                        }
                    },
                    trailingIcon = if (frontNumber.text.isNotEmpty()) {
                        {
                            IconButton(
                                onClick = { frontNumber = TextFieldValue("") },
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
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if (frontNumber.text.length == 6) {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        }
                    ),
                    textStyle = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(48.dp, 48.dp, 0.dp, 48.dp)
                        .weight(1f)
                        .height(120.dp)
                )

                OutlinedTextField(
                    value = backNumber,
                    onValueChange = {
                        if (it.text.length <= 1) backNumber = it
                    },
                    label = { 
                        Text(
                            "뒷1숫자",
                            style = MaterialTheme.typography.headlineMedium
                        ) 
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (frontNumber.text.length == 6 && backNumber.text.length == 1) {
                                searchQuery = frontNumber.text + '-' + backNumber.text
                                viewModel.searchBySearchId(searchQuery)
                                focusManager.clearFocus()
                            }
                        }
                    ),
                    textStyle = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(0.dp, 48.dp, 48.dp, 48.dp)
                        .width(120.dp)
                        .height(120.dp)
                )

                Button(
                    onClick = {
                        if (frontNumber.text.length == 6 && backNumber.text.length == 1) {
                            searchQuery = frontNumber.text + '-' + backNumber.text
                            viewModel.searchBySearchId(searchQuery)
                            focusManager.clearFocus()
                        }
                    },
                    enabled = frontNumber.text.length == 6 && backNumber.text.length == 1,
                    modifier = Modifier
                        .height(120.dp)
                        .width(240.dp),
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
                        .height(120.dp)
                        .width(240.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("취소 Cancel", style = MaterialTheme.typography.headlineMedium)
                }
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
                        val coroutineScope = rememberCoroutineScope()
                        val listState = rememberLazyListState()
                        
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
                                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
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
                        Column(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "기록 없습니다. 직원에게 접수하세요.",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize * 2
                                ),
                                maxLines = 1,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No results found. Please contact staff.",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize * 2
                                ),
                                maxLines = 1,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
//                        Text(
//                            text = "$searchQuery 일치하는 \n주민번호 없음: \n No Result ",
//                            modifier = Modifier
//                                .align(Alignment.CenterHorizontally)
//                                .padding(16.dp),
//                            style = MaterialTheme.typography.bodyLarge.copy(
//                                fontSize = MaterialTheme.typography.bodyLarge.fontSize * 2,
//                                lineHeight = MaterialTheme.typography.bodyLarge.fontSize * 3
//                            )
//                        )
                    }
                }
            }
        }
    }
} 