package com.danilobarreto.stockapp.quotes.data.dto

import com.danilobarreto.stockapp.quotes.domain.QuoteFundamentals
import com.danilobarreto.stockapp.quotes.domain.QuoteHistoryEntry

fun QuoteFundamentalsDto.toDomain(): QuoteFundamentals = QuoteFundamentals(
    ticker = ticker,
    closePrice = closePrice,
    marketCap = marketCap,
    pl = pl,
    pvp = pvp,
    evEbitda = evEbitda,
    roe = roe,
    roic = roic,
    netMargin = netMargin,
    grossMargin = grossMargin,
    netDebtEbitda = netDebtEbitda,
    lpa = lpa,
    vpa = vpa,
    ebitda = ebitda,
    priceToSalesRatio = priceToSalesRatio,
    earningsCagr5y = earningsCagr5y,
    dividendPerShareTtm = dividendPerShareTtm,
)

fun QuoteHistoryEntryDto.toDomain(): QuoteHistoryEntry = QuoteHistoryEntry(
    fetchedAt = fetchedAt,
    fundamentals = QuoteFundamentals(
        ticker = ticker,
        closePrice = closePrice,
        marketCap = marketCap,
        pl = pl,
        pvp = pvp,
        evEbitda = evEbitda,
        roe = roe,
        roic = roic,
        netMargin = netMargin,
        grossMargin = grossMargin,
        netDebtEbitda = netDebtEbitda,
        lpa = lpa,
        vpa = vpa,
        ebitda = ebitda,
        // QuoteSnapshot (histórico) não guarda esses 3 campos — só existem
        // na resposta ao vivo do /quotes/:ticker. Null aqui de propósito.
        priceToSalesRatio = null,
        earningsCagr5y = null,
        dividendPerShareTtm = null,
    ),
)