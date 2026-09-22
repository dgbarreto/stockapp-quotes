package com.danilobarreto.stockapp.quotes.data

import com.danilobarreto.stockapp.quotes.data.dto.toDomain
import com.danilobarreto.stockapp.quotes.domain.AssetSummary
import com.danilobarreto.stockapp.quotes.domain.Fii
import com.danilobarreto.stockapp.quotes.domain.FiiHistoryEntry
import com.danilobarreto.stockapp.quotes.domain.FiisRepository
import com.danilobarreto.stockapp.quotes.domain.PricePoint
import com.danilobarreto.stockapp.quotes.domain.PriceRange

class FiisRepositoryImpl(
    private val apiClient: FiisApiClient
): FiisRepository {
    override suspend fun getFii(ticker: String): Fii =
        apiClient.getFii(ticker).toDomain()

    override suspend fun getHistory(ticker: String): List<FiiHistoryEntry> =
        apiClient.getHistory(ticker).map { it.toDomain() }

    override suspend fun getPopularFiis(limit: Int): List<AssetSummary> =
        apiClient.getPopularFiis(limit).map { it.toDomain() }

    override suspend fun getPrices(ticker: String, range: PriceRange): List<PricePoint> =
        apiClient.getPrices(ticker, range.apiValue).map { it.toDomain() }
}