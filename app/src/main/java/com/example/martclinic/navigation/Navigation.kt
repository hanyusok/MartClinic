package com.example.martclinic.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.martclinic.domain.model.Person
import com.example.martclinic.di.SerializationModule
import com.example.martclinic.ui.components.error.NavigationErrorHandler
import com.example.martclinic.ui.screens.*
import com.example.martclinic.ui.viewmodel.CreatePersonViewModel
import com.example.martclinic.ui.viewmodel.SearchViewModel
import com.example.martclinic.ui.viewmodel.PersonViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import androidx.compose.material3.Text

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CreatePerson : Screen("create_person")
    object SearchPerson : Screen("search_person")
    object SearchPcode : Screen("search_pcode")
    object SearchSearchid : Screen("search_searchid")
    object PersonList : Screen("person_list")
    object PersonDetail : Screen("person_detail/{pcode}") {
        fun createRoute(pcode: Int) = "person_detail/$pcode"
    }
    object EditPerson : Screen("edit_person/{pcode}") {
        fun createRoute(pcode: Int) = "edit_person/$pcode"
    }
    object MtsWait : Screen("mts_wait")
    object Mtr : Screen("mtr")
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation(navController: NavHostController) {
    var navigationError by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val json = SerializationModule.provideJson()

    LaunchedEffect(navigationError) {
        navigationError?.let {
            snackbarHostState.showSnackbar(it)
            navigationError = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToSearch = { navController.navigate(Screen.SearchPerson.route) },
                    onNavigateToSearchidSearch = { navController.navigate(Screen.SearchSearchid.route) },
                )
            }
            
            composable(Screen.CreatePerson.route) {
                PersonCreateScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetail = { pcode ->
                        navController.navigate(Screen.PersonDetail.createRoute(pcode)) {
                            popUpTo(Screen.CreatePerson.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.SearchPerson.route) {
                SearchPersonScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onPersonClick = { pcode ->
                        try {
                            val pcodeInt = pcode.toInt()
                            navController.navigate(Screen.PersonDetail.createRoute(pcodeInt))
                        } catch (e: NumberFormatException) {
                            navigationError = "Invalid person code"
                        }
                    }
                )
            }

            composable(Screen.SearchPcode.route) {
                SearchPcodeScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onPersonClick = { pcode ->
                        try {
                            val pcodeInt = pcode.toInt()
                            navController.navigate(Screen.PersonDetail.createRoute(pcodeInt))
                        } catch (e: NumberFormatException) {
                            navigationError = "Invalid person code"
                        }
                    }
                )
            }

            composable(Screen.SearchSearchid.route) {
                SearchSearchidScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onPersonClick = { pcode ->
                        try {
                            val pcodeInt = pcode.toInt()
                            navController.navigate(Screen.PersonDetail.createRoute(pcodeInt))
                        } catch (e: NumberFormatException) {
                            navigationError = "Invalid person code"
                        }
                    }
                )
            }
            
            composable(Screen.PersonList.route) {
                PersonListScreen(
                    onNavigateToDetail = { pcode ->
                        navController.navigate(Screen.PersonDetail.createRoute(pcode))
                    },
                    onNavigateToCreate = { navController.navigate(Screen.CreatePerson.route) },
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSearch = { navController.navigate(Screen.SearchPerson.route) }
                )
            }
            
            composable(Screen.PersonDetail.route) { backStackEntry ->
                val pcode = backStackEntry.arguments?.getString("pcode")?.toIntOrNull()
                if (pcode != null) {
                    PersonDetailScreen(
                        pcode = pcode,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToHome = { 
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    )
                } else {
                    navigationError = "Invalid person code"
                    navController.popBackStack()
                }
            }
            
            composable(Screen.EditPerson.route) { backStackEntry ->
                val pcode = backStackEntry.arguments?.getString("pcode")?.toIntOrNull()
                if (pcode != null) {
                    EditPersonScreen(
                        pcode = pcode,
                        onNavigateBack = { navController.popBackStack() }
                    )
                } else {
                    navigationError = "Invalid person code"
                    navController.popBackStack()
                }
            }

            composable(Screen.MtsWait.route) {
                MtsWaitScreen()
            }

            composable(Screen.Mtr.route) {
                MtrScreen()
            }
        }
    }
} 