package com.example.swapcampus_isaaclee.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.swapcampus_isaaclee.domain.Annonce
import com.example.swapcampus_isaaclee.domain.Categorie
import com.example.swapcampus_isaaclee.domain.EtatAnnonce
import com.example.swapcampus_isaaclee.ui.i18n.currentLanguageTag
import com.example.swapcampus_isaaclee.ui.i18n.formatPrice
import com.example.swapcampus_isaaclee.ui.viewmodel.ListingsUiState

@Composable
fun ListingsScreen(
    uiState: ListingsUiState,
    onSearch: (String?, Categorie?) -> Unit,
    onListingClick: (Int) -> Unit,
    onCreateClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Categorie?>(null) }
    val lang = currentLanguageTag()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Listing")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    onSearch(it.ifBlank { null }, selectedCategory)
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                placeholder = { Text("Rechercher...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = {
                            selectedCategory = null
                            onSearch(searchQuery.ifBlank { null }, null)
                        },
                        label = { Text("Tous") }
                    )
                }
                items(Categorie.entries) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = {
                            val next = if (selectedCategory == cat) null else cat
                            selectedCategory = next
                            onSearch(searchQuery.ifBlank { null }, next)
                        },
                        label = { Text(cat.name) }
                    )
                }
            }

            when (uiState) {
                is ListingsUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ListingsUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(uiState.message, color = MaterialTheme.colorScheme.error)
                    }
                }
                is ListingsUiState.Success -> {
                    if (uiState.listings.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Aucune annonce trouvée.")
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(uiState.listings) { annonce ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().clickable { onListingClick(annonce.id) },
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(annonce.titre, style = MaterialTheme.typography.titleMedium)
                                            Text(
                                                formatPrice(annonce.prix, lang),
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            annonce.description,
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = 2
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                "Vendeur: ${annonce.proprietaire}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            val statusText = when (annonce.etat) {
                                                is EtatAnnonce.Disponible -> "Disponible"
                                                is EtatAnnonce.Reservee -> "Réservée"
                                                is EtatAnnonce.Vendue -> "Vendue"
                                            }
                                            Text(
                                                statusText,
                                                style = MaterialTheme.typography.labelMedium,
                                                color = when (annonce.etat) {
                                                    is EtatAnnonce.Disponible -> MaterialTheme.colorScheme.primary
                                                    is EtatAnnonce.Reservee -> MaterialTheme.colorScheme.tertiary
                                                    is EtatAnnonce.Vendue -> MaterialTheme.colorScheme.outline
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
