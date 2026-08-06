package com.danilobarreto.stockapp.quotes.data.dto

import com.danilobarreto.stockapp.quotes.domain.Fii
import com.danilobarreto.stockapp.quotes.domain.FiiHistoryEntry

fun FiiDto.toDomain(): Fii = Fii(
    ticker = ticker,
    name = name,
    segment = segment,
    managementType = managementType,
    closePrice = closePrice,
    bookValuePerShare = bookValuePerShare,
    pvp = pvp,
    dividendYieldTtm = dividendYieldTtm,
    netAssetValue = netAssetValue,
    sharesOutstanding = sharesOutstanding,
    totalShareholders = totalShareholders,
    dividendPerShareTtm = dividendPerShareTtm,
    distributionGrowthRate = distributionGrowthRate,
)

fun FiiHistoryEntryDto.toDomain(): FiiHistoryEntry = FiiHistoryEntry(
    fetchedAt = fetchedAt,
    fii = Fii(
        ticker = ticker,
        name = name,
        segment = segment,
        managementType = managementType,
        closePrice = closePrice,
        bookValuePerShare = bookValuePerShare,
        pvp = pvp,
        dividendYieldTtm = dividendYieldTtm,
        netAssetValue = netAssetValue,
        sharesOutstanding = sharesOutstanding,
        totalShareholders = totalShareholders,
        // FiiSnapshot (histórico) não guarda esses 2 campos — só existem na
        // resposta ao vivo do /fiis/:ticker. Null aqui de propósito.
        dividendPerShareTtm = null,
        distributionGrowthRate = null,
    ),
)