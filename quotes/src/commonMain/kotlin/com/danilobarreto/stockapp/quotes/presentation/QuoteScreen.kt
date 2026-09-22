package com.danilobarreto.stockapp.quotes.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danilobarreto.stockapp.designsystem.components.StockAppCard
import com.danilobarreto.stockapp.designsystem.components.StockAppErrorBanner
import com.danilobarreto.stockapp.designsystem.components.StockAppKeyValueRow
import com.danilobarreto.stockapp.designsystem.theme.StockAppColors
import com.danilobarreto.stockapp.designsystem.theme.StockAppTypography
import com.danilobarreto.stockapp.designsystem.util.toDecimalString
import com.danilobarreto.stockapp.quotes.domain.QuoteFundamentals

@Composable
fun QuoteContent(
    viewModel: QuotesViewModel,
    isSearching: Boolean,
    onViewValuation: (QuoteFundamentals) -> Unit,
    onOpenDetail: (String) -> Unit,
) {
    LaunchedEffect(Unit) { viewModel.loadPopular() }

    if (!isSearching) {
        val listState by viewModel.listUiState.collectAsState()
        AssetListSection(listState, onItemClick = onOpenDetail)
        return
    }

    val uiState by viewModel.uiState.collectAsState()
    when (val state = uiState) {
        is QuoteUiState.Idle -> {
            Text(
                "Aperte buscar para ver os indicadores.",
                style = StockAppTypography.bodyMedium,
                color = StockAppColors.textMuted,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
        is QuoteUiState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.padding(top = 24.dp))
        }
        is QuoteUiState.Error -> {
            StockAppErrorBanner(state.message, modifier = Modifier.padding(top = 24.dp))
        }
        is QuoteUiState.Success -> {
            QuoteFundamentasCard(state.fundamentals, onViewValuation)
        }
    }
}

@Composable
private fun QuoteFundamentasCard(fundamentals: QuoteFundamentals, onViewValuation: (QuoteFundamentals) -> Unit){
    StockAppCard(modifier = Modifier.padding(top = 24.dp)){
        Text(fundamentals.ticker, style = StockAppTypography.titleMedium, color = StockAppColors.textPrimary)
        Text(
            "R$ ${fundamentals.closePrice.toDecimalString()}",
            style = StockAppTypography.titleLarge,
            color = StockAppColors.textPrimary,
            modifier = Modifier.padding(top = 4.dp)
        )

        Column(modifier = Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            StockAppKeyValueRow("P/L", fundamentals.pl?.toDecimalString() ?: "—")
            StockAppKeyValueRow("P/VP", fundamentals.pvp?.toDecimalString() ?: "—")
            StockAppKeyValueRow("EV/EBITDA", fundamentals.evEbitda?.toDecimalString() ?: "—")
            StockAppKeyValueRow("ROE", fundamentals.roe?.let { "${it.toDecimalString()}%" } ?: "—")
            StockAppKeyValueRow("ROIC", fundamentals.roic?.let { "${it.toDecimalString()}%" } ?: "—")
            StockAppKeyValueRow("Margem líquida", fundamentals.netMargin?.let { "${it.toDecimalString()}%" } ?: "—")
            StockAppKeyValueRow("Margem bruta", fundamentals.grossMargin?.let { "${it.toDecimalString()}%" } ?: "—")
            StockAppKeyValueRow("Dívida líq./EBITDA", fundamentals.netDebtEbitda?.toDecimalString() ?: "—")
            StockAppKeyValueRow("LPA", fundamentals.lpa?.toDecimalString() ?: "—")
            StockAppKeyValueRow("VPA", fundamentals.vpa?.toDecimalString() ?: "—")
        }

        Button(
            onClick = { onViewValuation(fundamentals) },
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
        ) {
            Text("Ver valuation")
        }
    }
}