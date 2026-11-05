package com.example.swapcampus_isaaclee.repository

import com.example.swapcampus_isaaclee.domain.Annonce
import com.example.swapcampus_isaaclee.domain.CatalogueAnnonces
import com.example.swapcampus_isaaclee.domain.Categorie
import com.example.swapcampus_isaaclee.domain.EtatAnnonce
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ListingRepository {
    val listings: StateFlow<List<Annonce>>
    fun allListings(): List<Annonce>
    fun searchListings(motCle: String?, categorie: Categorie?): List<Annonce>
    fun postListing(id: Int, titre: String, description: String, prix: Double, categorie: Categorie, proprietaire: String, photoUrl: String? = null): Annonce
    fun updateListing(id: Int, titre: String, description: String, prix: Double, categorie: Categorie, photoUrl: String? = null): Annonce?
    fun changeStatus(id: Int, nouvelEtat: EtatAnnonce): Annonce?
    fun removeListing(id: Int): Boolean
}

class InMemoryListingRepository(
    private val catalog: CatalogueAnnonces = CatalogueAnnonces().apply {
        publierAnnonce(
            id = 1,
            titre = "Livre Algorithmique & Structures de Données",
            description = "Manuel complet de cours en très bon état, idéal pour les modules d'informatique.",
            prix = 25.0,
            categorie = Categorie.LIVRES,
            proprietaire = "Ada Lovelace"
        )
        publierAnnonce(
            id = 2,
            titre = "Calculatrice Graphique TI-84 Plus CE",
            description = "Parfait état de marche avec câble USB et coque de protection.",
            prix = 45.0,
            categorie = Categorie.ELECTRONIQUE,
            proprietaire = "Alan Turing"
        )
        publierAnnonce(
            id = 3,
            titre = "Annales & Polycopiés de Mathématiques",
            description = "Recueil complet d'exercices corrigés et examens blancs des 3 dernières années.",
            prix = 10.0,
            categorie = Categorie.COURS,
            proprietaire = "Ada Lovelace"
        )
        publierAnnonce(
            id = 4,
            titre = "Chaise de Bureau Ergonomique",
            description = "Chaise réglable en hauteur avec accoudoirs, très bon état.",
            prix = 35.0,
            categorie = Categorie.MOBILIER,
            proprietaire = "Admin ISEP"
        )
        changerEtat(4, EtatAnnonce.Reservee(acheteur = "Ada Lovelace"))
    }
) : ListingRepository {

    private val _listings = MutableStateFlow<List<Annonce>>(emptyList())
    override val listings: StateFlow<List<Annonce>> = _listings.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        _listings.value = catalog.toutesLesAnnonces()
    }

    override fun allListings(): List<Annonce> = catalog.toutesLesAnnonces()

    override fun searchListings(motCle: String?, categorie: Categorie?): List<Annonce> {
        return catalog.rechercherAnnonces(motCle, categorie)
    }

    override fun postListing(
        id: Int,
        titre: String,
        description: String,
        prix: Double,
        categorie: Categorie,
        proprietaire: String,
        photoUrl: String?
    ): Annonce {
        val created = catalog.publierAnnonce(
            id = id,
            titre = titre,
            description = description,
            prix = prix,
            categorie = categorie,
            proprietaire = proprietaire,
            photoUrl = photoUrl
        )
        refresh()
        return created
    }

    override fun updateListing(
        id: Int,
        titre: String,
        description: String,
        prix: Double,
        categorie: Categorie,
        photoUrl: String?
    ): Annonce? {
        val updated = catalog.modifierAnnonce(
            id = id,
            titre = titre,
            description = description,
            prix = prix,
            categorie = categorie,
            photoUrl = photoUrl
        )
        if (updated != null) refresh()
        return updated
    }

    override fun changeStatus(id: Int, nouvelEtat: EtatAnnonce): Annonce? {
        val updated = catalog.changerEtat(id, nouvelEtat)
        if (updated != null) refresh()
        return updated
    }

    override fun removeListing(id: Int): Boolean {
        val removed = catalog.retirerAnnonce(id)
        if (removed) refresh()
        return removed
    }
}
