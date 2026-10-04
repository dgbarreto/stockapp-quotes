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
    onOpenDetail: (String) -> Unit,
) {
    LaunchedEffect(Unit) { viewModel.loadPopular() }

    val listState by viewModel.listUiState.collectAsState()
    val query by viewModel.query.collectAsState()
    val searchState by viewModel.searchState.collectAsState()

    AssetSearchResults(listState, query, searchState, onItemClick = onOpenDetail)
}
