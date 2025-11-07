package com.hcpark.news.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hcpark.news.presentation.news.ui.NewsScreen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "news"
    ) {
        composable("news") {
            NewsScreen()
        }
    }
}