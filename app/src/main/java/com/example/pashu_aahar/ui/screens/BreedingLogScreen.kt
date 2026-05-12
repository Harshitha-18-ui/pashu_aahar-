package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class BreedingEntry(
    val id: String = UUID.randomUUID().toString(),
    val cowName: String,
    val serviceDate: Long,
    val bullDetails: String,
    val expectedCalvingDate: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedingLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val breedingLogs = remember { 
        mutableStateListOf(
            BreedingEntry(
                cowName = "Lakshmi",
                serviceDate = System.currentTimeMillis() - 45L * 24 * 60 * 60 * 1000,
                bullDetails = "Murrah Bull ID: 502",
                expectedCalvingDate = System.currentTimeMillis() + 235L * 24 * 60 * 60 * 1000
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Breeding & Pregnancy Log") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Breeding Record")
            }
        }
    ) { padding ->
        if (breedingLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No breeding records found", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(breedingLogs.reversed()) { entry ->
                    BreedingItem(entry)
                }
            }
        }

        if (showDialog) {
            AddBreedingDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, bull, date ->
                    // Average gestation period for cows is 283 days
                    val calvingDate = date + (283L * 24 * 60 * 60 * 1000)
                    breedingLogs.add(BreedingEntry(
                        cowName = name,
                        bullDetails = bull,
                        serviceDate = date,
                        expectedCalvingDate = calvingDate
                    ))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun BreedingItem(entry: BreedingEntry) {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ChildCare, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Text(entry.cowName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Service Date:", style = MaterialTheme.typography.bodyMedium)
                Text(sdf.format(Date(entry.serviceDate)), fontWeight = FontWeight.Medium)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Bull Details:", style = MaterialTheme.typography.bodyMedium)
                Text(entry.bullDetails, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Exp. Calving:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
                Text(sdf.format(Date(entry.expectedCalvingDate)), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun AddBreedingDialog(onDismiss: () -> Unit, onConfirm: (String, String, Long) -> Unit) {
    var name by remember { mutableStateOf("") }
    var bull by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Breeding Record") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Cow Name") })
                OutlinedTextField(value = bull, onValueChange = { bull = it }, label = { Text("Bull/Semen ID") })
                Text("Service date will be set to today.", style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onConfirm(name, bull, System.currentTimeMillis()) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
