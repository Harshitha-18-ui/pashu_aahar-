package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class WaterEntry(
    val id: String = UUID.randomUUID().toString(),
    val animalName: String,
    val quantityLiters: Float,
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val waterLogs = remember { 
        mutableStateListOf(
            WaterEntry(animalName = "Lakshmi", quantityLiters = 45f),
            WaterEntry(animalName = "Gauri", quantityLiters = 38f)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Water Intake Log") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Water Log")
            }
        }
    ) { padding ->
        if (waterLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No water logs yet", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(waterLogs.reversed()) { entry ->
                    WaterLogItem(entry)
                }
            }
        }

        if (showDialog) {
            AddWaterLogDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, qty ->
                    waterLogs.add(WaterEntry(animalName = name, quantityLiters = qty))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun WaterLogItem(entry: WaterEntry) {
    val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.WaterDrop, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.animalName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(sdf.format(Date(entry.timestamp)), style = MaterialTheme.typography.bodySmall)
            }
            Text("${entry.quantityLiters} L", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun AddWaterLogDialog(onDismiss: () -> Unit, onConfirm: (String, Float) -> Unit) {
    var name by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Water Intake") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Animal Name") })
                OutlinedTextField(value = qty, onValueChange = { qty = it }, label = { Text("Quantity (Liters)") })
            }
        },
        confirmButton = {
            Button(onClick = { 
                val q = qty.toFloatOrNull() ?: 0f
                if (name.isNotBlank() && q > 0) onConfirm(name, q) 
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
