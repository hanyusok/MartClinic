package com.example.martclinic.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.martclinic.domain.model.Person
import com.example.martclinic.domain.model.Mtr
import com.example.martclinic.ui.viewmodel.PersonViewModel
import com.example.martclinic.ui.viewmodel.MtrViewModel
import com.example.martclinic.ui.components.error.ErrorContent
import com.example.martclinic.ui.components.error.NavigationErrorHandler
import com.example.martclinic.util.DateUtils
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun PersonDetailField(
    label: String,
    value: String,
    isEditing: Boolean = false,
    onValueChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        readOnly = !isEditing,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailScreen(
    pcode: Int,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: PersonViewModel = hiltViewModel(),
    mtrViewModel: MtrViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentPerson by viewModel.currentPerson.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var shouldNavigateHome by remember { mutableStateOf(false) }

    LaunchedEffect(shouldNavigateHome) {
        if (shouldNavigateHome) {
            snackbarHostState.showSnackbar(
                message = "진료 신청이 완료되었습니다. Appointment completed.",
                duration = SnackbarDuration.Short
            )
            onNavigateToHome()
            shouldNavigateHome = false
        }
    }

    LaunchedEffect(pcode) {
        viewModel.getPerson(pcode)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("검색 결과 Search Result") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                currentPerson != null -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(48.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Left Column - Person Information
                        Column(
                            modifier = Modifier
                                .weight(0.7f)
                                .fillMaxHeight()
                        ) {
                            PersonInfoCard(person = currentPerson!!)
                        }

                        // Right Column - Action Buttons
                        Column(
                            modifier = Modifier
                                .weight(0.3f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = { showConfirmDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    "신청 Confirm",
                                    style = MaterialTheme.typography.displayMedium
                                )
                            }

                            Spacer(modifier = Modifier.height(96.dp))

                            Button(
                                onClick = onNavigateBack,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text(
                                    "취소 Cancel",
                                    style = MaterialTheme.typography.displayMedium
                                )
                            }
                        }
                    }
                }
            }

            NavigationErrorHandler(
                error = uiState.errorMessage,
                onErrorShown = { viewModel.clearError() }
            )

            if (showConfirmDialog && currentPerson != null) {
                AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    title = { 
                        Text(
                            "재확인 Re-Confirm",
                            style = MaterialTheme.typography.headlineMedium
                        ) 
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Warning",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(
                                    "본인이 아님에도 불구하고, 타인 이름으로 거짓으로 접수하면, 형사 고발되어, 2년이하 징역형 (또는 2천만원 이하) 처벌됩니다.",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            HorizontalDivider()
                            Text(
                                "${currentPerson!!.pname} ${when (currentPerson!!.sex) {
                                    "1", "3" -> "(남성)"
                                    "2", "4" -> "(여성)"
                                    else -> "Unknown"
                                }} , ${currentPerson!!.pbirth?.let { DateUtils.formatKoreanDate(it) } ?: "N/A"} (생일)",
                                style = MaterialTheme.typography.titleLarge
                            )

                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { newValue ->
                                    // Only allow up to 8 digits
                                    val digitsOnly = newValue.filter { it.isDigit() }.take(8)
                                    phoneNumber = digitsOnly
                                },
                                label = { 
                                    Text(
                                        "핸드폰 Mobile (필수 Necessary)",
                                        style = MaterialTheme.typography.titleLarge
                                    ) 
                                },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = MaterialTheme.typography.titleLarge,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true,
                                placeholder = {
                                    Text(
                                        "(010없이)XXXXXXXX (\"-\"없이)",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                    )
                                }
                            )
                        }
                    },
                    confirmButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    val pcode = currentPerson!!.pcode
                                    if (pcode != null && phoneNumber.length == 8) {
                                        val currentDateTime = DateUtils.getCurrentIsoDateTime()
                                        // Format phone number with hyphen
                                        val formattedPhone = "${phoneNumber.substring(0, 4)}-${phoneNumber.substring(4)}"
                                        val mtr = Mtr(
                                            pcode = pcode,
                                            visidate = currentDateTime,
                                            visitime = currentDateTime,
                                            serial = currentPerson!!.serial ?: 1,
                                            pname = currentPerson!!.pname ?: "",
                                            pbirth = currentPerson!!.pbirth ?: "",
                                            sex = currentPerson!!.sex ?: "",
                                            age = calculateAge(currentPerson!!.pbirth ?: ""),
                                            phonenum = formattedPhone,
                                            n = generateRandomN(),
                                            gubun = "요양",
                                            reserved = " ",
                                            fin = " "
                                        )
                                        mtrViewModel.createMtr(mtr)
                                        showConfirmDialog = false
                                        shouldNavigateHome = true
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                ),
                                enabled = phoneNumber.length == 8
                            ) {
                                Text(
                                    "확인 Confirm",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            Button(
                                onClick = { showConfirmDialog = false },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text(
                                    "취소 Cancel",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    },
                    dismissButton = { }
                )
            }
        }
    }
}

@Composable
private fun PersonInfoCard(person: Person) {
    Card(
        modifier = Modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Basic Information Section
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "인적 사항 Basic Information",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider()
                InfoRow("이름 Name", person.pname ?: "N/A")
                InfoRow("생년월일 Date of Birth", person.pbirth?.let { DateUtils.formatKoreanDate(it) } ?: "N/A")
                InfoRow("성별 Gender", when (person.sex) {
                    "1" -> "남성 Male"
                    "2" -> "여성 Female"
                    "3" -> "남성 Male"
                    "4" -> "여성 Female"
                    else -> "N/A"
                })
            }

            Spacer(modifier = Modifier.height(96.dp))

            // Agreement Section
            Column(
                verticalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                HorizontalDivider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                    text = "정보활용 동의합니다 Agreement",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                InfoRow("", when (person.agree) {
                    "1" -> "예 Yes"
                    "0" -> "예 Yes"
                    else -> "N/A"
                })
                }

                
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun calculateAge(birthDate: String): String {
    return try {
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