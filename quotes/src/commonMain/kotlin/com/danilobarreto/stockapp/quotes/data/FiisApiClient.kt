package com.danilobarreto.stockapp.quotes.data

import com.danilobarreto.stockapp.quotes.data.dto.AssetSummaryDto
import com.danilobarreto.stockapp.quotes.data.dto.FiiDto
import com.danilobarreto.stockapp.quotes.data.dto.FiiHistoryEntryDto
import com.danilobarreto.stockapp.quotes.data.dto.PricePointDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class FiisApiClient(
    private val baseUrl: String,
    private val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation){
            json(Json { ignoreUnknownKeys = true })
        }
    }
){
    suspend fun getFii(ticker: String): FiiDto =
        httpClient.get("$baseUrl/fiis/$ticker").body()

    suspend fun getHistory(ticker: String): List<FiiHistoryEntryDto> =
        httpClient.get("$baseUrl/fiis/$ticker/history").body()

    suspend fun getPopularFiis(limit: Int): List<AssetSummaryDto> =
        httpClient.get("$baseUrl/fiis/list") { parameter("limit", limit) }.body()

    suspend fun getPrices(ticker: String, range: String): List<PricePointDto> =
        httpClient.get("$baseUrl/fiis/$ticker/prices") { parameter("range", range) }.body()
}