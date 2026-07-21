package com.danilobarreto.stockapp.quotes.domain

data class QuoteFundamentals(
    val ticker: String,
    val closePrice: Double,
    val marketCap: Double,
    val pl: Double?,
    val pvp: Double?,
    val evEbitda: Double?,
    val roe: Double?,
    val roic: Double?,
    val netMargin: Double?,
    val grossMargin: Double?,
    val netDebtEbitda: Double?,
    val lpa: Double?,
    val vpa: Double?,
    val ebitda: Double?
)