package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class LaborEntry(
    val id: String = UUID.randomUUID().toString(),
    val workerName: String,
    val workType: String,
    val date: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaborLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val laborLogs = remember { 
        mutableStateListOf(
            LaborEntry(workerName = "Ramesh", workType = "Shed Cleaning"),
            LaborEntry(workerName = "Suresh", workType = "Milking")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Labor Attendance Log") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Entry")
            }
        }
    ) { padding ->
        if (laborLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No labor records yet", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(laborLogs.reversed()) { entry ->
                    LaborLogItem(entry)
                }
            }
        }

        if (showDialog) {
            AddLaborDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, type ->
                    laborLogs.add(LaborEntry(workerName = name, workType = type))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun LaborLogItem(entry: LaborEntry) {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Badge, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.workerName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(entry.workType, style = MaterialTheme.typography.bodyMedium)
                Text(sdf.format(Date(entry.date)), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun AddLaborDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Labor Attendance") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Worker Name") })
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Work Type") })
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onConfirm(name, type) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
