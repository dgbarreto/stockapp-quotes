package com.danilobarreto.stockapp.quotes.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danilobarreto.stockapp.designsystem.components.StockAppAreaLineChart
import com.danilobarreto.stockapp.designsystem.components.StockAppCard
import com.danilobarreto.stockapp.designsystem.components.StockAppErrorBanner
import com.danilobarreto.stockapp.designsystem.components.StockAppPrimaryButton
import com.danilobarreto.stockapp.designsystem.components.StockAppSegmentedControl
import com.danilobarreto.stockapp.designsystem.icons.StockAppIcons
import com.danilobarreto.stockapp.designsystem.theme.StockAppColors
import com.danilobarreto.stockapp.designsystem.theme.StockAppShapes
import com.danilobarreto.stockapp.designsystem.theme.StockAppSpacing
import com.danilobarreto.stockapp.designsystem.theme.StockAppTypography
import com.danilobarreto.stockapp.designsystem.util.toBrNumber
import com.danilobarreto.stockapp.designsystem.util.toBrPercent
import com.danilobarreto.stockapp.designsystem.util.toBrl
import com.danilobarreto.stockapp.quotes.domain.AssetDetailSummary
import com.danilobarreto.stockapp.quotes.domain.AssetIndicator
import com.danilobarreto.stockapp.quotes.domain.IndicatorFormat
import com.danilobarreto.stockapp.quotes.domain.PriceRange

@Composable
fun QuoteDetailScreen(
    viewModel: QuoteDetailViewModel,
    ticker: String,
    onBack: () -> Unit,
    onNewOrder: () -> Unit,
    onAlert: () -> Unit,
) {
    LaunchedEffect(ticker) { viewModel.load(ticker) }
    val uiState by viewModel.uiState.collectAsState()
    val pricesUiState by viewModel.pricesUiState.collectAsState()
    val selectedRange by viewModel.selectedRange.collectAsState()

    AssetDetailScreen(
        ticker = ticker,
        uiState = uiState,
        pricesUiState = pricesUiState,
        selectedRange = selectedRange,
        onRangeSelected = { viewModel.selectRange(ticker, it) },
        onBack = onBack,
        onNewOrder = onNewOrder,
        onAlert = onAlert,
    )
}

@Composable
fun FiiDetailScreen(
    viewModel: FiiDetailViewModel,
    ticker: String,
    onBack: () -> Unit,
    onNewOrder: () -> Unit,
    onAlert: () -> Unit,
) {
    LaunchedEffect(ticker) { viewModel.load(ticker) }
    val uiState by viewModel.uiState.collectAsState()
    val pricesUiState by viewModel.pricesUiState.collectAsState()
    val selectedRange by viewModel.selectedRange.collectAsState()

    AssetDetailScreen(
        ticker = ticker,
        uiState = uiState,
        pricesUiState = pricesUiState,
        selectedRange = selectedRange,
        onRangeSelected = { viewModel.selectRange(ticker, it) },
        onBack = onBack,
        onNewOrder = onNewOrder,
        onAlert = onAlert,
    )
}

