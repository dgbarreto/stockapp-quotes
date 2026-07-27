package com.danilobarreto.stockapp.quotes.data

import com.danilobarreto.stockapp.quotes.data.dto.FiiDto
import com.danilobarreto.stockapp.quotes.data.dto.FiiHistoryEntryDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
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
}