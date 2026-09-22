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
import com.danilobarreto.stockapp.quotes.domain.Fii

@Composable
fun FiiContent(
    viewModel: FiisViewModel,
    isSearching: Boolean,
    onViewValuation: (Fii) -> Unit,
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
        is FiiUiState.Idle -> Text("Aperte buscar para ver os indicadores.", style = StockAppTypography.bodyMedium, color = StockAppColors.textMuted, modifier = Modifier.padding(top = 24.dp))
        is FiiUiState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(top = 24.dp))
        is FiiUiState.Error -> StockAppErrorBanner(state.message, modifier = Modifier.padding(top = 24.dp))
        is FiiUiState.Success -> FiiCard(state.fii, onViewValuation)
    }
}

@Composable
private fun FiiCard(fii: Fii, onViewValuation: (Fii) -> Unit){
    StockAppCard(modifier = Modifier.padding(top = 24.dp)){
        Text(fii.ticker, style = StockAppTypography.titleMedium, color = StockAppColors.textPrimary)
        Text(fii.name, style = StockAppTypography.bodyMedium, color = StockAppColors.textMuted)
        Text(
            "R$ ${fii.closePrice.toDecimalString()}",
            style = StockAppTypography.titleLarge,
            color = StockAppColors.textPrimary,
            modifier = Modifier.padding(top = 8.dp)
        )

        Column(modifier = Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            StockAppKeyValueRow("P/VP", fii.pvp?.toDecimalString() ?: "—")
            StockAppKeyValueRow("DY (12m)", fii.dividendYieldTtm?.let { "${it.toDecimalString()}%" } ?: "—")
            StockAppKeyValueRow("VP por cota", fii.bookValuePerShare?.let { "R$ ${it.toDecimalString()}" } ?: "—")
            StockAppKeyValueRow("Cotas emitidas", fii.sharesOutstanding?.toDecimalString() ?: "—")
            StockAppKeyValueRow("Cotistas", fii.totalShareholders?.toString() ?: "—")
            StockAppKeyValueRow("Segmento", fii.segment ?: "—")
            StockAppKeyValueRow("Tipo de gestão", fii.managementType ?: "—")
        }

        Button(onClick = { onViewValuation(fii) }, modifier = Modifier.padding(top = 16.dp).fillMaxWidth()) {
            Text("Ver valuation")
        }
    }
}