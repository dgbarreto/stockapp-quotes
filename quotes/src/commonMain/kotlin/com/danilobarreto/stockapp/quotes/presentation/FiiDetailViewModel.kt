package com.danilobarreto.stockapp.quotes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilobarreto.stockapp.quotes.domain.AssetDetailSummary
import com.danilobarreto.stockapp.quotes.domain.AssetIndicator
import com.danilobarreto.stockapp.quotes.domain.FiisRepository
import com.danilobarreto.stockapp.quotes.domain.IndicatorFormat
import com.danilobarreto.stockapp.quotes.domain.PriceRange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val BAZIN_TARGET_YIELD = 0.06

class FiiDetailViewModel(
    private val repository: FiisRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<AssetDetailUiState>(AssetDetailUiState.Loading)
    val uiState: StateFlow<AssetDetailUiState> = _uiState.asStateFlow()

    private val _selectedRange = MutableStateFlow(PriceRange.ONE_MONTH)
    val selectedRange: StateFlow<PriceRange> = _selectedRange.asStateFlow()

    private val _pricesUiState = MutableStateFlow<PricesUiState>(PricesUiState.Loading)
    val pricesUiState: StateFlow<PricesUiState> = _pricesUiState.asStateFlow()

    fun load(ticker: String) {
        viewModelScope.launch {
            _uiState.value = AssetDetailUiState.Loading
            _uiState.value = try {
                val fii = repository.getFii(ticker)
                val recentCloses = repository.getPrices(ticker, PriceRange.ONE_MONTH).map { it.close }
                val last = recentCloses.lastOrNull() ?: fii.closePrice
                val prev = recentCloses.getOrNull(recentCloses.size - 2) ?: last
                val changePercent = if (prev != 0.0) ((last - prev) / prev) * 100 else 0.0
                val priceCeiling = fii.dividendPerShareTtm
                    ?.takeIf { it > 0 }
                    ?.div(BAZIN_TARGET_YIELD)

                AssetDetailUiState.Success(
                    AssetDetailSummary(
                        ticker = fii.ticker,
                        companyName = fii.name,
                        price = last,
                        changePercent = changePercent,
                        indicators = listOf(
                            AssetIndicator("P/VP", fii.pvp, IndicatorFormat.RATIO),
                            AssetIndicator("DY (12m)", fii.dividendYieldTtm, IndicatorFormat.PERCENT),
                            AssetIndicator("VP por cota", fii.bookValuePerShare, IndicatorFormat.CURRENCY),
                            AssetIndicator("Preço-teto (Bazin, 6%)", priceCeiling, IndicatorFormat.CURRENCY),
                        ),
                    ),
                )
            } catch (e: Exception) {
                AssetDetailUiState.Error(e.message ?: "Erro ao carregar o FII")
            }
            loadPrices(ticker, PriceRange.ONE_MONTH)
        }
    }

    fun selectRange(ticker: String, range: PriceRange) {
        _selectedRange.value = range
        loadPrices(ticker, range)
    }

    private fun loadPrices(ticker: String, range: PriceRange) {
        viewModelScope.launch {
            _pricesUiState.value = PricesUiState.Loading
            _pricesUiState.value = try {
                PricesUiState.Success(repository.getPrices(ticker, range).map { it.close })
            } catch (e: Exception) {
                PricesUiState.Error(e.message ?: "Erro ao carregar o histórico")
            }
        }
    }
}