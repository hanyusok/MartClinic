package com.example.martclinic.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.martclinic.domain.model.MtsWait
import com.example.martclinic.ui.viewmodel.MtsWaitViewModel
import com.example.martclinic.util.DateUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MtsWaitScreen(
    viewModel: MtsWaitViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    // Get today's date in YYYYMMDD format for API
    val today = remember {
        val currentDateTime = LocalDateTime.now()
        currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    }

    // Format date for display using DateUtils
    val displayDate = remember {
        try {
            val dateTime = LocalDateTime.parse("${today}T00:00:00")
            DateUtils.formatDateOnly(DateUtils.formatToIso(dateTime))
        } catch (e: Exception) {
            today
        }
    }

    // Load data for today's date when screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadByVisitDate(today)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MTS Wait List - ${displayDate}") },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null && !uiState.error!!.contains("No records found") -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = when {
                                uiState.error!!.contains("404") -> "대기 인원 없음"
                                else -> "An error occurred. Please try again."
                            },
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadByVisitDate(today) }
                        ) {
                            Text("Retry")
                        }
                    }
                }
                uiState.mtsWaitList.isEmpty() || (uiState.error != null && uiState.error!!.contains("No records found")) -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "대기 인원 없음",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.mtsWaitList) { mtsWait ->
                            MtsWaitItem(
                                mtsWait = mtsWait
                            )
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AddMtsWaitDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { pcode, displayname, resid1, resid2 ->
                    viewModel.addMtsWait(
                        pcode = pcode,
                        displayname = displayname,
                        resid1 = resid1,
                        resid2 = resid2,
                        visidate = DateUtils.getCurrentIsoDateTime()
                    )
                    showAddDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMtsWaitDialog(
    onDismiss: () -> Unit,
    onConfirm: (pcode: Int, displayname: String, resid1: String, resid2: String) -> Unit
) {
    var pcode by remember { mutableStateOf("") }
    var displayname by remember { mutableStateOf("") }
    var resid1 by remember { mutableStateOf("") }
    var resid2 by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New MTS Wait") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = pcode,
                    onValueChange = { pcode = it },
                    label = { Text("Patient Code") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = displayname,
                    onValueChange = { displayname = it },
                    label = { Text("Display Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = resid1,
                    onValueChange = { resid1 = it },
                    label = { Text("Residence 1") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = resid2,
                    onValueChange = { resid2 = it },
                    label = { Text("Residence 2") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val pcodeInt = pcode.toIntOrNull() ?: 0
                    if (pcodeInt > 0 && displayname.isNotBlank()) {
                        onConfirm(pcodeInt, displayname, resid1, resid2)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MtsWaitItem(
    mtsWait: MtsWait
) {
    // Parse and format the time using DateUtils
    val timeText = remember(mtsWait.visidate) {
        try {
            val dateTime = DateUtils.parseIsoDate(mtsWait.visidate)
            dateTime?.let { DateUtils.formatTimeOnly(DateUtils.formatToIso(it)) } ?: mtsWait.visidate
        } catch (e: Exception) {
            android.util.Log.e("TimeConversion", "Error converting time", e)
            mtsWait.visidate
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Text(
                    text = mtsWait.displayname ?: "직원접수 Unknown",
                    style = MaterialTheme.typography.titleMedium
                )
                Row {
                    IconButton(onClick = { /* TODO: Implement edit */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { /* TODO: Implement delete */ }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.Text(
                text = "Time: $timeText",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            androidx.compose.material3.Text(
                text = "PCODE: ${mtsWait.pcode}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (mtsWait.resid1.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                androidx.compose.material3.Text(
                    text = "Residence 1: ${mtsWait.resid1}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (mtsWait.resid2.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                androidx.compose.material3.Text(
                    text = "Residence 2: ${mtsWait.resid2}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

