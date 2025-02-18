package com.six.sense.presentation.screen.materialComponents

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ir.kaaveh.sdpcompose.sdp

/**
 * Component info
 *
 * @param modifier Modifier for the layout.
 * @param title Title.
 * @param description Description.
 */
@Composable
fun ComponentInfo(modifier: Modifier = Modifier, title: String, vararg description: String) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        Text(
            modifier = modifier.align(Alignment.Start),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = modifier.align(Alignment.Start),
            text = description.joinToString("\n"),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


/**
 * Component info
 *
 * @param modifier Modifier for the layout.
 * @param title Title.
 * @param description Description.
 */
@Composable
fun ComponentInfoV(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    @StringRes vararg description: Int,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        Text(
            modifier = modifier.align(Alignment.Start),
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium
        )
        description.forEach {
            Text(
                modifier = modifier.align(Alignment.Start),
                text = stringResource(it),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}