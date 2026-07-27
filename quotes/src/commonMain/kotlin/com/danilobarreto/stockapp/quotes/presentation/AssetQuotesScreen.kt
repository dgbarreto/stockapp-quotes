package com.danilobarreto.stockapp.quotes.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.danilobarreto.stockapp.designsystem.theme.StockAppColors
import com.danilobarreto.stockapp.designsystem.theme.StockAppTypography

enum class AssetType { Stock, Fii }

@Composable
fun AssetQuotesScreen(
    quotesViewModel: QuotesViewModel,
    fiisViewModel: FiisViewModel
){
    var selectedAssetType by remember { mutableStateOf(AssetType.Stock) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StockAppColors.surface1)
            .safeContentPadding()
            .padding(16.dp)) {

        Text("Cotações", style = StockAppTypography.titleLarge, color = StockAppColors.textPrimary)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
                .background(StockAppColors.border, RoundedCornerShape(10.dp))
                .padding(3.dp)
        ) {
            AssetTypeSegment("Ações", selectedAssetType == AssetType.Stock, Modifier.weight(1f)) { selectedAssetType = AssetType.Stock }
            AssetTypeSegment("FIIs", selectedAssetType == AssetType.Fii, Modifier.weight(1f)) { selectedAssetType = AssetType.Fii }
        }

        when (selectedAssetType) {
            AssetType.Stock -> QuoteContent(quotesViewModel)
            AssetType.Fii -> FiiContent(fiisViewModel)
        }
    }
}

@Composable
private fun AssetTypeSegment(label: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit){
    Text(
        label,
        style = StockAppTypography.bodyMedium,
        color = if (selected) StockAppColors.textPrimary else StockAppColors.textSecondary,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clickable(onClick = onClick)
            .background(if (selected) StockAppColors.surface2 else StockAppColors.border, RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp)
    )
}