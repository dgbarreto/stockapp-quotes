package com.danilobarreto.stockapp.quotes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilobarreto.stockapp.quotes.domain.AssetSummary
import com.danilobarreto.stockapp.quotes.domain.QuoteFundamentals
import com.danilobarreto.stockapp.quotes.domain.QuotesRepository
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException


sealed interface AssetListUiState {
    data object Loading : AssetListUiState
    data class Success(val items: List<AssetSummary>) : AssetListUiState
    data class Error(val message: String) : AssetListUiState
}

class QuotesViewModel(
    private val repository: QuotesRepository
): ViewModel(){
    private val _listUiState = MutableStateFlow<AssetListUiState>(AssetListUiState.Loading)
    val listUiState: StateFlow<AssetListUiState> = _listUiState.asStateFlow()
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _searchState = MutableStateFlow<AssetSearchUiState>(AssetSearchUiState.Idle)
    val searchState: StateFlow<AssetSearchUiState> = _searchState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(value: String) {
        _query.value = value.trim().uppercase()
        scheduleSearch(debounce = true)
    }

    /** Tecla "Buscar" do teclado: pula o debounce. */
    fun searchNow() = scheduleSearch(debounce = false)

    private fun scheduleSearch(debounce: Boolean) {
        searchJob?.cancel()
        _searchState.value = AssetSearchUiState.Idle
        val ticker = _query.value
        if (!ticker.isFullTicker() || ticker in loadedTickers()) return

        searchJob = viewModelScope.launch {
            if (debounce) delay(SEARCH_DEBOUNCE_MS)
            _searchState.value = AssetSearchUiState.Loading
            _searchState.value = try {
                val fundamentals = repository.getFundamentals(ticker)
                loadPopular(silent = true)
                AssetSearchUiState.Found(ticker = fundamentals.ticker, price = fundamentals.closePrice)
            } catch (e: CancellationException) {
                throw e
            } catch (e: ClientRequestException) {
                AssetSearchUiState.NotFound(ticker)
            } catch (e: Exception) {
                AssetSearchUiState.Error(e.message ?: "Erro ao buscar $ticker")
            }
        }
    }

    private fun loadedTickers(): Set<String> =
        (_listUiState.value as? AssetListUiState.Success)?.items?.mapTo(mutableSetOf()) { it.ticker }.orEmpty()
    fun loadPopular(silent: Boolean = false) {
        viewModelScope.launch {
            if (!silent) _listUiState.value = AssetListUiState.Loading
            try {
                val items = repository.getPopularQuotes(8)
                _listUiState.value = AssetListUiState.Success(items)
                // O ticker buscado já está na lista (entrou no known_tickers): o cartão
                // padrão assume e a linha de resultado da busca sai de cena.
                if (items.any { it.ticker == _query.value }) {
                    searchJob?.cancel()
                    _searchState.value = AssetSearchUiState.Idle
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (!silent) {
                    _listUiState.value = AssetListUiState.Error(e.message ?: "Erro ao carregar cotações")
                }
            }
        }
    }
}