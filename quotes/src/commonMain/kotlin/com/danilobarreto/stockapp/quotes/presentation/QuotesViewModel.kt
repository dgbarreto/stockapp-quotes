package com.danilobarreto.stockapp.quotes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilobarreto.stockapp.quotes.domain.AssetSummary
import com.danilobarreto.stockapp.quotes.domain.QuoteFundamentals
import com.danilobarreto.stockapp.quotes.domain.QuotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface QuoteUiState{
    data object Idle: QuoteUiState
    data object Loading: QuoteUiState
    data class Success(val fundamentals: QuoteFundamentals): QuoteUiState
    data class Error(val message: String): QuoteUiState
}

sealed interface AssetListUiState {
    data object Loading : AssetListUiState
    data class Success(val items: List<AssetSummary>) : AssetListUiState
    data class Error(val message: String) : AssetListUiState
}

class QuotesViewModel(
    private val repository: QuotesRepository
): ViewModel(){
    private val _uiState = MutableStateFlow<QuoteUiState>(QuoteUiState.Idle)
    val uiState: StateFlow<QuoteUiState> = _uiState.asStateFlow()

    private val _listUiState = MutableStateFlow<AssetListUiState>(AssetListUiState.Loading)
    val listUiState: StateFlow<AssetListUiState> = _listUiState.asStateFlow()

    fun loadPopular() {
        viewModelScope.launch {
            _listUiState.value = AssetListUiState.Loading
            _listUiState.value = try {
                AssetListUiState.Success(repository.getPopularQuotes(8))
            } catch (e: Exception) {
                AssetListUiState.Error(e.message ?: "Erro ao carregar cotações")
            }
        }
    }

    fun search(ticker: String){
        if(ticker.isBlank()) return

        viewModelScope.launch {
            _uiState.value = QuoteUiState.Loading
            _uiState.value = try{
                QuoteUiState.Success(repository.getFundamentals(ticker))
            } catch(e: Exception){
                QuoteUiState.Error(e.message ?: "Erro ao buscar cotação")
            }
        }
    }
}