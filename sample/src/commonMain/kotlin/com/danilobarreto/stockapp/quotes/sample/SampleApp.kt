package com.danilobarreto.stockapp.quotes.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.danilobarreto.stockapp.auth.data.AuthApiClient
import com.danilobarreto.stockapp.auth.data.AuthRepositoryImpl
import com.danilobarreto.stockapp.auth.data.TokenStorage
import com.danilobarreto.stockapp.auth.presentation.LoginScreen
import com.danilobarreto.stockapp.auth.presentation.LoginViewModel
import com.danilobarreto.stockapp.designsystem.icons.StockAppIcons
import com.danilobarreto.stockapp.designsystem.theme.StockAppColors
import com.danilobarreto.stockapp.designsystem.theme.StockAppTheme
import com.danilobarreto.stockapp.quotes.data.FiisApiClient
import com.danilobarreto.stockapp.quotes.data.FiisRepositoryImpl
import com.danilobarreto.stockapp.quotes.data.QuotesApiClient
import com.danilobarreto.stockapp.quotes.data.QuotesRepositoryImpl
import com.danilobarreto.stockapp.quotes.presentation.AssetQuotesScreen
import com.danilobarreto.stockapp.quotes.presentation.AssetType
import com.danilobarreto.stockapp.quotes.presentation.FiiDetailScreen
import com.danilobarreto.stockapp.quotes.presentation.FiiDetailViewModel
import com.danilobarreto.stockapp.quotes.presentation.FiisViewModel
import com.danilobarreto.stockapp.quotes.presentation.QuoteDetailScreen
import com.danilobarreto.stockapp.quotes.presentation.QuoteDetailViewModel
import com.danilobarreto.stockapp.quotes.presentation.QuotesViewModel
import kotlinx.coroutines.launch

// Detalhe selecionado na lista de populares (ticker + se é ação ou FII), só pra saber
// qual tela/ViewModel de detalhe mostrar. Não existe navegação de verdade no sample.
private data class SelectedAsset(val ticker: String, val assetType: AssetType)

@Composable
fun SampleApp() {
    val tokenStorage = remember { TokenStorage() }
    val httpClient = remember { createSampleHttpClient(tokenStorage) }

    val authRepository = remember {
        AuthRepositoryImpl(AuthApiClient(httpClient, sampleBaseUrl()), tokenStorage)
    }
    val quotesRepository = remember {
        QuotesRepositoryImpl(QuotesApiClient(baseUrl = sampleBaseUrl(), httpClient = httpClient))
    }

    val fiisRepository = remember {
        FiisRepositoryImpl(FiisApiClient(baseUrl = sampleBaseUrl(), httpClient = httpClient))
    }

    val fiisViewModel = remember { FiisViewModel(fiisRepository) }
    val loginViewModel = remember { LoginViewModel(authRepository) }
    val quotesViewModel = remember { QuotesViewModel(quotesRepository) }
    val quoteDetailViewModel = remember { QuoteDetailViewModel(quotesRepository) }
    val fiiDetailViewModel = remember { FiiDetailViewModel(fiisRepository) }
    val coroutineScope = rememberCoroutineScope()

    val isLoggedIn by authRepository.isLoggedIn.collectAsState()
    var selectedAssetType by remember { mutableStateOf(AssetType.Stock) }
    var selectedAsset by remember { mutableStateOf<SelectedAsset?>(null) }

    StockAppTheme {
        if (!isLoggedIn) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { /* isLoggedIn muda e recompõe pra QuoteScreen sozinho */ },
                onNavigateToRegister = { /* sample é só login, de propósito */ }
            )
            return@StockAppTheme
        }

        val asset = selectedAsset
        if (asset == null) {
            Box(modifier = Modifier.fillMaxSize()) {
                AssetQuotesScreen(
                    quotesViewModel = quotesViewModel,
                    fiisViewModel = fiisViewModel,
                    onViewStockValuation = {},
                    onViewFiiValuation = {},
                    onOpenStockDetail = { ticker -> selectedAsset = SelectedAsset(ticker, AssetType.Stock) },
                    onOpenFiiDetail = { ticker -> selectedAsset = SelectedAsset(ticker, AssetType.Fii) },
                    selectedAssetType = selectedAssetType,
                    onAssetTypeSelected = { selectedAssetType = it },
                )
                // Só existe no sample: a tela de Cotações de verdade não tem botão de
                // voltar (é destino de aba, não de pilha) — aqui serve pra deslogar e
                // voltar pro Login sem precisar reiniciar o app a cada teste.
                Box(
                    modifier = Modifier
                        .padding(top = 48.dp, start = 16.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(StockAppColors.onPrimary.copy(alpha = 0.14f))
                        .clickable { coroutineScope.launch { authRepository.logout() } },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        StockAppIcons.ArrowLeft,
                        contentDescription = "Voltar pro login",
                        tint = StockAppColors.onPrimary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        } else if (asset.assetType == AssetType.Fii) {
            FiiDetailScreen(
                viewModel = fiiDetailViewModel,
                ticker = asset.ticker,
                onBack = { selectedAsset = null },
                onNewOrder = { /* sample não tem fluxo de ordem */ },
                onAlert = { /* alertas ainda não existem no app */ },
            )
        } else {
            QuoteDetailScreen(
                viewModel = quoteDetailViewModel,
                ticker = asset.ticker,
                onBack = { selectedAsset = null },
                onNewOrder = { /* sample não tem fluxo de ordem */ },
                onAlert = { /* alertas ainda não existem no app */ },
            )
        }
    }
}
