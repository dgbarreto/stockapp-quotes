package com.danilobarreto.stockapp.quotes.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteFundamentalsDto(
    val ticker: String,
    @SerialName("close_price") val closePrice: Double,
    @SerialName("market_cap") val marketCap: Double,
    val pl: Double,
    val pvp: Double,
    @SerialName("ev_ebitda") val evEbitda: Double,
    val roe: Double,
    val roic: Double,
    @SerialName("net_margin") val netMargin: Double,
    @SerialName("gross_margin") val grossMargin: Double,
    @SerialName("net_debt_ebitda") val netDebtEbitda: Double,
    val lpa: Double,
    val vpa: Double,
    val ebitda: Double
)
