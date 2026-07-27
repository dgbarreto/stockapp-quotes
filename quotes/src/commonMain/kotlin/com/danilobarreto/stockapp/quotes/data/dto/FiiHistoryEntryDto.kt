package com.danilobarreto.stockapp.quotes.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FiiHistoryEntryDto(
    val ticker: String,
    val fetchedAt: String,
    val name: String,
    val segment: String?,
    val managementType: String?,
    val closePrice: Double,
    val bookValuePerShare: Double?,
    val pvp: Double?,
    val dividendYieldTtm: Double?,
    val netAssetValue: Double?,
    val sharesOutstanding: Double?,
    val totalShareholders: Int?
)