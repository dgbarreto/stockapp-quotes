package com.danilobarreto.stockapp.quotes.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.danilobarreto.stockapp.designsystem.components.StockAppAvatar
import com.danilobarreto.stockapp.designsystem.components.StockAppErrorBanner
import com.danilobarreto.stockapp.designsystem.icons.StockAppIcons
import com.danilobarreto.stockapp.designsystem.theme.StockAppColors
import com.danilobarreto.stockapp.designsystem.theme.StockAppShapes
import com.danilobarreto.stockapp.designsystem.theme.StockAppTypography
import com.danilobarreto.stockapp.designsystem.util.toBrl

internal const val SEARCH_DEBOUNCE_MS = 400L
private val FULL_TICKER = Regex("^[A-Z]{4}\\d{1,2}$")

/** "PETR4", "HGLG11" → true; "PET", "PETR" → false */
internal fun String.isFullTicker(): Boolean = FULL_TICKER.matches(this)

sealed interface AssetSearchUiState {
    data object Idle : AssetSearchUiState
    data object Loading : AssetSearchUiState
    data class Found(val ticker: String, val price: Double, val name: String? = null) : AssetSearchUiState
    data class NotFound(val ticker: String) : AssetSearchUiState
    data class Error(val message: String) : AssetSearchUiState
}

/** Lista de populares filtrada pelo texto + resultado da busca remota, quando houver. */
@Composable
internal fun AssetSearchResults(
    listState: AssetListUiState,
    query: String,
    searchState: AssetSearchUiState,
    onItemClick: (String) -> Unit,
) {
    val filtered = if (listState is AssetListUiState.Success && query.isNotBlank()) {
        listState.copy(items = listState.items.filter { it.ticker.contains(query) })
    } else {
        listState
    }
    val hasMatches = filtered is AssetListUiState.Success && filtered.items.isNotEmpty()

    // Lista some só quando há texto e nenhum item bate — aí quem fala é a busca remota.
    if (query.isBlank() || hasMatches || filtered !is AssetListUiState.Success) {
        AssetListSection(filtered, onItemClick = onItemClick)
    }

    when (searchState) {
        AssetSearchUiState.Idle ->
            if (query.isNotBlank() && !hasMatches && !query.isFullTicker()) {
                SearchHint("Digite o ticker completo para buscar (ex.: PETR4).")
            }
        AssetSearchUiState.Loading ->
            Box(Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = StockAppColors.primary)
            }
        is AssetSearchUiState.Found ->
            SearchResultRow(searchState, onClick = { onItemClick(searchState.ticker) })
        is AssetSearchUiState.NotFound ->
            SearchHint("Nenhum ativo encontrado para ${searchState.ticker}.")
        is AssetSearchUiState.Error ->
            StockAppErrorBanner(searchState.message, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
private fun SearchResultRow(result: AssetSearchUiState.Found, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(StockAppShapes.cardRadius)
            .background(StockAppColors.surface2)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StockAppAvatar(
            imageUrl = null,
            fallbackText = result.ticker.take(4),
            fallbackBackgroundColor = StockAppColors.primaryTint,
            fallbackTextColor = StockAppColors.primaryDeep,
            size = 44.dp,
            textStyle = StockAppTypography.labelMedium.copy(fontWeight = FontWeight.Bold),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(result.ticker, style = StockAppTypography.titleMedium, color = StockAppColors.textPrimary)
            Text(
                result.name ?: "Ver detalhes",
                style = StockAppTypography.bodySmall,
                color = StockAppColors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(result.price.toBrl(), style = StockAppTypography.titleMedium, color = StockAppColors.textPrimary)
        Icon(StockAppIcons.ChevronRight, contentDescription = null, tint = StockAppColors.textMuted, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun SearchHint(text: String) {
    Text(
        text,
        style = StockAppTypography.bodyMedium,
        color = StockAppColors.textMuted,
        modifier = Modifier.padding(top = 24.dp),
    )
}