package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class MilkCollectionEntry(
    val id: String = UUID.randomUUID().toString(),
    val cowName: String,
    val shift: String, // "Morning", "Evening"
    val quantity: Float,
    val date: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilkCollectionLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val collectionLogs = remember { 
        mutableStateListOf(
            MilkCollectionEntry(cowName = "Lakshmi", shift = "Morning", quantity = 8.5f),
            MilkCollectionEntry(cowName = "Lakshmi", shift = "Evening", quantity = 6.0f)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Milk Collection Log") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Collection")
            }
        }
    ) { padding ->
        if (collectionLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No collection records yet", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(collectionLogs.reversed()) { entry ->
                    CollectionItem(entry)
                }
            }
        }

        if (showDialog) {
            AddCollectionDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, shift, qty ->
                    collectionLogs.add(MilkCollectionEntry(cowName = name, shift = shift, quantity = qty))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun CollectionItem(entry: MilkCollectionEntry) {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (entry.shift == "Morning") Icons.Default.WbSunny else Icons.Default.WbTwilight,
                contentDescription = null,
                tint = if (entry.shift == "Morning") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.cowName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("${entry.shift} Shift", style = MaterialTheme.typography.bodySmall)
                Text(sdf.format(Date(entry.date)), style = MaterialTheme.typography.labelSmall)
            }
            Text("${entry.quantity} L", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun AddCollectionDialog(onDismiss: () -> Unit, onConfirm: (String, String, Float) -> Unit) {
    var name by remember { mutableStateOf("") }
    var shift by remember { mutableStateOf("Morning") }
    var qty by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Milk Collection") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Cow Name") }, modifier = Modifier.fillMaxWidth())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = shift == "Morning", onClick = { shift = "Morning" })
                    Text("Morning")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = shift == "Evening", onClick = { shift = "Evening" })
                    Text("Evening")
                }
                OutlinedTextField(value = qty, onValueChange = { qty = it }, label = { Text("Quantity (Liters)") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = { 
                val q = qty.toFloatOrNull() ?: 0f
                if (name.isNotBlank() && q > 0) onConfirm(name, shift, q) 
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
