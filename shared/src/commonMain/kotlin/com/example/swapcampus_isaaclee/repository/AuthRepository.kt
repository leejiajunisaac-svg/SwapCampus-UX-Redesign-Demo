package com.example.swapcampus_isaaclee.repository

import com.example.swapcampus_isaaclee.domain.CompteUtilisateur
import com.example.swapcampus_isaaclee.domain.RoleUtilisateur
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDate

interface AuthRepository {
    val currentUser: StateFlow<CompteUtilisateur?>
    fun authenticate(email: String, motDePasse: String): CompteUtilisateur?
    fun logout()
}

class InMemoryAuthRepository : AuthRepository {
    private val accounts = listOf(
        CompteUtilisateur("1", "Ada Lovelace", "ada@isep.fr", RoleUtilisateur.ETUDIANT, LocalDate(2026, 1, 15)),
        CompteUtilisateur("2", "Alan Turing", "alan@isep.fr", RoleUtilisateur.ETUDIANT, LocalDate(2026, 2, 1)),
        CompteUtilisateur("3", "Admin ISEP", "admin@isep.fr", RoleUtilisateur.ADMINISTRATEUR, LocalDate(2025, 9, 1))
    )

    private val _currentUser = MutableStateFlow<CompteUtilisateur?>(null)
    override val currentUser: StateFlow<CompteUtilisateur?> = _currentUser.asStateFlow()

    override fun authenticate(email: String, motDePasse: String): CompteUtilisateur? {
        val user = accounts.find { it.email.equals(email.trim(), ignoreCase = true) }
        return if (user != null && motDePasse == "password123") {
            _currentUser.value = user
            user
        } else {
            null
        }
    }

    override fun logout() {
        _currentUser.value = null
    }
}

