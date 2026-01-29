package org.unizd.rma.sarlija.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.unizd.rma.sarlija.presentation.ui.screens.HomeScreen
import org.unizd.rma.sarlija.presentation.ui.screens.RacunAddScreen
import org.unizd.rma.sarlija.presentation.ui.screens.RacunDetailsScreen
import org.unizd.rma.sarlija.presentation.ui.screens.RacunListScreen
import org.unizd.rma.sarlija.presentation.viewmodel.RacunViewModel

object Routes {
    const val HOME = "home"
    const val ADD = "add"
    const val LIST = "list"
    const val DETAILS = "details"
}

@Composable
fun AppNavHost(vm: RacunViewModel) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                onAddClick = { nav.navigate(Routes.ADD) },
                onSavedClick = { nav.navigate(Routes.LIST) }
            )
        }

        composable(Routes.ADD) {
            RacunAddScreen(
                vm = vm,
                onBack = { nav.popBackStack() }
            )
        }

        composable(Routes.LIST) {
            RacunListScreen(
                vm = vm,
                onOpenDetails = { id -> nav.navigate("${Routes.DETAILS}/$id") },
                onBack = { nav.popBackStack() }
            )
        }

        composable(
            route = "${Routes.DETAILS}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong("id") ?: 0L
            RacunDetailsScreen(
                vm = vm,
                racunId = id,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
