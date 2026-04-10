package com.engineerfred.reclaim

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform