package com.danilobarreto.stockapp.quotes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilobarreto.stockapp.quotes.domain.AssetDetailSummary
import com.danilobarreto.stockapp.quotes.domain.AssetIndicator
import com.danilobarreto.stockapp.quotes.domain.IndicatorFormat
import com.danilobarreto.stockapp.quotes.domain.PriceRange
import com.danilobarreto.stockapp.quotes.domain.QuotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Yield alvo fixo do método de Bazin — ver decisoes.md: escolhido pra dar um
// "Preço-teto" determinístico na tela de Detalhe, sem pedir input do usuário
// (diferente da tela de Valuation, que deixa o usuário escolher o método).
private const val BAZIN_TARGET_YIELD = 0.06

sealed interface AssetDetailUiState {
    data object Loading : AssetDetailUiState
    data class Success(val summary: AssetDetailSummary) : AssetDetailUiState
    data class Error(val message: String) : AssetDetailUiState
}

sealed interface PricesUiState {
    data object Loading : PricesUiState
    data class Success(val closes: List<Double>) : PricesUiState
    data class Error(val message: String) : PricesUiState
}

class QuoteDetailViewModel(
    private val repository: QuotesRepository,
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
                val fundamentals = repository.getFundamentals(ticker)
                val recentCloses = repository.getPrices(ticker, PriceRange.ONE_MONTH).map { it.close }
                val last = recentCloses.lastOrNull() ?: fundamentals.closePrice
                val prev = recentCloses.getOrNull(recentCloses.size - 2) ?: last
                val changePercent = if (prev != 0.0) ((last - prev) / prev) * 100 else 0.0
                val dividendYieldPercent = fundamentals.dividendPerShareTtm
                    ?.takeIf { fundamentals.closePrice > 0 }
                    ?.let { it / fundamentals.closePrice * 100 }
                val priceCeiling = fundamentals.dividendPerShareTtm
                    ?.takeIf { it > 0 }
                    ?.div(BAZIN_TARGET_YIELD)

                AssetDetailUiState.Success(
                    AssetDetailSummary(
                        ticker = fundamentals.ticker,
                        companyName = fundamentals.companyName,
                        price = last,
                        changePercent = changePercent,
                        indicators = listOf(
                            AssetIndicator("P/L", fundamentals.pl, IndicatorFormat.RATIO),
                            AssetIndicator("Dividend yield", dividendYieldPercent, IndicatorFormat.PERCENT),
                            AssetIndicator("P/VP", fundamentals.pvp, IndicatorFormat.RATIO),
                            AssetIndicator("Preço-teto (Bazin, 6%)", priceCeiling, IndicatorFormat.CURRENCY),
                        ),
                    ),
                )
            } catch (e: Exception) {
                AssetDetailUiState.Error(e.message ?: "Erro ao carregar o ativo")
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