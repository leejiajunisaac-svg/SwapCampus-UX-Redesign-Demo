package com.example.swapcampus_isaaclee.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object ListingsRoute

@Serializable
object ProfileRoute

@Serializable
object PostRoute

@Serializable
data class DetailRoute(val id: Int)
