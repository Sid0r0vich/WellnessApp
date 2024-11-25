package com.example.wellness.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wellness.R
import com.example.wellness.data.HomeScreenData
import com.example.wellness.ui.components.HomeUserCard
import com.example.wellness.ui.viewModels.AppViewModelProvider
import com.example.wellness.ui.viewModels.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onDynamic: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val onClicks = arrayOf({}, onDynamic, {})

    DefaultScreen {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight()
        ) {
            item {
                HomeUserCard(userName = uiState.userName)
            }
            item {
                DefaultSpacer()
                IndicatorsPanel(
                    paddingGrid = LocalGridPadding.current * 2,
                    values = listOf(42, 13),
                    painters = listOf(
                        painterResource(R.drawable.trace),
                        painterResource(R.drawable.heart)
                    ),
                )
            }
            itemsIndexed(HomeScreenData.panelLabels zip HomeScreenData.panelImageIds) { idx, panel ->
                DefaultSpacer()
                AppPanel(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(panel.first),
                    painter = painterResource(panel.second),
                    onClick = onClicks[idx]
                )
            }
        }
    }
}

@Composable
fun AppPanel(
    modifier: Modifier = Modifier,
    text: String,
    painter: Painter,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(PaddingValues(
                    horizontal = 25.dp,
                    vertical = 20.dp,
                )),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier,
            )
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
            )
        }
    }
}

@Composable
fun IndicatorsPanel(
    modifier: Modifier = Modifier,
    paddingGrid: Dp,
    values: List<Int>,
    painters: List<Painter>,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        (values zip painters).forEachIndexed { idx, indicator ->
            AppPanel(
                modifier = Modifier.weight(1.0F),
                text = indicator.first.toString(),
                painter = indicator.second,
            )
            if (idx != values.size - 1) DefaultSpacer()
        }
    }
}