package com.danilobarreto.stockapp.quotes.domain

data class QuoteHistoryEntry(
    val fetchedAt: String,
    val fundamentals: QuoteFundamentals
)