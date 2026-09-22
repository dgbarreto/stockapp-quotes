package com.danilobarreto.stockapp.quotes.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PricePointDto(
    val date: String,
    val close: Double,
)