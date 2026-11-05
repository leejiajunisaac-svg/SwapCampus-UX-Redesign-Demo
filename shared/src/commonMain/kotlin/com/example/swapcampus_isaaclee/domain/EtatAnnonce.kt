package com.example.swapcampus_isaaclee.domain

import kotlinx.datetime.LocalDate

sealed class EtatAnnonce {
    data object Disponible : EtatAnnonce()
    data class Reservee(val acheteur: String) : EtatAnnonce()
    data class Vendue(val dateVente: LocalDate) : EtatAnnonce()
}

