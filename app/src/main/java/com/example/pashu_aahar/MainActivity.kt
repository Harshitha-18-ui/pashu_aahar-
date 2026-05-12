package com.example.pashu_aahar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.pashu_aahar.ui.screens.*
import com.example.pashu_aahar.ui.theme.Pashu_aaharTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pashu_aaharTheme {
                MainApp()
            }
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Recipe : Screen("recipe", "Recipe", Icons.Default.Grass)
    data object FeedingLog : Screen("feeding_log", "Feeding", Icons.Default.History)
    data object MilkLog : Screen("milk_log", "Milk", Icons.Default.Opacity)
    data object HealthLog : Screen("health_log", "Health", Icons.Default.MedicalInformation)
    data object Cost : Screen("cost", "Cost", Icons.Default.CurrencyRupee)
    data object Tips : Screen("tips", "Tips", Icons.Default.MedicalServices)
    data object CowProfile : Screen("cow_profile", "Add Profile", Icons.Default.Pets)
    
    // New Screens
    data object HeatLog : Screen("heat_log", "Heat Record", Icons.Default.Favorite)
    data object BreedingLog : Screen("breeding_log", "Breeding", Icons.Default.ChildCare)
    data object CalvingLog : Screen("calving_log", "Calving", Icons.Default.ChildFriendly)
    data object WeightLog : Screen("weight_log", "Weight", Icons.Default.MonitorWeight)
    data object ExpenseLog : Screen("expense_log", "Expenses", Icons.Default.Payments)
    data object SalesLog : Screen("sales_log", "Sales", Icons.Default.Sell)
    data object InventoryLog : Screen("inventory_log", "Inventory", Icons.Default.Inventory)
    data object LaborLog : Screen("labor_log", "Labor", Icons.Default.Groups)
    data object WaterLog : Screen("water_log", "Water", Icons.Default.WaterDrop)
    data object ManureLog : Screen("manure_log", "Manure", Icons.Default.Agriculture)
    data object DiaryLog : Screen("diary_log", "Daily Diary", Icons.Default.Book)
    data object DailyTaskLog : Screen("task_log", "Tasks", Icons.Default.Assignment)
    data object MovementLog : Screen("movement_log", "Movement", Icons.Default.TransferWithinAStation)
    data object VetVisitLog : Screen("vet_visit", "Vet Visit", Icons.Default.LocalHospital)
    data object DewormingLog : Screen("deworming", "Deworming", Icons.Default.BugReport)
    data object InsuranceLog : Screen("insurance", "Insurance", Icons.Default.VerifiedUser)
    data object MilkQualityLog : Screen("milk_quality", "Milk Quality", Icons.Default.Grading)
    data object MilkCollectionLog : Screen("milk_collection", "Collection", Icons.Default.ShoppingBasket)
}

@Composable
fun MainApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    
    val bottomNavItems = listOf(
        Screen.Home,
        Screen.FeedingLog,
        Screen.MilkLog,
        Screen.HealthLog
    )

    Scaffold(
        bottomBar = {
            if (currentScreen == Screen.Home || currentScreen in bottomNavItems) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.label) },
                            label = { Text(screen.label) },
                            selected = currentScreen == screen,
                            onClick = { currentScreen = screen }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                is Screen.Home -> HomeScreen(
                    onNavigateToProfile = { currentScreen = Screen.CowProfile },
                    onNavigateToFeedingLog = { currentScreen = Screen.FeedingLog },
                    onNavigateToMilkLog = { currentScreen = Screen.MilkLog },
                    onNavigateToHealthLog = { currentScreen = Screen.HealthLog },
                    onNavigateToRecipe = { currentScreen = Screen.Recipe },
                    onNavigateToCost = { currentScreen = Screen.Cost },
                    onNavigateToTips = { currentScreen = Screen.Tips },
                    onNavigateToHeatLog = { currentScreen = Screen.HeatLog },
                    onNavigateToBreedingLog = { currentScreen = Screen.BreedingLog },
                    onNavigateToCalvingLog = { currentScreen = Screen.CalvingLog },
                    onNavigateToWeightLog = { currentScreen = Screen.WeightLog },
                    onNavigateToExpenseLog = { currentScreen = Screen.ExpenseLog },
                    onNavigateToSalesLog = { currentScreen = Screen.SalesLog },
                    onNavigateToInventoryLog = { currentScreen = Screen.InventoryLog },
                    onNavigateToLaborLog = { currentScreen = Screen.LaborLog },
                    onNavigateToWaterLog = { currentScreen = Screen.WaterLog },
                    onNavigateToManureLog = { currentScreen = Screen.ManureLog },
                    onNavigateToDiaryLog = { currentScreen = Screen.DiaryLog },
                    onNavigateToDailyTaskLog = { currentScreen = Screen.DailyTaskLog },
                    onNavigateToMovementLog = { currentScreen = Screen.MovementLog },
                    onNavigateToVetVisitLog = { currentScreen = Screen.VetVisitLog },
                    onNavigateToDewormingLog = { currentScreen = Screen.DewormingLog },
                    onNavigateToInsuranceLog = { currentScreen = Screen.InsuranceLog },
                    onNavigateToMilkQualityLog = { currentScreen = Screen.MilkQualityLog },
                    onNavigateToMilkCollectionLog = { currentScreen = Screen.MilkCollectionLog }
                )
                is Screen.Recipe -> RecipeScreen()
                is Screen.FeedingLog -> FeedingLogScreen()
                is Screen.MilkLog -> MilkLogScreen()
                is Screen.HealthLog -> HealthLogScreen()
                is Screen.Cost -> CostTrackerScreen()
                is Screen.Tips -> VeterinaryTipsScreen()
                is Screen.CowProfile -> CowProfileScreen(onBack = { currentScreen = Screen.Home })
                is Screen.HeatLog -> HeatLogScreen()
                is Screen.BreedingLog -> BreedingLogScreen()
                is Screen.CalvingLog -> CalvingLogScreen()
                is Screen.WeightLog -> WeightLogScreen()
                is Screen.ExpenseLog -> ExpenseLogScreen()
                is Screen.SalesLog -> SalesLogScreen()
                is Screen.InventoryLog -> InventoryLogScreen()
                is Screen.LaborLog -> LaborLogScreen()
                is Screen.WaterLog -> WaterLogScreen()
                is Screen.ManureLog -> ManureLogScreen()
                is Screen.DiaryLog -> DiaryLogScreen()
                is Screen.DailyTaskLog -> DailyTaskLogScreen()
                is Screen.MovementLog -> MovementLogScreen()
                is Screen.VetVisitLog -> VetVisitLogScreen()
                is Screen.DewormingLog -> DewormingLogScreen()
                is Screen.InsuranceLog -> InsuranceLogScreen()
                is Screen.MilkQualityLog -> MilkQualityLogScreen()
                is Screen.MilkCollectionLog -> MilkCollectionLogScreen()
            }
        }
    }
}
