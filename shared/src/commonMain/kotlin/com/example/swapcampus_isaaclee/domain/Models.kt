package com.example.swapcampus_isaaclee.domain

import kotlinx.datetime.LocalDate

data class CompteUtilisateur(
    val id: String,
    val nom: String,
    val email: String,
    val role: RoleUtilisateur,
    val dateCreation: LocalDate
)

data class Annonce(
    val id: Int,
    val titre: String,
    val description: String,
    val prix: Double,
    val categorie: Categorie,
    val proprietaire: String,
    val etat: EtatAnnonce = EtatAnnonce.Disponible,
    val photoUrl: String? = null
)

data class Message(
    val id: String,
    val expediteurId: String,
    val destinataireId: String,
    val contenu: String,
    val dateEnvoi: LocalDate
)

data class ActionModeration(
    val id: String,
    val moderateurId: String,
    val type: TypeActionModeration,
    val motif: String,
    val dateAction: LocalDate
)

