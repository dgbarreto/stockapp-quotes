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
    ebitda = ebitda
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
    ),
)