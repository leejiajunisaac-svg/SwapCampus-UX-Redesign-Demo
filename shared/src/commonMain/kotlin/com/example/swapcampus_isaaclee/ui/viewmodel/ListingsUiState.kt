package com.example.swapcampus_isaaclee.ui.viewmodel

import com.example.swapcampus_isaaclee.domain.Annonce

sealed interface ListingsUiState {
    data object Loading : ListingsUiState
    data class Success(val listings: List<Annonce>) : ListingsUiState
    data class Error(val message: String) : ListingsUiState
}
