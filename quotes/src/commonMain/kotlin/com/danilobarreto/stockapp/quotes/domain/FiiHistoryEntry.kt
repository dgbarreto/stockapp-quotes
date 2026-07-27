package com.danilobarreto.stockapp.quotes.domain

data class FiiHistoryEntry(
    val fetchedAt: String,
    val fii: Fii
)