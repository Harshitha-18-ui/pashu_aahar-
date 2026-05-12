package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class InsuranceEntry(
    val id: String = UUID.randomUUID().toString(),
    val cowName: String,
    val policyNumber: String,
    val company: String,
    val expiryDate: Long,
    val sumInsured: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsuranceLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val insuranceLogs = remember { 
        mutableStateListOf(
            InsuranceEntry(
                cowName = "Lakshmi",
                policyNumber = "POL-12345678",
                company = "AIC of India",
                expiryDate = System.currentTimeMillis() + 180L * 24 * 60 * 60 * 1000,
                sumInsured = 65000.0
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cattle Insurance (Bima)") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Policy")
            }
        }
    ) { padding ->
        if (insuranceLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No insurance policies recorded", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(insuranceLogs) { entry ->
                    InsuranceItem(entry)
                }
            }
        }

        if (showDialog) {
            AddInsuranceDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name, pol, comp, amt ->
                    insuranceLogs.add(InsuranceEntry(
                        cowName = name,
                        policyNumber = pol,
                        company = comp,
                        sumInsured = amt,
                        expiryDate = System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000
                    ))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun InsuranceItem(entry: InsuranceEntry) {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Text(entry.cowName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Policy No:", style = MaterialTheme.typography.bodyMedium)
                Text(entry.policyNumber, fontWeight = FontWeight.Medium)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Sum Insured:", style = MaterialTheme.typography.bodyMedium)
                Text("₹${entry.sumInsured}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Valid Till:", style = MaterialTheme.typography.bodyMedium)
                Text(sdf.format(Date(entry.expiryDate)), color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun AddInsuranceDialog(onDismiss: () -> Unit, onConfirm: (String, String, String, Double) -> Unit) {
    var name by remember { mutableStateOf("") }
    var pol by remember { mutableStateOf("") }
    var comp by remember { mutableStateOf("") }
    var amt by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Insurance Policy") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Cow Name") })
                OutlinedTextField(value = pol, onValueChange = { pol = it }, label = { Text("Policy Number") })
                OutlinedTextField(value = comp, onValueChange = { comp = it }, label = { Text("Company Name") })
                OutlinedTextField(value = amt, onValueChange = { amt = it }, label = { Text("Sum Insured (₹)") })
            }
        },
        confirmButton = {
            Button(onClick = { 
                val valAmt = amt.toDoubleOrNull() ?: 0.0
                if (name.isNotBlank()) onConfirm(name, pol, comp, valAmt) 
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
