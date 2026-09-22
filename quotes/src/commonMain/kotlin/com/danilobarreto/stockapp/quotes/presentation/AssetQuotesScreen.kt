package com.danilobarreto.stockapp.quotes.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.danilobarreto.stockapp.designsystem.components.StockAppSegmentedControl
import com.danilobarreto.stockapp.designsystem.icons.StockAppIcons
import com.danilobarreto.stockapp.designsystem.theme.StockAppColors
import com.danilobarreto.stockapp.designsystem.theme.StockAppShapes
import com.danilobarreto.stockapp.designsystem.theme.StockAppTypography
import com.danilobarreto.stockapp.quotes.domain.Fii
import com.danilobarreto.stockapp.quotes.domain.QuoteFundamentals

enum class AssetType { Stock, Fii }

@Composable
fun AssetQuotesScreen(
    quotesViewModel: QuotesViewModel,
    fiisViewModel: FiisViewModel,
    onViewStockValuation: (QuoteFundamentals) -> Unit,
    onViewFiiValuation: (Fii) -> Unit,
    onOpenStockDetail: (String) -> Unit,
    onOpenFiiDetail: (String) -> Unit,
    selectedAssetType: AssetType,
    onAssetTypeSelected: (AssetType) -> Unit,
) {
    var stockQuery by remember { mutableStateOf("") }
    var fiiQuery by remember { mutableStateOf("") }
    val query = if (selectedAssetType == AssetType.Stock) stockQuery else fiiQuery

    Column(modifier = Modifier.fillMaxSize().background(StockAppColors.surface1)) {
        QuotesHeader(
            query = query,
            onQueryChange = {
                if (selectedAssetType == AssetType.Stock) stockQuery = it else fiiQuery = it
            },
            onSearch = {
                if (selectedAssetType == AssetType.Stock) quotesViewModel.search(stockQuery)
                else fiisViewModel.search(fiiQuery)
            },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            StockAppSegmentedControl(
                options = listOf("Ações", "FIIs"),
                selectedIndex = selectedAssetType.ordinal,
                onOptionSelected = { onAssetTypeSelected(AssetType.entries[it]) },
            )

            when (selectedAssetType) {
                AssetType.Stock -> QuoteContent(
                    viewModel = quotesViewModel,
                    isSearching = stockQuery.isNotBlank(),
                    onViewValuation = onViewStockValuation,
                    onOpenDetail = onOpenStockDetail,
                )
                AssetType.Fii -> FiiContent(
                    viewModel = fiisViewModel,
                    isSearching = fiiQuery.isNotBlank(),
                    onViewValuation = onViewFiiValuation,
                    onOpenDetail = onOpenFiiDetail,
                )
            }
        }
    }
}

@Composable
private fun QuotesHeader(query: String, onQueryChange: (String) -> Unit, onSearch: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(StockAppColors.primary, shape = StockAppShapes.headerBottomRadius)
            .safeContentPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Text("Cotações", style = StockAppTypography.headerTitle, color = StockAppColors.onPrimary)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = StockAppShapes.pillRadius,
                    ambientColor = Color.Black.copy(alpha = 0.08f),
                    spotColor = Color.Black.copy(alpha = 0.08f),
                )
                .background(StockAppColors.surface2, shape = StockAppShapes.pillRadius)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(StockAppIcons.Search, contentDescription = null, tint = StockAppColors.primary, modifier = Modifier.size(18.dp))
            Box(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                if (query.isEmpty()) {
                    Text(
                        "Buscar ticker ou empresa",
                        style = StockAppTypography.bodyMedium,
                        color = StockAppColors.textMuted,
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = { onQueryChange(it.uppercase()) },
                    singleLine = true,
                    textStyle = StockAppTypography.bodyMedium.copy(color = StockAppColors.textPrimary),
                    cursorBrush = SolidColor(StockAppColors.primary),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}