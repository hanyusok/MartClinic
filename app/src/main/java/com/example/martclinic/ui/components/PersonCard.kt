package com.example.martclinic.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.martclinic.domain.model.Person
import com.example.martclinic.util.DateUtils
import com.example.martclinic.util.StringUtils

@Composable
fun PersonCard(
    person: Person,
    onPersonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(300.dp)
            .clickable(onClick = onPersonClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Name and Birth Date
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = person.pname ?: "Unknown",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = when (person.sex) {
                        "1", "3" -> "(남성 male)"
                        "2", "4" -> "(여성 female)"
                        else -> "Other"
                    },
                    style = MaterialTheme.typography.titleMedium
                )

                HorizontalDivider()
                Text(
                    text = "생일: ${person.pbirth?.let { DateUtils.formatKoreanDate(it) } ?: "N/A"}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

