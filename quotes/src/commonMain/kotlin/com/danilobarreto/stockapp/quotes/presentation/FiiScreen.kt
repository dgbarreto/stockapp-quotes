package com.danilobarreto.stockapp.quotes.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
fun FiiContent(viewModel: FiisViewModel){
    val uiState by viewModel.uiState.collectAsState()
    var ticker by remember { mutableStateOf("") }

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = ticker,
            onValueChange = { ticker = it.uppercase() },
            label = { Text("Ticker") },
            modifier = Modifier.weight(1f)
        )
        Button(onClick = { viewModel.search(ticker) }, modifier = Modifier.padding(start = 8.dp)) {
            Text("Buscar")
        }
    }

    when(val state = uiState){
        is FiiUiState.Idle -> Text("Digite o ticker de um FII para ver os indicadores.", style = StockAppTypography.bodyMedium, color = StockAppColors.textMuted, modifier = Modifier.padding(top = 24.dp))
        is FiiUiState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(top = 24.dp))
        is FiiUiState.Error -> StockAppErrorBanner(state.message, modifier = Modifier.padding(top = 24.dp))
        is FiiUiState.Success -> FiiCard(state.fii)
    }
}

@Composable
private fun FiiCard(fii: Fii){
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
    }
}