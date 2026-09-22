package com.danilobarreto.stockapp.quotes.domain

interface FiisRepository {
    suspend fun getFii(ticker: String): Fii
    suspend fun getHistory(ticker: String): List<FiiHistoryEntry>
    suspend fun getPopularFiis(limit: Int = 8): List<AssetSummary>
    suspend fun getPrices(ticker: String, range: PriceRange): List<PricePoint>
}