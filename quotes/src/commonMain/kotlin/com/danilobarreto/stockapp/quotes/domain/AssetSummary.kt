package com.danilobarreto.stockapp.quotes.domain

data class AssetSummary(
    val ticker: String,
    val companyName: String?,
    val price: Double,
    val changePercent: Double,
    val sparkline: List<Double>,
)