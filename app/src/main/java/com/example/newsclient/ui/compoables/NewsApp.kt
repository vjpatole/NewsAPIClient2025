package com.example.newsclient.ui.compoables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsApp(navController: NavHostController, modifier: Modifier) {
    var screenTitle by remember { mutableStateOf("News List") }

    // Observe navigation changes to update the title
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(navBackStackEntry) {
        screenTitle = when (navBackStackEntry?.destination?.route) {
            "newsList" -> "News List"
            "newsDetail/{url}" -> "News Detail"
            else -> "News App"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle) }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "newsList",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("newsList") { NewsListScreen(navController) }
            composable("newsDetail/{url}") { backStackEntry ->
                val url = backStackEntry.arguments?.getString("url") ?: ""
                requireNotNull(url)
                NewsDetailScreen(url)
            }
        }
    }
}