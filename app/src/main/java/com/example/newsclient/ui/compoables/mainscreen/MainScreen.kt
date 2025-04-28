package com.example.newsclient.ui.compoables.mainscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.newsclient.ui.compoables.NewsApp
import com.example.newsclient.ui.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(newsViewModel: NewsViewModel) {
    val navController = rememberNavController()
    Scaffold(
        modifier =
            Modifier.fillMaxSize(),
    ) { innerPadding ->
        NewsApp(navController, Modifier.padding(innerPadding), newsViewModel)
    }
}
