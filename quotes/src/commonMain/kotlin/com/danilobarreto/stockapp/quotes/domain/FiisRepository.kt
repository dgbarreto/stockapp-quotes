package com.danilobarreto.stockapp.quotes.domain

interface FiisRepository {
    suspend fun getFii(ticker: String): Fii
    suspend fun getHistory(ticker: String): List<FiiHistoryEntry>
}