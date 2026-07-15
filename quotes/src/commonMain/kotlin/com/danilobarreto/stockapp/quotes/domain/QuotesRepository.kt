package com.danilobarreto.stockapp.quotes.domain

interface QuotesRepository{
    suspend fun getFundamentals(ticker: String): QuoteFundamentals
    suspend fun getHistoryu(ticker: String): List<QuoteHistoryEntry>
}