package com.six.sense.presentation.screen.materialComponents.materialTopBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.six.sense.presentation.screen.materialComponents.ComponentInfo

/**
 * Material top bar.
 *
 * @param modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialTopBar(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize()) {
        ComponentInfo(
            title = "Material Top App Bar",
            description = arrayOf(
                "Top app bars display navigation, actions, and text at the top of a screen",
                "",
                "On scroll, apply a container fill color to separate app bar from body content Top app bars have the same width as the device window",
            )
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "\"Small\"Top App Bar", style = MaterialTheme.typography.titleMedium)
            TopAppBar(
                actions = {
                    IconButton({}) {
                        Icon(Icons.Outlined.MoreVert, null)
                    }
                },
                navigationIcon = {
                    IconButton({}) {
                        Icon(Icons.Outlined.ArrowBackIosNew, null)
                    }
                },
                title = { Text(text = "Small App Bar") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
            Text(text = "\"Center\"Top App Bar", style = MaterialTheme.typography.titleMedium)
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Center Aligned App Bar",
                    )
                }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
            Text(text = "\"Medium\"Top App Bar", style = MaterialTheme.typography.titleMedium)
            MediumTopAppBar(
                title = { Text("Medium App Bar") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
            Text(text = "\"Large\"Top App Bar", style = MaterialTheme.typography.titleMedium)
            LargeTopAppBar(
                title = { Text("Large App Bar") }, colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    }
}