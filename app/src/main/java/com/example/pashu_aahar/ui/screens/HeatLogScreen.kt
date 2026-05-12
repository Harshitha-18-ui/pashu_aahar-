package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class HeatEntry(
    val id: String = UUID.randomUUID().toString(),
    val cowName: String,
    val heatObservedAt: Long,
    val status: String, // "Observed", "AI Done", "Confirmed"
    val nextExpectedHeat: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeatLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val heatLogs = remember { 
        mutableStateListOf(
            HeatEntry(
                cowName = "Lakshmi",
                heatObservedAt = System.currentTimeMillis() - 21L * 24 * 60 * 60 * 1000,
                status = "AI Done",
                nextExpectedHeat = System.currentTimeMillis()
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Heat & AI Tracker") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Heat Record")
            }
        }
    ) { padding ->
        if (heatLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No heat records found", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(heatLogs.reversed()) { entry ->
                    HeatLogItem(entry)
                }
            }
        }

        if (showDialog) {
            AddHeatDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, status ->
                    val now = System.currentTimeMillis()
                    // Cow heat cycle is approx 21 days
                    val nextHeat = now + (21L * 24 * 60 * 60 * 1000)
                    heatLogs.add(HeatEntry(
                        cowName = name,
                        heatObservedAt = now,
                        status = status,
                        nextExpectedHeat = nextHeat
                    ))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun HeatLogItem(entry: HeatEntry) {
    val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
    val sdfDate = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.width(12.dp))
                Text(entry.cowName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Badge(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
                    Text(entry.status)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Observed: ${sdf.format(Date(entry.heatObservedAt))}", style = MaterialTheme.typography.bodyMedium)
            Text("Next Expected Heat: ${sdfDate.format(Date(entry.nextExpectedHeat))}", 
                style = MaterialTheme.typography.bodyMedium, 
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun AddHeatDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Observed") }
    val statuses = listOf("Observed", "AI Done")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Heat Record") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Cow Name") })
                Text("Current Status:", style = MaterialTheme.typography.labelMedium)
                Row {
                    statuses.forEach { s ->
                        FilterChip(
                            selected = status == s,
                            onClick = { status = s },
                            label = { Text(s) },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onConfirm(name, status) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
