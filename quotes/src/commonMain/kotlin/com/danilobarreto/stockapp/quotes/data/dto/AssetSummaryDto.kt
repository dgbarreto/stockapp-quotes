package com.danilobarreto.stockapp.quotes.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AssetSummaryDto(
    val ticker: String,
    val price: Double,
    val changePercent: Double,
    val sparkline: List<Double>,
)