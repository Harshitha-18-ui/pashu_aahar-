package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CowProfileScreen(onBack: () -> Unit) {
    var currentStep by remember { mutableIntStateOf(1) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cow Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress Indicator
            LinearProgressIndicator(
                progress = { currentStep / 3f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
            )
            
            Text(
                text = "Step $currentStep of 3",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            when (currentStep) {
                1 -> StepOne()
                2 -> StepTwo()
                3 -> StepThree()
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep > 1) {
                    OutlinedButton(
                        onClick = { currentStep-- },
                        modifier = Modifier.weight(1f).height(56.dp)
                    ) {
                        Text("Back")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
                
                Button(
                    onClick = { if (currentStep < 3) currentStep++ else onBack() },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(if (currentStep == 3) "Generate Recipe" else "Next")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepOne() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Breed Type", style = MaterialTheme.typography.titleMedium)
        var expanded by remember { mutableStateOf(false) }
        var selectedBreed by remember { mutableStateOf("Jersey") }
        
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedBreed,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                shape = MaterialTheme.shapes.medium
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(text = { Text("Jersey") }, onClick = { selectedBreed = "Jersey"; expanded = false })
                DropdownMenuItem(text = { Text("Desi") }, onClick = { selectedBreed = "Desi"; expanded = false })
                DropdownMenuItem(text = { Text("Holstein") }, onClick = { selectedBreed = "Holstein"; expanded = false })
            }
        }
        
        Text("Age (Years)", style = MaterialTheme.typography.titleMedium)
        var age by remember { mutableFloatStateOf(5f) }
        Slider(
            value = age,
            onValueChange = { age = it },
            valueRange = 1f..15f,
            steps = 14
        )
        Text("${age.toInt()} Years", modifier = Modifier.align(Alignment.End))
        
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )
    }
}

@Composable
fun StepTwo() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Milk Yield per Day (Liters)", style = MaterialTheme.typography.titleMedium)
        var yield by remember { mutableFloatStateOf(10f) }
        Slider(
            value = yield,
            onValueChange = { yield = it },
            valueRange = 0f..50f
        )
        Text("${yield.toInt()} L", modifier = Modifier.align(Alignment.End))
        
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Target Yield (Liters)") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )
    }
}

@Composable
fun StepThree() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Summary", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            HorizontalDivider()
            SummaryItem("Breed", "Jersey")
            SummaryItem("Age", "5 Years")
            SummaryItem("Weight", "450 kg")
            SummaryItem("Current Yield", "12 L/day")
            SummaryItem("Target Yield", "15 L/day")
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}
