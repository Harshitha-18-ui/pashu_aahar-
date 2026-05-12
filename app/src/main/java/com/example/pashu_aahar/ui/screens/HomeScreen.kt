package com.example.pashu_aahar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class DashboardItem(
    val title: String,
    val icon: ImageVector,
    val color: androidx.compose.ui.graphics.Color,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToRecipe: () -> Unit,
    onNavigateToFeedingLog: () -> Unit,
    onNavigateToMilkLog: () -> Unit,
    onNavigateToHealthLog: () -> Unit,
    onNavigateToCost: () -> Unit,
    onNavigateToTips: () -> Unit,
    onNavigateToHeatLog: () -> Unit,
    onNavigateToBreedingLog: () -> Unit,
    onNavigateToCalvingLog: () -> Unit,
    onNavigateToWeightLog: () -> Unit,
    onNavigateToExpenseLog: () -> Unit,
    onNavigateToSalesLog: () -> Unit,
    onNavigateToInventoryLog: () -> Unit,
    onNavigateToLaborLog: () -> Unit,
    onNavigateToWaterLog: () -> Unit,
    onNavigateToManureLog: () -> Unit,
    onNavigateToDiaryLog: () -> Unit,
    onNavigateToDailyTaskLog: () -> Unit,
    onNavigateToMovementLog: () -> Unit,
    onNavigateToVetVisitLog: () -> Unit,
    onNavigateToDewormingLog: () -> Unit,
    onNavigateToInsuranceLog: () -> Unit,
    onNavigateToMilkQualityLog: () -> Unit,
    onNavigateToMilkCollectionLog: () -> Unit
) {
    val items = listOf(
        DashboardItem("Add Cow Profile", Icons.Default.Pets, MaterialTheme.colorScheme.primary, onNavigateToProfile),
        DashboardItem("Feeding Log", Icons.Default.History, MaterialTheme.colorScheme.primary, onNavigateToFeedingLog),
        DashboardItem("Milk Record", Icons.Default.Opacity, MaterialTheme.colorScheme.primary, onNavigateToMilkLog),
        DashboardItem("Health Record", Icons.Default.MedicalInformation, MaterialTheme.colorScheme.primary, onNavigateToHealthLog),
        DashboardItem("Generate Feed", Icons.Default.Grass, MaterialTheme.colorScheme.primary, onNavigateToRecipe),
        DashboardItem("Cost Tracker", Icons.Default.CurrencyRupee, MaterialTheme.colorScheme.primary, onNavigateToCost),
        DashboardItem("Heat Tracker", Icons.Default.Favorite, MaterialTheme.colorScheme.primary, onNavigateToHeatLog),
        DashboardItem("Breeding", Icons.Default.ChildCare, MaterialTheme.colorScheme.primary, onNavigateToBreedingLog),
        DashboardItem("Calving", Icons.Default.ChildFriendly, MaterialTheme.colorScheme.primary, onNavigateToCalvingLog),
        DashboardItem("Weight", Icons.Default.MonitorWeight, MaterialTheme.colorScheme.primary, onNavigateToWeightLog),
        DashboardItem("Expenses", Icons.Default.Payments, MaterialTheme.colorScheme.primary, onNavigateToExpenseLog),
        DashboardItem("Sales", Icons.Default.Sell, MaterialTheme.colorScheme.primary, onNavigateToSalesLog),
        DashboardItem("Inventory", Icons.Default.Inventory, MaterialTheme.colorScheme.primary, onNavigateToInventoryLog),
        DashboardItem("Labor", Icons.Default.Groups, MaterialTheme.colorScheme.primary, onNavigateToLaborLog),
        DashboardItem("Water", Icons.Default.WaterDrop, MaterialTheme.colorScheme.primary, onNavigateToWaterLog),
        DashboardItem("Manure", Icons.Default.Agriculture, MaterialTheme.colorScheme.primary, onNavigateToManureLog),
        DashboardItem("Daily Diary", Icons.Default.Book, MaterialTheme.colorScheme.primary, onNavigateToDiaryLog),
        DashboardItem("Daily Tasks", Icons.AutoMirrored.Filled.Assignment, MaterialTheme.colorScheme.primary, onNavigateToDailyTaskLog),
        DashboardItem("Movement", Icons.Default.TransferWithinAStation, MaterialTheme.colorScheme.primary, onNavigateToMovementLog),
        DashboardItem("Vet Visit", Icons.Default.LocalHospital, MaterialTheme.colorScheme.primary, onNavigateToVetVisitLog),
        DashboardItem("Deworming", Icons.Default.BugReport, MaterialTheme.colorScheme.primary, onNavigateToDewormingLog),
        DashboardItem("Insurance", Icons.Default.VerifiedUser, MaterialTheme.colorScheme.primary, onNavigateToInsuranceLog),
        DashboardItem("Milk Quality", Icons.Default.Grading, MaterialTheme.colorScheme.primary, onNavigateToMilkQualityLog),
        DashboardItem("Collection", Icons.Default.ShoppingBasket, MaterialTheme.colorScheme.primary, onNavigateToMilkCollectionLog),
        DashboardItem("Veterinary Tips", Icons.Default.MedicalServices, MaterialTheme.colorScheme.primary, onNavigateToTips)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pashu-Aahar", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Welcome Farmer 👋",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items) { item ->
                    DashboardCard(item)
                }
            }
        }
    }
}

@Composable
fun DashboardCard(item: DashboardItem) {
    Card(
        onClick = item.onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                modifier = Modifier.size(48.dp),
                tint = item.color
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.title,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
