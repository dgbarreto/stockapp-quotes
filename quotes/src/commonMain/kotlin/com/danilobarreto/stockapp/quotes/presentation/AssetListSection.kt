package com.danilobarreto.stockapp.quotes.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danilobarreto.stockapp.designsystem.components.StockAppAreaLineChart
import com.danilobarreto.stockapp.designsystem.components.StockAppErrorBanner
import com.danilobarreto.stockapp.designsystem.theme.StockAppColors
import com.danilobarreto.stockapp.designsystem.theme.StockAppShapes
import com.danilobarreto.stockapp.designsystem.theme.StockAppTypography
import com.danilobarreto.stockapp.designsystem.util.toBrPercent
import com.danilobarreto.stockapp.designsystem.util.toBrl
import com.danilobarreto.stockapp.designsystem.util.toDecimalString
import com.danilobarreto.stockapp.quotes.domain.AssetSummary

// Usado tanto pela lista de Ações quanto pela de FIIs — o modelo (AssetSummary) e o
// estado (AssetListUiState) já são genéricos o bastante pra isso.
@Composable
fun AssetListSection(state: AssetListUiState, onItemClick: (String) -> Unit, modifier: Modifier = Modifier) {
    when (state) {
        is AssetListUiState.Loading -> {
            Box(modifier = modifier.fillMaxWidth().padding(top = 32.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AssetListUiState.Error -> {
            StockAppErrorBanner(state.message, modifier = modifier.padding(top = 24.dp))
        }
        is AssetListUiState.Success -> {
            if (state.items.isEmpty()) {
                Text(
                    "Nenhum ativo popular no momento.",
                    style = StockAppTypography.bodyMedium,
                    color = StockAppColors.textMuted,
                    modifier = modifier.padding(top = 24.dp),
                )
            } else {
                Column(modifier = modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    state.items.forEach { item ->
                        AssetSummaryCard(item, onClick = { onItemClick(item.ticker) })
                    }
                }
            }
        }
    }
}

@Composable
private fun AssetSummaryCard(item: AssetSummary, onClick: () -> Unit) {
    val positive = item.changePercent >= 0
    val accentColor = if (positive) StockAppColors.textSuccess else StockAppColors.textDanger
    val chipBg = if (positive) StockAppColors.bgSuccess else StockAppColors.bgDanger
    val sign = if (positive) "+" else ""

    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(StockAppColors.surface2, shape = StockAppShapes.cardRadius)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.ticker, style = StockAppTypography.titleMedium, color = StockAppColors.textPrimary)
            item.companyName?.let {
                Text(
                    it,
                    style = StockAppTypography.bodySmall,
                    color = StockAppColors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                item.price.toBrl(),
                style = StockAppTypography.titleLarge.copy(fontSize = 22.sp),
                color = StockAppColors.textPrimary,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                item.changePercent.toBrPercent(),
                style = StockAppTypography.labelMedium,
                color = accentColor,
                modifier = Modifier
                    .background(chipBg, shape = RoundedCornerShape(100))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            )
            if (item.sparkline.size >= 2) {
                StockAppAreaLineChart(
                    values = item.sparkline.map { it.toFloat() },
                    modifier = Modifier.width(76.dp).padding(top = 6.dp),
                    height = 34.dp,
                    lineColor = accentColor,
                    areaColor = accentColor.copy(alpha = 0.12f),
                    strokeWidth = 2.dp,
                )
            }
        }
    }
}