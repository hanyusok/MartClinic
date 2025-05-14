package com.example.martclinic.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.martclinic.domain.model.Mtr
import com.example.martclinic.ui.viewmodel.MtrViewModel
import com.example.martclinic.util.DateUtils
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.Instant
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MtrScreen(
    viewModel: MtrViewModel = hiltViewModel()
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
                title = { Text("MTR List - ${displayDate}") },
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
                                uiState.error!!.contains("404") -> "환자 없음"
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
                uiState.mtrList.isEmpty() || (uiState.error != null && uiState.error!!.contains("No records found")) -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "환자 없음",
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
                        items(uiState.mtrList) { mtr ->
                            MtrItem(
                                mtr = mtr,
                                onEdit = { /* TODO: Implement edit */ },
                                onDelete = { viewModel.deleteMtr(mtr.pcode, mtr.visidate) }
                            )
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AddMtrDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { pcode, pname, pbirth, phonenum ->
                    val currentDateTime = DateUtils.getCurrentIsoDateTime()
                    val currentDate = DateUtils.getCurrentIsoDateTime()
                    val mtr = Mtr(
                        pcode = pcode,
                        visidate = currentDate,
                        visitime = currentDateTime,
                        pname = pname,
                        serial = 0,  // Default serial number
                        pbirth = formatBirthDate(pbirth),
                        age = calculateAge(pbirth),
                        phonenum = phonenum,
                        sex = "1"  // Default sex value
                    )
                    viewModel.createMtr(mtr)
                    showAddDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMtrDialog(
    onDismiss: () -> Unit,
    onConfirm: (pcode: Int, pname: String, pbirth: String, phonenum: String) -> Unit
) {
    var pcode by remember { mutableStateOf("") }
    var pname by remember { mutableStateOf("") }
    var pbirth by remember { mutableStateOf("") }
    var phonenum by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New MTR") },
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
                    value = pname,
                    onValueChange = { pname = it },
                    label = { Text("Patient Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = pbirth,
                    onValueChange = { pbirth = it },
                    label = { Text("Birth Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phonenum,
                    onValueChange = { phonenum = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val pcodeInt = pcode.toIntOrNull() ?: 0
                    if (pcodeInt > 0 && pname.isNotBlank() && pbirth.isNotBlank()) {
                        onConfirm(pcodeInt, pname, pbirth, phonenum)
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
fun MtrItem(
    mtr: Mtr,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
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
                Text(
                    text = mtr.pname,
                    style = MaterialTheme.typography.titleMedium
                )
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Time: ${formatTime(mtr.visitime)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "PCODE: ${mtr.pcode}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Age: ${mtr.age}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Sex: ${mtr.sex}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (!mtr.phonenum.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Phone: ${mtr.phonenum}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun formatTime(timeString: String): String {
    return try {
        DateUtils.formatTimeOnly(timeString)
    } catch (e: Exception) {
        timeString
    }
}

private fun formatBirthDate(birthDate: String): String {
    return try {
        // Convert YYYY-MM-DD to ISO 8601 format with timezone
        val localDateTime = LocalDateTime.parse("${birthDate}T00:00:00")
        val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        instant.toString() // This will give format like "1973-11-11T15:00:00.000Z"
    } catch (e: Exception) {
        birthDate
    }
}

private fun calculateAge(birthDate: String): String {
    return try {
        // Parse birth date from ISO 8601 format
        val instant = Instant.parse(birthDate)
        val birth = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val now = LocalDateTime.now()
        val years = now.year - birth.year
        val months = now.monthValue - birth.monthValue
        val adjustedMonths = if (months < 0) months + 12 else months
        "${years}y ${adjustedMonths}m"
    } catch (e: Exception) {
        "Unknown"
    }
}

fun generateRandomN(): Int {
    val min = 100
    val max = 40000
    val step = 100
    val range = (max - min) / step + 1
    val randomIndex = (0 until range).random()
    return min + (randomIndex * step)
} 