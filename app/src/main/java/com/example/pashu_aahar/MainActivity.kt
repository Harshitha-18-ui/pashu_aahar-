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
            if (currentScreen != Screen.CowProfile) {
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
                    onNavigateToRecipe = { currentScreen = Screen.Recipe },
                    onNavigateToFeedingLog = { currentScreen = Screen.FeedingLog },
                    onNavigateToMilkLog = { currentScreen = Screen.MilkLog },
                    onNavigateToHealthLog = { currentScreen = Screen.HealthLog },
                    onNavigateToCost = { currentScreen = Screen.Cost },
                    onNavigateToTips = { currentScreen = Screen.Tips }
                )
                is Screen.Recipe -> RecipeScreen()
                is Screen.FeedingLog -> FeedingLogScreen()
                is Screen.MilkLog -> MilkLogScreen()
                is Screen.HealthLog -> HealthLogScreen()
                is Screen.Cost -> CostTrackerScreen()
                is Screen.Tips -> VeterinaryTipsScreen()
                is Screen.CowProfile -> CowProfileScreen(onBack = { currentScreen = Screen.Home })
            }
        }
    }
}
