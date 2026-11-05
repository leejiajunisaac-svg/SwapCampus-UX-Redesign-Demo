package com.example.swapcampus_isaaclee.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapcampus_isaaclee.domain.Categorie
import com.example.swapcampus_isaaclee.domain.EtatAnnonce
import com.example.swapcampus_isaaclee.repository.ListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListingsViewModel(
    private val repository: ListingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListingsUiState>(ListingsUiState.Loading)
    val uiState: StateFlow<ListingsUiState> = _uiState.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message: StateFlow<UiMessage?> = _message.asStateFlow()

    init {
        loadListings()
    }

    fun loadListings(motCle: String? = null, categorie: Categorie? = null) {
        viewModelScope.launch {
            _uiState.value = ListingsUiState.Loading
            try {
                val results = repository.searchListings(motCle, categorie)
                _uiState.value = ListingsUiState.Success(results)
            } catch (e: Exception) {
                _uiState.value = ListingsUiState.Error(e.message ?: "Erreur de chargement")
            }
        }
    }

    fun reserveListing(id: Int, acheteur: String) {
        viewModelScope.launch {
            val updated = repository.changeStatus(id, EtatAnnonce.Reservee(acheteur))
            if (updated != null) {
                _message.value = UiMessage(message = "Annonce réservée avec succès")
                loadListings()
            } else {
                _message.value = UiMessage(message = "Impossible de réserver cette annonce")
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
