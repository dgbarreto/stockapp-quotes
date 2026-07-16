package com.danilobarreto.stockapp.quotes.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.danilobarreto.stockapp.designsystem.theme.StockAppTheme
import com.danilobarreto.stockapp.quotes.data.QuotesApiClient
import com.danilobarreto.stockapp.quotes.data.QuotesRepositoryImpl
import com.danilobarreto.stockapp.quotes.presentation.QuoteScreen
import com.danilobarreto.stockapp.quotes.presentation.QuotesViewModel

@Composable
fun SampleApp() {
    val viewModel = remember {
        val apiClient = QuotesApiClient(baseUrl = sampleBaseUrl())
        val repository = QuotesRepositoryImpl(apiClient)
        QuotesViewModel(repository)
    }
    StockAppTheme{
        QuoteScreen(viewModel)
    }
}
