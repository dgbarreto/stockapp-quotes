package com.danilobarreto.stockapp.quotes.data

import com.danilobarreto.stockapp.quotes.data.dto.AssetSummaryDto
import com.danilobarreto.stockapp.quotes.data.dto.PricePointDto
import com.danilobarreto.stockapp.quotes.data.dto.QuoteFundamentalsDto
import com.danilobarreto.stockapp.quotes.data.dto.QuoteHistoryEntryDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class QuotesApiClient(
    private val baseUrl: String,
    private val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation){
            json(Json { ignoreUnknownKeys = true })
        }
    }
){
    suspend fun getFundamentals(ticker: String): QuoteFundamentalsDto =
        httpClient.get("$baseUrl/quotes/$ticker").body()

    suspend fun getHistory(ticker: String): List<QuoteHistoryEntryDto> =
        httpClient.get("$baseUrl/quotes/$ticker/history").body()

    suspend fun getPopularQuotes(limit: Int): List<AssetSummaryDto> =
        httpClient.get("$baseUrl/quotes/list") { parameter("limit", limit) }.body()

    suspend fun getPrices(ticker: String, range: String): List<PricePointDto> =
        httpClient.get("$baseUrl/quotes/$ticker/prices") { parameter("range", range) }.body()
}