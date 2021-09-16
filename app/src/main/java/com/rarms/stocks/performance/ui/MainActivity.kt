package com.rarms.stocks.performance.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rarms.stocks.performance.StocksApplication
import com.rarms.stocks.performance.data.AppContainer
import com.rarms.stocks.performance.ui.theme.StocksElevation
import com.rarms.stocks.performance.ui.theme.StocksPerformanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as StocksApplication).container
        setContent {
            StocksApp(appContainer)
        }
    }
}

@Composable
fun StocksApp(appContainer: AppContainer) {
    StocksPerformanceTheme() {
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = StocksScreen.fromRoute(backstackEntry.value?.destination?.route)
        Scaffold(bottomBar = {
            BottomAppBar(elevation = StocksElevation) {
                BottomNavItem(
                    screen = StocksScreen.PerformanceComparison,
                    chosenScreen = currentScreen,
                    navController = navController
                )
                BottomNavItem(
                    screen = StocksScreen.Candlesticks,
                    chosenScreen = currentScreen,
                    navController = navController
                )
            }
        }) { innerPadding ->
            StocksNavHost(
                appContainer = appContainer,
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun RowScope.BottomNavItem(
    chosenScreen: StocksScreen,
    screen: StocksScreen,
    navController: NavHostController
) {
    BottomNavigationItem(
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = screen.name,
                tint = if (chosenScreen == screen) MaterialTheme.colors.onPrimary
                else MaterialTheme.colors.onPrimary.copy(alpha = 0.7f)
            )
        },
        label = {
            Text(text = screen.readableName)
        },
        selected = chosenScreen == screen,
        onClick = { navController.navigate(screen.name) })
}