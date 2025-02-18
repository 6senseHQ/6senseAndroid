package com.six.sense.presentation.screen.materialComponents.materialBottomNav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.util.fastForEachIndexed
import com.six.sense.R
import com.six.sense.presentation.screen.materialComponents.ComponentInfoV
import ir.kaaveh.sdpcompose.sdp

/**
 * Material bottom navigation
 *
 * @param modifier Modifier for the layout.
 */
@Composable
fun MaterialBottomNavigation(modifier: Modifier = Modifier) {
    /**
     * Selected index with label.
     */
    var selectedIndexWithLabel by remember { mutableIntStateOf(0) }

    /**
     * Selected index without label.
     */
    var selectedIndexWithoutLabel by remember { mutableIntStateOf(1) }

    /**
     * Outlined icons list.
     */
    val outlinedIcons = listOf(
        R.drawable.home_24px_out,
        R.drawable.person_24px,
        R.drawable.settings_24px
    )

    /**
     * Filled icons list.
     */
    val filledIcons = listOf(
        R.drawable.home_24px,
        R.drawable.person_24px_fill,
        R.drawable.settings_24px_fill
    )

    /**
     * Labels list.
     */
    val labels = listOf("Home", "Profile", "Settings")


    Column(modifier.fillMaxSize()) {
        ComponentInfoV(
            modifier = Modifier,
            title = R.string.navigation_bar,
            description = intArrayOf(R.string.nav_dsc_one, R.string.nav_dsc_two)
        )
        Column(
            modifier = Modifier
                .padding(16.sdp),
            verticalArrangement = Arrangement.spacedBy(16.sdp)
        ) {
            Text(
                text = "Material Navigation Bar \"Icons with labels\"",
                style = MaterialTheme.typography.titleMedium
            )
            NavigationBar(modifier = Modifier) {
                labels.fastForEachIndexed { index, label ->

                    NavigationBarItem(
                        selected = selectedIndexWithLabel == index,
                        onClick = { selectedIndexWithLabel = index },
                        icon = {
                            Icon(
                                if (selectedIndexWithLabel == index) ImageVector.vectorResource(
                                    filledIcons[index]
                                ) else ImageVector.vectorResource(
                                    outlinedIcons[index]
                                ),
                                null
                            )
                        },
                        label = { Text(text = label) })
                }
            }

            Text(
                text = "Material Navigation Bar \"Icons only\"",
                style = MaterialTheme.typography.titleMedium
            )
            NavigationBar(modifier = Modifier) {
                labels.fastForEachIndexed { index, label ->

                    NavigationBarItem(
                        selected = selectedIndexWithoutLabel == index,
                        onClick = { selectedIndexWithoutLabel = index },
                        icon = {
                            Icon(
                                if (selectedIndexWithoutLabel == index) ImageVector.vectorResource(
                                    filledIcons[index]
                                ) else ImageVector.vectorResource(
                                    outlinedIcons[index]
                                ),
                                null
                            )
                        },
                        label = null
                    )
                }
            }
        }
    }
}
