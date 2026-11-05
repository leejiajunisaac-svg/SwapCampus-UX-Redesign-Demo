package com.example.swapcampus_isaaclee.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.swapcampus_isaaclee.domain.CompteUtilisateur

@Composable
fun ProfileScreen(
    currentUser: CompteUtilisateur?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Mon Profil", style = MaterialTheme.typography.headlineMedium)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Nom: ${currentUser?.nom ?: "Invité"}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Email: ${currentUser?.email ?: "Non connecté"}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Rôle: ${currentUser?.role?.name ?: "ETUDIANT"}", style = MaterialTheme.typography.bodySmall)
            }
        }

        HorizontalDivider()

        LanguageSelector()
    }
}
