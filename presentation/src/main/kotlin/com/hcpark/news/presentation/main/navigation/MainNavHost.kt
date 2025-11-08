package com.hcpark.news.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hcpark.news.presentation.news.ui.NewsScreen
import com.hcpark.news.presentation.top20.ui.Top20Screen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "top20"
    ) {
        composable("top20") {
            Top20Screen()
        }
        composable("news") {
            NewsScreen()
        }
    }
}