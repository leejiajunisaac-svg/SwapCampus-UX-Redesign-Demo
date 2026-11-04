package com.example.swapcampus_isaaclee

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform