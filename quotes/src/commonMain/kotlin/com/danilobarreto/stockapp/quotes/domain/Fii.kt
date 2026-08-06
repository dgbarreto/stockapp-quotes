package com.danilobarreto.stockapp.quotes.domain

data class Fii(
    val ticker: String,
    val name: String,
    val segment: String?,
    val managementType: String?,
    val closePrice: Double,
    val bookValuePerShare: Double?,
    val pvp: Double?,
    val dividendYieldTtm: Double?,
    val netAssetValue: Double?,
    val sharesOutstanding: Double?,
    val totalShareholders: Int?,
    val dividendPerShareTtm: Double?,
    val distributionGrowthRate: Double?,
)