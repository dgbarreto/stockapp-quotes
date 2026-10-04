package com.danilobarreto.stockapp.quotes.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssetSummaryDto(
    val ticker: String,
    @SerialName("company_name") val companyName: String? = null,
    val price: Double,
    val changePercent: Double,
    val sparkline: List<Double>,
)