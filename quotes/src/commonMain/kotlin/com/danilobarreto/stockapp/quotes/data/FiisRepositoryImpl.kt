package com.danilobarreto.stockapp.quotes.data

import com.danilobarreto.stockapp.quotes.data.dto.toDomain
import com.danilobarreto.stockapp.quotes.domain.Fii
import com.danilobarreto.stockapp.quotes.domain.FiiHistoryEntry
import com.danilobarreto.stockapp.quotes.domain.FiisRepository

class FiisRepositoryImpl(
    private val apiClient: FiisApiClient
): FiisRepository {
    override suspend fun getFii(ticker: String): Fii =
        apiClient.getFii(ticker).toDomain()

    override suspend fun getHistory(ticker: String): List<FiiHistoryEntry> =
        apiClient.getHistory(ticker).map { it.toDomain() }
}