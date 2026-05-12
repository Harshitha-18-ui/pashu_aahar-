package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Park
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class ManureEntry(
    val id: String = UUID.randomUUID().toString(),
    val action: String, // "Collected", "Sold", "Used in Field"
    val quantity: String,
    val date: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManureLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val manureLogs = remember { 
        mutableStateListOf(
            ManureEntry(action = "Sold", quantity = "5 Trolleys"),
            ManureEntry(action = "Used in Field", quantity = "2 Trolleys")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manure & Fertilizer Log") },
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
        if (manureLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No manure records yet", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(manureLogs.reversed()) { entry ->
                    ManureLogItem(entry)
                }
            }
        }

        if (showDialog) {
            AddManureDialog(
                onDismiss = { showDialog = false },
                onConfirm = { action, qty ->
                    manureLogs.add(ManureEntry(action = action, quantity = qty))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun ManureLogItem(entry: ManureEntry) {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Park, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.action, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(sdf.format(Date(entry.date)), style = MaterialTheme.typography.bodySmall)
            }
            Text(entry.quantity, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun AddManureDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var action by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Manure Action") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = action, onValueChange = { action = it }, label = { Text("Action (Sold/Used/Collected)") })
                OutlinedTextField(value = qty, onValueChange = { qty = it }, label = { Text("Quantity") })
            }
        },
        confirmButton = {
            Button(onClick = { if (action.isNotBlank()) onConfirm(action, qty) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
