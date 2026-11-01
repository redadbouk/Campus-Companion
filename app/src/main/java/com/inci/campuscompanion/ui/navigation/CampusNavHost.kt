package com.inci.campuscompanion.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inci.campuscompanion.TaskScreen
import com.inci.campuscompanion.ui.EventsScreen
import com.inci.campuscompanion.ui.EventsViewModel
import com.inci.campuscompanion.ui.MainViewModel

object Routes {
    const val TASKS = "tasks"
    const val EVENTS = "events"
}

// The NavController lives here only. Screens get callbacks, not the controller.
@Composable
fun CampusNavHost(
    mainViewModel: MainViewModel,
    eventsViewModel: EventsViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.TASKS,
        modifier = modifier
    ) {
        composable(Routes.TASKS) {
            val isDarkMode by mainViewModel.isDarkMode.collectAsStateWithLifecycle()
            val tasks by mainViewModel.tasks.collectAsStateWithLifecycle()

            TaskScreen(
                isDarkMode = isDarkMode,
                onToggleDarkMode = { mainViewModel.toggleDarkMode(it) },
                tasks = tasks,
                onToggleTask = { mainViewModel.toggleTaskCompletion(it) },
                onAddTask = { title, description -> mainViewModel.upsertTask(title, description) },
                onOpenEvents = {
                    navController.navigate(Routes.EVENTS) { launchSingleTop = true }
                }
            )
        }

        composable(Routes.EVENTS) {
            val eventsState by eventsViewModel.uiState.collectAsStateWithLifecycle()

            EventsScreen(
                state = eventsState,
                onRefresh = { eventsViewModel.loadEvents() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
