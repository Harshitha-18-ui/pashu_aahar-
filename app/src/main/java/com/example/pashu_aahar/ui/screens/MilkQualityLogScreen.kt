package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class QualityEntry(
    val id: String = UUID.randomUUID().toString(),
    val fat: Float,
    val snf: Float,
    val date: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilkQualityLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val qualityLogs = remember { 
        mutableStateListOf(
            QualityEntry(fat = 4.5f, snf = 8.5f),
            QualityEntry(fat = 3.8f, snf = 8.2f)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Milk Quality Log") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Quality Record")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (qualityLogs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No quality records yet", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(qualityLogs.reversed()) { entry ->
                        QualityLogItem(entry)
                    }
                }
            }
        }

        if (showDialog) {
            AddQualityDialog(
                onDismiss = { showDialog = false },
                onConfirm = { fat, snf ->
                    qualityLogs.add(QualityEntry(fat = fat, snf = snf))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun QualityLogItem(entry: QualityEntry) {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Science, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(sdf.format(Date(entry.date)), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row {
                    Text("Fat: ${entry.fat}%", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("SNF: ${entry.snf}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun AddQualityDialog(onDismiss: () -> Unit, onConfirm: (Float, Float) -> Unit) {
    var fat by remember { mutableStateOf("") }
    var snf by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Milk Quality") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = fat, onValueChange = { fat = it }, label = { Text("Fat %") })
                OutlinedTextField(value = snf, onValueChange = { snf = it }, label = { Text("SNF") })
            }
        },
        confirmButton = {
            Button(onClick = { 
                val f = fat.toFloatOrNull() ?: 0f
                val s = snf.toFloatOrNull() ?: 0f
                if (f > 0 && s > 0) onConfirm(f, s) 
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
