package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class MovementEntry(
    val id: String = UUID.randomUUID().toString(),
    val animalName: String,
    val type: String, // "Purchased", "Sold", "Moved"
    val detail: String,
    val date: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val movements = remember { 
        mutableStateListOf(
            MovementEntry(animalName = "Nandi", type = "Purchased", detail = "From Sonepur Mela"),
            MovementEntry(animalName = "Heifer #5", type = "Moved", detail = "To South Shed")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cattle Movement Log") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Movement")
            }
        }
    ) { padding ->
        if (movements.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No movement records", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movements.reversed()) { entry ->
                    MovementItem(entry)
                }
            }
        }

        if (showDialog) {
            AddMovementDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, type, detail ->
                    movements.add(MovementEntry(animalName = name, type = type, detail = detail))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun MovementItem(entry: MovementEntry) {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.animalName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(entry.detail, style = MaterialTheme.typography.bodySmall)
                Text(sdf.format(Date(entry.date)), style = MaterialTheme.typography.labelSmall)
            }
            Badge(containerColor = when(entry.type) {
                "Purchased" -> MaterialTheme.colorScheme.primaryContainer
                "Sold" -> MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.secondaryContainer
            }) {
                Text(entry.type)
            }
        }
    }
}

@Composable
fun AddMovementDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Purchased") }
    var detail by remember { mutableStateOf("") }
    val types = listOf("Purchased", "Sold", "Moved")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Movement") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Animal Name") })
                Text("Movement Type:", style = MaterialTheme.typography.labelMedium)
                Row {
                    types.forEach { t ->
                        FilterChip(
                            selected = type == t,
                            onClick = { type = t },
                            label = { Text(t) },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
                OutlinedTextField(value = detail, onValueChange = { detail = it }, label = { Text("Details (Location/Buyer/Seller)") })
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onConfirm(name, type, detail) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
