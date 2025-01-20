package com.six.sense.presentation.screen.materialComponents.materialBottomSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.six.sense.presentation.screen.materialComponents.ComponentInfo
import kotlinx.coroutines.launch

/**
 * Material bottom sheet.
 *
 * @param modifier Modifier for the layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialBottomSheet(modifier: Modifier = Modifier) {
    /**
     * Sheet state.
     */
    val sheetState = rememberModalBottomSheetState()
    /**
     * Coroutine Scope.
     */
    val scope = rememberCoroutineScope()
    /**
     * Show bottom sheet.
     */
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Show sheet") },
                icon = { Icon(Icons.Outlined.Add, contentDescription = "") },
                onClick = { showBottomSheet = true }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
        ) {
            ComponentInfo(
                modifier = Modifier,
                title = "Bottom sheets",
                description = arrayOf(
                    "Bottom sheets show secondary content anchored to the bottom of the screen",
                    "",
                    "Bottom sheets can be dismissed in order to interact with the main content",
                    "",
                    "Modal bottom sheets are above a scrim while standard bottom sheets don't have a scrim. Besides this, both types of bottom sheets have the same specs."
                )
            )
            if (showBottomSheet)
                ModalBottomSheet(
                    modifier = Modifier.padding(contentPadding).fillMaxSize(),
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    // Sheet content
                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally), onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }) {
                        Text(
                            text = "Hide bottom sheet"
                        )
                    }
                }
        }
    }
}