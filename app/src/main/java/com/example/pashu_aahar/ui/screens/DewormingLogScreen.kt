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

data class DewormingEntry(
    val id: String = UUID.randomUUID().toString(),
    val animalName: String,
    val medicineName: String,
    val date: Long = System.currentTimeMillis(),
    val nextDueDate: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DewormingLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val logs = remember { 
        mutableStateListOf(
            DewormingEntry(
                animalName = "Lakshmi",
                medicineName = "Albendazole",
                nextDueDate = System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Deworming Log") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Record")
            }
        }
    ) { padding ->
        if (logs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No deworming records", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(logs.reversed()) { entry ->
                    DewormingItem(entry)
                }
            }
        }

        if (showDialog) {
            AddDewormingDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, medicine ->
                    val nextDue = System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000
                    logs.add(DewormingEntry(animalName = name, medicineName = medicine, nextDueDate = nextDue))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun DewormingItem(entry: DewormingEntry) {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.MedicalInformation, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.animalName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(entry.medicineName, style = MaterialTheme.typography.bodyMedium)
                Text("Given on: ${sdf.format(Date(entry.date))}", style = MaterialTheme.typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Next Due", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                Text(sdf.format(Date(entry.nextDueDate)), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun AddDewormingDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var medicine by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Deworming Record") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Animal Name") })
                OutlinedTextField(value = medicine, onValueChange = { medicine = it }, label = { Text("Medicine Name") })
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onConfirm(name, medicine) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
