package com.example.swapcampus_isaaclee.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.swapcampus_isaaclee.domain.Categorie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingFormScreen(
    onBack: () -> Unit,
    onSubmit: (String, String, Double, Categorie) -> Unit
) {
    var titre by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var prixText by remember { mutableStateOf("") }
    var selectedCategorie by remember { mutableStateOf(Categorie.LIVRES) }
    var expandedCategory by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publier une annonce") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = titre,
                onValueChange = { titre = it },
                label = { Text("Titre") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            OutlinedTextField(
                value = prixText,
                onValueChange = { prixText = it },
                label = { Text("Prix (€)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = !expandedCategory }
            ) {
                OutlinedTextField(
                    value = selectedCategorie.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Catégorie") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false }
                ) {
                    Categorie.entries.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.name) },
                            onClick = {
                                selectedCategorie = cat
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            errorText?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val prix = prixText.toDoubleOrNull()
                    when {
                        titre.isBlank() -> errorText = "Le titre ne peut pas être vide."
                        description.isBlank() -> errorText = "La description ne peut pas être vide."
                        prix == null || prix <= 0.0 -> errorText = "Veuillez entrer un prix valide supérieur à 0."
                        else -> {
                            errorText = null
                            onSubmit(titre.trim(), description.trim(), prix, selectedCategorie)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Valider et Publier")
            }
        }
    }
}
