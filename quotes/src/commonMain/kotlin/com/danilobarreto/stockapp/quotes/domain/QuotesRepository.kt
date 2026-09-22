package com.danilobarreto.stockapp.quotes.domain

interface QuotesRepository{
    suspend fun getFundamentals(ticker: String): QuoteFundamentals
    suspend fun getHistoryu(ticker: String): List<QuoteHistoryEntry>
    suspend fun getPopularQuotes(limit: Int = 8): List<AssetSummary>
    suspend fun getPrices(ticker: String, range: PriceRange): List<PricePoint>
}