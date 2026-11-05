package com.example.swapcampus_isaaclee.ui.viewmodel

data class UiMessage(
    val id: Long = kotlin.random.Random.nextLong(),
    val message: String
)