@Composable
private fun AssetDetailScreen(
    ticker: String,
    uiState: AssetDetailUiState,
    pricesUiState: PricesUiState,
    selectedRange: PriceRange,
    onRangeSelected: (PriceRange) -> Unit,
    onBack: () -> Unit,
    onNewOrder: () -> Unit,
    onAlert: () -> Unit,
) {
    val summary = (uiState as? AssetDetailUiState.Success)?.summary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StockAppColors.surface1)
            .verticalScroll(rememberScrollState())
    ) {
        DetailHeader(ticker = ticker, summary = summary, onBack = onBack, onAlert = onAlert)

        when (uiState) {
            is AssetDetailUiState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is AssetDetailUiState.Error -> {
                StockAppErrorBanner(uiState.message, modifier = Modifier.padding(16.dp))
            }
            is AssetDetailUiState.Success -> {
                Column(modifier = Modifier.padding(horizontal = StockAppSpacing.screenHorizontal, vertical = StockAppSpacing.lg)) {
                    ChartCard(
                        pricesUiState = pricesUiState,
                        selectedRange = selectedRange,
                        onRangeSelected = onRangeSelected,
                        positive = uiState.summary.changePercent >= 0,
                    )
                    IndicatorsGrid(uiState.summary.indicators, modifier = Modifier.padding(top = 20.dp))

                    Row(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StockAppPrimaryButton(text = "Nova ordem", onClick = onNewOrder, modifier = Modifier.weight(1f))
                        SecondaryButton(icon = StockAppIcons.Bell, label = "Alerta", onClick = onAlert, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailHeader(ticker: String, summary: AssetDetailSummary?, onBack: () -> Unit, onAlert: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(StockAppColors.primary, shape = StockAppShapes.headerBottomRadius)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
            .padding(
                start = StockAppSpacing.screenHorizontal,
                end = StockAppSpacing.screenHorizontal,
                top = StockAppSpacing.headerTop,
                bottom = StockAppSpacing.headerBottom,
            )
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            HeaderIconButton(icon = StockAppIcons.ArrowLeft, contentDescription = "Voltar", onClick = onBack)
            HeaderIconButton(icon = StockAppIcons.Bell, contentDescription = "Criar alerta", onClick = onAlert)
        }

        Text(
            ticker,
            style = StockAppTypography.headerTitle.copy(fontSize = 22.sp),
            color = StockAppColors.onPrimary,
            modifier = Modifier.padding(top = 16.dp),
        )

        summary?.companyName?.let {
            Text(
                it,
                style = StockAppTypography.bodyMedium,
                color = StockAppColors.onPrimary.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp),
            )
        }

        summary?.let {
            Text(
                it.price.toBrl(),
                style = StockAppTypography.displayLarge,
                color = StockAppColors.onPrimary,
                modifier = Modifier.padding(top = 4.dp),
            )
            val positive = it.changePercent >= 0
            val sign = if (positive) "+" else ""
            Text(
                "${it.changePercent.toBrPercent(signed = true)} hoje",
                style = StockAppTypography.labelMedium,
                color = StockAppColors.onPrimary,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .background(StockAppColors.onPrimary.copy(alpha = 0.16f), shape = RoundedCornerShape(100))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }
    }
}

@Composable
private fun HeaderIconButton(icon: ImageVector, contentDescription: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(StockAppColors.onPrimary.copy(alpha = 0.14f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = contentDescription, tint = StockAppColors.onPrimary, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun ChartCard(
    pricesUiState: PricesUiState,
    selectedRange: PriceRange,
    onRangeSelected: (PriceRange) -> Unit,
    positive: Boolean,
) {
    StockAppCard {
        StockAppSegmentedControl(
            options = PriceRange.entries.map { it.label },
            selectedIndex = PriceRange.entries.indexOf(selectedRange),
            onOptionSelected = { onRangeSelected(PriceRange.entries[it]) },
        )

        when (pricesUiState) {
            is PricesUiState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth().height(140.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is PricesUiState.Error -> {
                StockAppErrorBanner(pricesUiState.message, modifier = Modifier.padding(top = 16.dp))
            }
            is PricesUiState.Success -> {
                val color = if (positive) StockAppColors.textSuccess else StockAppColors.textDanger
                if (pricesUiState.closes.size >= 2) {
                    StockAppAreaLineChart(
                        values = pricesUiState.closes.map { it.toFloat() },
                        modifier = Modifier.padding(top = 16.dp),
                        height = 140.dp,
                        lineColor = color,
                        areaColor = color.copy(alpha = 0.12f),
                    )
                } else {
                    Text(
                        "Sem histórico suficiente para este período.",
                        style = StockAppTypography.bodySmall,
                        color = StockAppColors.textMuted,
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun IndicatorsGrid(indicators: List<AssetIndicator>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        indicators.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowItems.forEach { indicator -> IndicatorCard(indicator, modifier = Modifier.weight(1f)) }
                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun IndicatorCard(indicator: AssetIndicator, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(StockAppColors.surface2, shape = StockAppShapes.cardRadius)
            .padding(14.dp),
    ) {
        Text(indicator.label, style = StockAppTypography.labelSmall, color = StockAppColors.textMuted)
        Text(
            formatIndicatorValue(indicator),
            style = StockAppTypography.titleMedium,
            color = StockAppColors.textPrimary,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

private fun formatIndicatorValue(indicator: AssetIndicator): String {
    val value = indicator.value ?: return "—"
    return when (indicator.format) {
        IndicatorFormat.RATIO -> value.toBrNumber(2)
        IndicatorFormat.PERCENT -> value.toBrPercent()
        IndicatorFormat.CURRENCY -> value.toBrl()
    }
}

@Composable
private fun SecondaryButton(icon: ImageVector, label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(44.dp)
            .background(StockAppColors.surface2, shape = StockAppShapes.pillRadius)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = StockAppColors.primary, modifier = Modifier.size(18.dp))
        Text(
            label,
            style = StockAppTypography.buttonLabel,
            color = StockAppColors.textPrimary,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}