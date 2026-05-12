package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class VetVisitEntry(
    val id: String = UUID.randomUUID().toString(),
    val vetName: String,
    val purpose: String,
    val recommendations: String,
    val date: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetVisitLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val visitLogs = remember { 
        mutableStateListOf(
            VetVisitEntry(vetName = "Dr. Sharma", purpose = "Routine Checkup", recommendations = "Increase green fodder intake."),
            VetVisitEntry(vetName = "Dr. Verma", purpose = "Treatment", recommendations = "Apply ointment twice a day.")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vet Visit Log") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Visit Record")
            }
        }
    ) { padding ->
        if (visitLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No visit records yet", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(visitLogs.reversed()) { entry ->
                    VetVisitItem(entry)
                }
            }
        }

        if (showDialog) {
            AddVetVisitDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, purpose, rec ->
                    visitLogs.add(VetVisitEntry(vetName = name, purpose = purpose, recommendations = rec))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun VetVisitItem(entry: VetVisitEntry) {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MedicalServices, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Text(entry.vetName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Text(sdf.format(Date(entry.date)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Purpose: ${entry.purpose}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text("Recommendations: ${entry.recommendations}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun AddVetVisitDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }
    var recommendations by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Vet Visit Record") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Veterinarian Name") })
                OutlinedTextField(value = purpose, onValueChange = { purpose = it }, label = { Text("Purpose of Visit") })
                OutlinedTextField(value = recommendations, onValueChange = { recommendations = it }, label = { Text("Recommendations") })
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onConfirm(name, purpose, recommendations) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
