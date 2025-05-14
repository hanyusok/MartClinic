package com.example.martclinic.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.martclinic.ui.viewmodel.CreatePersonViewModel
import com.example.martclinic.ui.viewmodel.UiState
import com.example.martclinic.ui.viewmodel.onError
import com.example.martclinic.util.DateUtils
import com.example.martclinic.util.StringUtils

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonCreateScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    viewModel: CreatePersonViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    // 주민등록번호와 생일 상태
    var rrnInput by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf<String?>(null) }

    // 주민등록번호 입력 시 생일 추출
    LaunchedEffect(rrnInput) {
        birthDate = StringUtils.extractBirthDateFromRRN(rrnInput)
        // 생일을 formState에 저장하려면 ViewModel에 업데이트
        viewModel.updateFormField("birthDate", birthDate ?: "")
        viewModel.updateFormField("oldidnum", rrnInput)
        // Set the correctly formatted SEARCHID
        StringUtils.extractSearchId(rrnInput)?.let { searchId ->
            viewModel.updateFormField("searchId", searchId)
        }
    }

    // Handle successful creation
    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            val createdPerson = (uiState as UiState.Success).data
            createdPerson.pcode?.let { pcode ->
                onNavigateToDetail(pcode)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("신환 등록 Create Person") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            formState?.let { person ->
                                viewModel.createPerson(person)
                            }
                        },
                        enabled = !uiState.isLoading && formState != null
                    ) {
                        Icon(Icons.Rounded.CheckCircle, "확인 Save")
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
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 이름(Full Name)
                    PersonDetailField(
                        label = "이름(Full Name)",
                        value = formState?.pname ?: "",
                        isEditing = true,
                        onValueChange = { viewModel.updateFormField("pname", it) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    // 주민번호(13자리) oldidnum 저장
                    PersonDetailField(
                        label = "주민번호(13자리) '-'없이 입력",
                        value = rrnInput,
                        isEditing = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { newValue ->
                            // 숫자만 허용하고 최대 13자리로 제한
                            val digitsOnly = newValue.filter { it.isDigit() }
                            if (digitsOnly.length <= 13) {
                                rrnInput = digitsOnly
                            }
                        },
                    )

                    // SEARCHID 표시 (자동 생성)
                    StringUtils.extractSearchId(rrnInput)?.let { searchId ->
                        Text(
                            text = "SEARCHID: $searchId",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                    )
                    }

                    // 생일 표시
                    birthDate?.let {
                        Text(
                            text = "생일: ${DateUtils.formatDateOnly(it)}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } ?: Text(
                        text = "유효한 주민등록번호를 입력하세요",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // 핸드폰 번호 memo1
                    PersonDetailField(
                        label = "핸드폰 번호 '-'없이 입력",
                        value = formState?.memo1 ?: "",
                        isEditing = true,
                        onValueChange = { newValue ->
                            // 숫자만 허용하고 최대 11자리로 제한
                            val digitsOnly = newValue.filter { it.isDigit() }
                            if (digitsOnly.length <= 11) {
                                // Format the phone number before saving
                                StringUtils.formatPhoneNumber(digitsOnly)?.let { formattedNumber ->
                                    viewModel.updateFormField("memo1", formattedNumber)
                                } ?: run {
                                    // If not valid length yet, just save the digits
                                    viewModel.updateFormField("memo1", digitsOnly)
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    // 개인정보 수집 및 이용 동의
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = formState?.agree == "1",
                            onCheckedChange = { isChecked ->
                                viewModel.updateFormField("agree", if (isChecked) "1" else "0")
                            }
                        )
                        Text(
                            text = "개인정보 수집 및 이용 동의",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

            
                }
            }

            uiState.onError { errorMessage ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(errorMessage)
                }
            }
        }
    }
}