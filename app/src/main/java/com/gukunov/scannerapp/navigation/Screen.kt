package com.gukunov.scannerapp.navigation

/** Все экраны приложения. [route] — строковый ключ для сохранения back stack. */
sealed interface Screen {
    val route: String

    data object Scanner : Screen { override val route = "scanner" }
    data object History : Screen { override val route = "history" }
    data class ScanDetails(val scanId: Int) : Screen { override val route = "details/$scanId" }
    data object Settings : Screen { override val route = "settings" }
    data object About : Screen { override val route = "about" }

    companion object {
        fun fromRoute(route: String): Screen = when {
            route == History.route -> History
            route == Settings.route -> Settings
            route == About.route -> About
            route.startsWith("details/") -> ScanDetails(route.substringAfter("/").toInt())
            else -> Scanner
        }
    }
}
