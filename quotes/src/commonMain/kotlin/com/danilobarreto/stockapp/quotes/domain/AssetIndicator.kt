package com.danilobarreto.stockapp.quotes.domain

enum class IndicatorFormat { RATIO, PERCENT, CURRENCY }

data class AssetIndicator(
    val label: String,
    val value: Double?,
    val format: IndicatorFormat,
)

data class AssetDetailSummary(
    val ticker: String,
    val price: Double,
    val changePercent: Double,
    val indicators: List<AssetIndicator>,
)