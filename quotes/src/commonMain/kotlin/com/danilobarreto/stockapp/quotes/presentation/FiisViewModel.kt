package com.danilobarreto.stockapp.quotes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilobarreto.stockapp.quotes.domain.Fii
import com.danilobarreto.stockapp.quotes.domain.FiisRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface FiiUiState {
    data object Idle: FiiUiState
    data object Loading: FiiUiState
    data class Success(val fii: Fii): FiiUiState
    data class Error(val message: String): FiiUiState
}

class FiisViewModel(
    private val repository: FiisRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<FiiUiState>(FiiUiState.Idle)
    val uiState: StateFlow<FiiUiState> = _uiState.asStateFlow()

    fun search(ticker: String) {
        if (ticker.isBlank()) return

        viewModelScope.launch {
            _uiState.value = FiiUiState.Loading
            _uiState.value = try {
                FiiUiState.Success(repository.getFii(ticker))
            } catch (e: Exception) {
                FiiUiState.Error(e.message ?: "Erro ao buscar FII")
            }
        }
    }
}