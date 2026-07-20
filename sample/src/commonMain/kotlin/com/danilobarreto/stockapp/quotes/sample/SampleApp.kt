package com.danilobarreto.stockapp.quotes.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.danilobarreto.stockapp.auth.data.AuthApiClient
import com.danilobarreto.stockapp.auth.data.AuthRepositoryImpl
import com.danilobarreto.stockapp.auth.data.TokenStorage
import com.danilobarreto.stockapp.auth.presentation.LoginScreen
import com.danilobarreto.stockapp.auth.presentation.LoginViewModel
import com.danilobarreto.stockapp.designsystem.theme.StockAppTheme
import com.danilobarreto.stockapp.quotes.data.QuotesApiClient
import com.danilobarreto.stockapp.quotes.data.QuotesRepositoryImpl
import com.danilobarreto.stockapp.quotes.presentation.QuoteScreen
import com.danilobarreto.stockapp.quotes.presentation.QuotesViewModel

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

    val loginViewModel = remember { LoginViewModel(authRepository) }
    val quotesViewModel = remember { QuotesViewModel(quotesRepository) }

    val isLoggedIn by authRepository.isLoggedIn.collectAsState()

    StockAppTheme {
        if (isLoggedIn) {
            QuoteScreen(quotesViewModel)
        } else {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { /* isLoggedIn muda e recompõe pra QuoteScreen sozinho */ },
                onNavigateToRegister = { /* sample é só login, de propósito */ }
            )
        }
    }
}