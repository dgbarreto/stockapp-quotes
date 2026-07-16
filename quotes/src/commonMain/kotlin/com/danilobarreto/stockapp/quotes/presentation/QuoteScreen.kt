package com.danilobarreto.stockapp.quotes.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.danilobarreto.stockapp.designsystem.theme.StockAppShapes
import com.danilobarreto.stockapp.designsystem.theme.StockAppTypography
import com.danilobarreto.stockapp.designsystem.util.toDecimalString
import com.danilobarreto.stockapp.quotes.domain.QuoteFundamentals

@Composable
fun QuoteScreen(viewModel: QuotesViewModel){
    val uiState by viewModel.uiState.collectAsState()
    var ticker by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StockAppColors.surface1)
            .padding(16.dp)) {

        Text("Cotações", style = StockAppTypography.titleLarge, color = StockAppColors.textPrimary)

        Row(verticalAlignment = Alignment.CenterVertically){
            OutlinedTextField(
                value = ticker,
                onValueChange = { ticker = it.uppercase() },
                label = { Text("Ticker") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { viewModel.search(ticker) },
                modifier = Modifier.padding(start = 8.dp)
            ){
                Text("Buscar")
            }
            when(val state = uiState){
                is QuoteUiState.Idle -> {
                    Text(
                        "Digite um ticker para ver os indicadores.",
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
                    QuoteFundamentasCard(state.fundamentals)
                }
            }
        }
    }
}

@Composable
private fun QuoteFundamentasCard(fundamentals: QuoteFundamentals){
    StockAppCard(modifier = Modifier.padding(top = 24.dp)){
        Text(fundamentals.ticker, style = StockAppTypography.titleMedium, color = StockAppColors.textPrimary)
        Text(
            "R$ ${fundamentals.closePrice.toDecimalString()}",
            style = StockAppTypography.titleLarge,
            color = StockAppColors.textPrimary,
            modifier = Modifier.padding(top = 4.dp)
        )

        Column(modifier = Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            StockAppKeyValueRow("P/L", fundamentals.pl.toDecimalString())
            StockAppKeyValueRow("P/VP", fundamentals.pvp.toDecimalString())
            StockAppKeyValueRow("EV/EBITDA", fundamentals.evEbitda.toDecimalString())
            StockAppKeyValueRow("ROE", "${fundamentals.roe.toDecimalString()}%")
            StockAppKeyValueRow("ROIC", "${fundamentals.roic.toDecimalString()}%")
            StockAppKeyValueRow("Margem líquida", "${fundamentals.netMargin.toDecimalString()}%")
            StockAppKeyValueRow("Margem bruta", "${fundamentals.grossMargin.toDecimalString()}%")
            StockAppKeyValueRow("Dívida líq./EBITDA", fundamentals.netDebtEbitda.toDecimalString())
            StockAppKeyValueRow("LPA", fundamentals.lpa.toDecimalString())
            StockAppKeyValueRow("VPA", fundamentals.vpa.toDecimalString())
        }
    }
}