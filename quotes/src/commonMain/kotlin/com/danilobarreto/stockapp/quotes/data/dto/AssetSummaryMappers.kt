package com.danilobarreto.stockapp.quotes.data.dto

import com.danilobarreto.stockapp.quotes.domain.AssetSummary
import com.danilobarreto.stockapp.quotes.domain.PricePoint

fun AssetSummaryDto.toDomain(): AssetSummary = AssetSummary(
    ticker = ticker,
    price = price,
    changePercent = changePercent,
    sparkline = sparkline,
    companyName = companyName
)

fun PricePointDto.toDomain(): PricePoint = PricePoint(date = date, close = close)