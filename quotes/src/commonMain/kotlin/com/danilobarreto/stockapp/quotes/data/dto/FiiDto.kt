package com.danilobarreto.stockapp.quotes.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FiiDto(
    val ticker: String,
    val name: String,
    val segment: String?,
    @SerialName("management_type") val managementType: String?,
    @SerialName("close_price") val closePrice: Double,
    @SerialName("book_value_per_share") val bookValuePerShare: Double?,
    val pvp: Double?,
    @SerialName("dividend_yield_ttm") val dividendYieldTtm: Double?,
    @SerialName("net_asset_value") val netAssetValue: Double?,
    @SerialName("shares_outstanding") val sharesOutstanding: Double?,
    @SerialName("total_shareholders") val totalShareholders: Int?,
    val dividendPerShareTtm: Double?,
    val distributionGrowthRate: Double?,
)