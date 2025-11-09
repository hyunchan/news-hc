package com.hcpark.news.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hcpark.news.domain.model.Category
import com.hcpark.news.presentation.news.ui.NewsScreen
import com.hcpark.news.presentation.top20.ui.Top20Screen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainRoute.TOP20
    ) {
        composable(MainRoute.TOP20) {
            Top20Screen(
                navigate = navController::navigate
            )
        }
        composable(
            route = MainRoute.NEWS,
            arguments = listOf(
                navArgument(MainRoute.CATEGORY) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            NewsScreen(
                navigate = navController::navigate
            )
        }
    }
}

object MainRoute {
    fun news(
        sources: String? = null,
        category: Category? = null
    ): String =
        NEWS
            .replace("{$SOURCES}", "$sources")
            .replace("{$CATEGORY}", "${category?.key}")

    const val TOP20 = "top20"
    const val NEWS = "news?sources={sources}&category={category}"
    const val CATEGORY = "category"
    const val SOURCES = "sources"
}