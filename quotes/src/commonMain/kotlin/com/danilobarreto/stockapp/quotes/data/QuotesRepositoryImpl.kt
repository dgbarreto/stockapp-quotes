package com.danilobarreto.stockapp.quotes.data

import com.danilobarreto.stockapp.quotes.data.dto.toDomain
import com.danilobarreto.stockapp.quotes.domain.QuoteFundamentals
import com.danilobarreto.stockapp.quotes.domain.QuoteHistoryEntry
import com.danilobarreto.stockapp.quotes.domain.QuotesRepository

class QuotesRepositoryImpl(
    private val apiClient: QuotesApiClient
): QuotesRepository{
    override suspend fun getFundamentals(ticker: String): QuoteFundamentals =
        apiClient.getFundamentals(ticker).toDomain()

    override suspend fun getHistoryu(ticker: String): List<QuoteHistoryEntry> =
        apiClient.getHistory(ticker).map { it.toDomain() }
}