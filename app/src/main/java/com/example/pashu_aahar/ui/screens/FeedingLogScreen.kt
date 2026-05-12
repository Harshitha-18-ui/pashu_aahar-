package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class FeedingEntry(
    val id: String = UUID.randomUUID().toString(),
    val animalName: String,
    val feedType: String,
    val quantity: String,
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedingLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val feedingLogs = remember { 
        mutableStateListOf(
            FeedingEntry(animalName = "Lakshmi", feedType = "Green Fodder", quantity = "10 kg"),
            FeedingEntry(animalName = "Gauri", feedType = "Concentrate Mix", quantity = "2 kg")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feeding Log") },
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
        if (feedingLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.History, 
                        contentDescription = null, 
                        modifier = Modifier.size(64.dp), 
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        "No logs yet", 
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(feedingLogs.reversed()) { entry ->
                    LogItem(entry)
                }
            }
        }

        if (showDialog) {
            AddLogDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, type, qty ->
                    feedingLogs.add(FeedingEntry(animalName = name, feedType = type, quantity = qty))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun LogItem(entry: FeedingEntry) {
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.animalName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(entry.feedType, style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(entry.quantity, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(sdf.format(Date(entry.timestamp)), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun AddLogDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Feeding Log") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Animal Name") })
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Feed Type") })
                OutlinedTextField(value = qty, onValueChange = { qty = it }, label = { Text("Quantity (e.g. 5kg)") })
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onConfirm(name, type, qty) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
