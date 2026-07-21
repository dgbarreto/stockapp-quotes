package com.danilobarreto.stockapp.quotes.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuoteHistoryEntryDto(
    val ticker: String,
    val fetchedAt: String,
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