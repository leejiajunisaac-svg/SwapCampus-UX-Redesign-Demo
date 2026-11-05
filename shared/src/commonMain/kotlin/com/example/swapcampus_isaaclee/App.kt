package com.example.swapcampus_isaaclee

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.swapcampus_isaaclee.domain.EtatAnnonce
import com.example.swapcampus_isaaclee.repository.InMemoryAuthRepository
import com.example.swapcampus_isaaclee.repository.InMemoryListingRepository
import com.example.swapcampus_isaaclee.ui.navigation.*
import com.example.swapcampus_isaaclee.ui.screens.*
import com.example.swapcampus_isaaclee.ui.viewmodel.ListingsUiState
import com.example.swapcampus_isaaclee.ui.viewmodel.ListingsViewModel
import kotlinx.coroutines.launch

@Composable
fun App() {
    val repository = remember { InMemoryListingRepository() }
    val authRepository = remember {
        InMemoryAuthRepository().apply {
            authenticate("ada@isep.fr", "password123")
        }
    }
    val viewModel = remember { ListingsViewModel(repository) }
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val currentUser by authRepository.currentUser.collectAsState()

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it.message)
            viewModel.clearMessage()
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    MaterialTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute?.contains("ListingsRoute") == true,
                        onClick = {
                            navController.navigate(ListingsRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Annonces") }
                    )
                    NavigationBarItem(
                        selected = currentRoute?.contains("ProfileRoute") == true,
                        onClick = {
                            navController.navigate(ProfileRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text("Profil") }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = ListingsRoute,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<ListingsRoute> {
                    ListingsScreen(
                        uiState = uiState,
                        onSearch = { query, category ->
                            viewModel.loadListings(query, category)
                        },
                        onListingClick = { id ->
                            navController.navigate(DetailRoute(id))
                        },
                        onCreateClick = {
                            navController.navigate(PostRoute)
                        }
                    )
                }

                composable<DetailRoute> { backStackEntry ->
                    val route: DetailRoute = backStackEntry.toRoute()
                    val annonce = (uiState as? ListingsUiState.Success)?.listings?.find { it.id == route.id }
                    ListingDetailScreen(
                        annonce = annonce,
                        onBack = { navController.popBackStack() },
                        onReserve = { id ->
                            val userName = currentUser?.nom ?: "Utilisateur"
                            viewModel.reserveListing(id, userName)
                            navController.popBackStack()
                        }
                    )
                }

                composable<PostRoute> {
                    ListingFormScreen(
                        onBack = { navController.popBackStack() },
                        onSubmit = { titre, desc, prix, cat ->
                            scope.launch {
                                val nextId = ((uiState as? ListingsUiState.Success)?.listings?.maxOfOrNull { it.id } ?: 0) + 1
                                val ownerName = currentUser?.nom ?: "Ada Lovelace"
                                repository.postListing(
                                    id = nextId,
                                    titre = titre,
                                    description = desc,
                                    prix = prix,
                                    categorie = cat,
                                    proprietaire = ownerName
                                )
                                viewModel.loadListings()
                                navController.popBackStack()
                            }
                        }
                    )
                }

                composable<ProfileRoute> {
                    ProfileScreen(currentUser = currentUser)
                }
            }
        }
    }
}
