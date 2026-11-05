package com.example.swapcampus_isaaclee.domain

class CatalogueAnnonces {
    // Encapsulated private list: never exposed directly as MutableList
    private val annonces = mutableListOf<Annonce>()

    // Expose as an unmodifiable read-only List
    fun toutesLesAnnonces(): List<Annonce> = annonces.toList()

    fun publierAnnonce(
        id: Int,
        titre: String,
        description: String,
        prix: Double,
        categorie: Categorie,
        proprietaire: String,
        photoUrl: String? = null
    ): Annonce {
        // Scope function: .apply builds the item, registers it internally, and returns it
        return Annonce(
            id = id,
            titre = titre,
            description = description,
            prix = prix,
            categorie = categorie,
            proprietaire = proprietaire,
            photoUrl = photoUrl
        ).apply {
            annonces.add(this)
        }
    }

    fun rechercherAnnonces(
        motCle: String? = null,
        categorie: Categorie? = null
    ): List<Annonce> {
        return annonces.filter { annonce ->
            val matchMotCle = motCle.isNullOrBlank() ||
                    annonce.titre.contains(motCle, ignoreCase = true) ||
                    annonce.description.contains(motCle, ignoreCase = true)

            val matchCategorie = categorie == null || annonce.categorie == categorie

            matchMotCle && matchCategorie
        }
    }

    fun modifierAnnonce(
        id: Int,
        titre: String,
        description: String,
        prix: Double,
        categorie: Categorie,
        photoUrl: String? = null
    ): Annonce? {
        val index = annonces.indexOfFirst { it.id == id }
        if (index == -1) return null

        val existante = annonces[index]
        val modifiee = existante.copy(
            titre = titre,
            description = description,
            prix = prix,
            categorie = categorie,
            photoUrl = photoUrl
        )
        annonces[index] = modifiee
        return modifiee
    }

    fun changerEtat(id: Int, nouvelEtat: EtatAnnonce): Annonce? {
        val index = annonces.indexOfFirst { it.id == id }
        if (index == -1) return null

        val existante = annonces[index]
        val miseAJour = existante.copy(etat = nouvelEtat)
        annonces[index] = miseAJour
        return miseAJour
    }

    fun retirerAnnonce(id: Int): Boolean {
        return annonces.removeAll { it.id == id }
    }
}

// Required Extension Function on List<Annonce>
fun List<Annonce>.disponibles(): List<Annonce> {
    return this.filter { it.etat is EtatAnnonce.Disponible }
}

// Exhaustive 'when' without an 'else' branch
fun descriptionEtat(etat: EtatAnnonce): String = when (etat) {
    is EtatAnnonce.Disponible -> "Disponible à la vente"
    is EtatAnnonce.Reservee -> "Réservée par ${etat.acheteur}"
    is EtatAnnonce.Vendue -> "Vendue le ${etat.dateVente}"
}

