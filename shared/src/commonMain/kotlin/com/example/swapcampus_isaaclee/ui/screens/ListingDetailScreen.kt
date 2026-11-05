package com.example.swapcampus_isaaclee.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.swapcampus_isaaclee.domain.Annonce
import com.example.swapcampus_isaaclee.domain.EtatAnnonce
import com.example.swapcampus_isaaclee.ui.i18n.currentLanguageTag
import com.example.swapcampus_isaaclee.ui.i18n.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDetailScreen(
    annonce: Annonce?,
    onBack: () -> Unit,
    onReserve: (Int) -> Unit
) {
    val lang = currentLanguageTag()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(annonce?.titre ?: "Détails") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (annonce == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Annonce introuvable")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(annonce.titre, style = MaterialTheme.typography.headlineMedium)
            Text(
                formatPrice(annonce.prix, lang),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            AssistChip(
                onClick = {},
                label = { Text("Catégorie: ${annonce.categorie.name}") }
            )
            HorizontalDivider()
            Text("Description", style = MaterialTheme.typography.titleSmall)
            Text(annonce.description, style = MaterialTheme.typography.bodyLarge)
            HorizontalDivider()
            Text("Vendeur: ${annonce.proprietaire}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.weight(1f))

            if (annonce.etat is EtatAnnonce.Disponible) {
                Button(
                    onClick = { onReserve(annonce.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Réserver cette annonce")
                }
            } else {
                OutlinedButton(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val label = when (annonce.etat) {
                        is EtatAnnonce.Reservee -> "Déjà réservée"
                        is EtatAnnonce.Vendue -> "Vendue"
                        else -> "Indisponible"
                    }
                    Text(label)
                }
            }
        }
    }
}
