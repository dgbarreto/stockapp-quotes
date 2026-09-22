package com.danilobarreto.stockapp.quotes.domain

enum class PriceRange(val apiValue: String, val label: String) {
    ONE_MONTH("1m", "1M"),
    SIX_MONTHS("6m", "6M"),
    ONE_YEAR("1y", "1A"),
    MAX("max", "Máx"),
}