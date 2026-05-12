package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class SaleEntry(
    val id: String = UUID.randomUUID().toString(),
    val buyerName: String,
    val itemSold: String, // e.g., "Milk", "Cow", "Manure"
    val amount: Double,
    val date: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesLogScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val salesLogs = remember { 
        mutableStateListOf(
            SaleEntry(buyerName = "Dairy Co-op", itemSold = "Milk (50L)", amount = 2500.0),
            SaleEntry(buyerName = "Local Farmer", itemSold = "Organic Manure", amount = 800.0)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sales & Income Log") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Sale")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val totalIncome = salesLogs.sumOf { it.amount }
            
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total Income", style = MaterialTheme.typography.titleMedium)
                    Text("₹${totalIncome}", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }

            if (salesLogs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No sales recorded yet", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(salesLogs.reversed()) { entry ->
                        SaleLogItem(entry)
                    }
                }
            }
        }

        if (showDialog) {
            AddSaleDialog(
                onDismiss = { showDialog = false },
                onConfirm = { buyer, item, amt ->
                    salesLogs.add(SaleEntry(buyerName = buyer, itemSold = item, amount = amt))
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun SaleLogItem(entry: SaleEntry) {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Payments, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.buyerName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(entry.itemSold, style = MaterialTheme.typography.bodyMedium)
                Text(sdf.format(Date(entry.date)), style = MaterialTheme.typography.bodySmall)
            }
            Text("₹${entry.amount}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun AddSaleDialog(onDismiss: () -> Unit, onConfirm: (String, String, Double) -> Unit) {
    var buyer by remember { mutableStateOf("") }
    var item by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record New Sale") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = buyer, onValueChange = { buyer = it }, label = { Text("Buyer Name") })
                OutlinedTextField(value = item, onValueChange = { item = it }, label = { Text("Item Sold") })
                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount (₹)") })
            }
        },
        confirmButton = {
            Button(onClick = { 
                val amt = amount.toDoubleOrNull() ?: 0.0
                if (buyer.isNotBlank() && amt > 0) onConfirm(buyer, item, amt) 
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
