package com.danilobarreto.stockapp.quotes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform