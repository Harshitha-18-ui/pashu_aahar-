package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class HealthEntry(
    val id: String = UUID.randomUUID().toString(),
    val animalName: String,
    val treatment: String,
    val date: Long = System.currentTimeMillis(),
    val isVaccination: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val healthLogs = remember { 
        mutableStateListOf(
            HealthEntry(animalName = "Lakshmi", treatment = "FMD Vaccination", isVaccination = true),
            HealthEntry(animalName = "Gauri", treatment = "Deworming", isVaccination = false)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health & Vaccination") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Health Record")
            }
        }
    ) { padding ->
        if (healthLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No health records yet", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(healthLogs.reversed()) { entry ->
                    HealthLogItem(entry)
                }
            }
        }

        if (showDialog) {
            AddHealthLogDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, treatment, isVacc ->
                    healthLogs.add(HealthEntry(animalName = name, treatment = treatment, isVaccination = isVacc))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun HealthLogItem(entry: HealthEntry) {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (entry.isVaccination) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.MedicalInformation, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.animalName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(entry.treatment, style = MaterialTheme.typography.bodyMedium)
                Text(sdf.format(Date(entry.date)), style = MaterialTheme.typography.bodySmall)
            }
            if (entry.isVaccination) {
                Badge { Text("Vaccination") }
            }
        }
    }
}

@Composable
fun AddHealthLogDialog(onDismiss: () -> Unit, onConfirm: (String, String, Boolean) -> Unit) {
    var name by remember { mutableStateOf("") }
    var treatment by remember { mutableStateOf("") }
    var isVaccination by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Health Record") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Animal Name") })
                OutlinedTextField(value = treatment, onValueChange = { treatment = it }, label = { Text("Treatment/Medicine") })
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isVaccination, onCheckedChange = { isVaccination = it })
                    Text("Is this a vaccination?")
                }
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onConfirm(name, treatment, isVaccination) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
