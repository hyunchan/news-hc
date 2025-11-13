package com.hcpark.news.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hcpark.news.domain.model.Category
import com.hcpark.news.presentation.bookmarked.ui.BookmarkedScreen
import com.hcpark.news.presentation.news.ui.NewsScreen
import com.hcpark.news.presentation.top20.ui.Top20Screen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainRoute.SCHEME_TOP20
    ) {
        composable(MainRoute.SCHEME_TOP20) {
            Top20Screen(navigate = navController::navigate)
        }
        composable(
            route = MainRoute.SCHEME_NEWS,
            arguments = listOf(
                navArgument(MainRoute.CATEGORY) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            NewsScreen(
                navigate = navController::navigate,
                navigateToMain = {
                    navController.popBackStack(MainRoute.main, false)
                }
            )
        }
        composable(MainRoute.SCHEME_BOOKMARKED) {
            BookmarkedScreen(navigate = navController::navigate)
        }
    }
}

object MainRoute {
    val main = top20()
    fun top20() = SCHEME_TOP20

    fun news(
        sources: String? = null,
        category: Category? = null
    ): String =
        SCHEME_NEWS
            .replace("{$SOURCES}", "$sources")
            .replace("{$CATEGORY}", "${category?.key}")

    fun bookmarked() = SCHEME_BOOKMARKED

    const val SCHEME_TOP20 = "top20"
    const val SCHEME_NEWS = "news?sources={sources}&category={category}"
    const val SCHEME_BOOKMARKED = "bookmarked"
    const val CATEGORY = "category"
    const val SOURCES = "sources"
}
