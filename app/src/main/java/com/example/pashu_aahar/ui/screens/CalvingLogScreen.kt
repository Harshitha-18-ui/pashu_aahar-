package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChildFriendly
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class CalvingEntry(
    val id: String = UUID.randomUUID().toString(),
    val damName: String,
    val calfSex: String,
    val weight: String,
    val date: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalvingLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val logs = remember { 
        mutableStateListOf(
            CalvingEntry(damName = "Lakshmi", calfSex = "Female", weight = "32 kg")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calving Log") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Calving Record")
            }
        }
    ) { padding ->
        if (logs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No calving records yet", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(logs.reversed()) { entry ->
                    CalvingLogItem(entry)
                }
            }
        }

        if (showDialog) {
            AddCalvingDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, sex, w ->
                    logs.add(CalvingEntry(damName = name, calfSex = sex, weight = w))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun CalvingLogItem(entry: CalvingEntry) {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ChildFriendly, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Dam: ${entry.damName}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Sex: ${entry.calfSex}", style = MaterialTheme.typography.bodyMedium)
                Text(sdf.format(Date(entry.date)), style = MaterialTheme.typography.bodySmall)
            }
            Text(entry.weight, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun AddCalvingDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("Female") }
    var weight by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Calving Record") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Dam (Cow) Name") }, modifier = Modifier.fillMaxWidth())
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = sex == "Female", onClick = { sex = "Female" })
                    Text("Female")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = sex == "Male", onClick = { sex = "Male" })
                    Text("Male")
                }
                OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Birth Weight (kg)") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onConfirm(name, sex, weight) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
