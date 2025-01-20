package com.six.sense.presentation.screen.materialComponents.materialList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Crop
import androidx.compose.material.icons.rounded.DataUsage
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.six.sense.presentation.screen.materialComponents.ComponentInfo
import ir.kaaveh.sdpcompose.sdp

/**
 * Material list
 *
 * @param modifier Modifier for the layout.
 */
@Composable
fun MaterialList(modifier: Modifier = Modifier) {
    /**
     * Is switch checked.
     */
    var isSwitchChecked by remember { mutableStateOf(false) }
    Column(
        modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.sdp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ComponentInfo(
            title = "Material List Item",
            description = arrayOf(
                "Lists are continuous, vertical indexes of text and images",
                "",
                "Height: The \"tallest\" element within a list item determines the list item’s height – either 56dp, 72dp, or 88dp"
            )
        )
        Column(
            modifier = Modifier.padding(16.sdp),
            verticalArrangement = Arrangement.spacedBy(16.sdp)
        ) {
            Text(text = "List Item Variant \"Small\"", style = MaterialTheme.typography.titleMedium)
            ListItem(
                modifier = Modifier,
                leadingContent = {
                    Icon(
                        Icons.Rounded.Crop,
                        contentDescription = "Icon"
                    )
                },
                headlineContent = { Text("Headline Content") },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            )
            Text(
                text = "List Item Variant \"Trailing Icon\"",
                style = MaterialTheme.typography.titleMedium
            )
            ListItem(
                modifier = Modifier,
                headlineContent = { Text("Headline Content") },
                supportingContent = { Text("Supporting Content") },
                trailingContent = {
                    Switch(
                        checked = isSwitchChecked,
                        onCheckedChange = { isSwitchChecked = it },
                        thumbContent = {
                            if (isSwitchChecked) {
                                Icon(Icons.Rounded.Check, contentDescription = "Icon")
                            }
                        }, colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            checkedIconColor =
                            MaterialTheme.colorScheme.primary
                        )
                    )
                },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            )
            Text(
                text = "List Item Variant \"3 Lines\"",
                style = MaterialTheme.typography.titleMedium
            )
            ListItem(
                modifier = Modifier,
                headlineContent = { Text("Headline Content") },
                supportingContent = { Text("Supporting Content") },
                overlineContent = { Text("Overline Content") },
                leadingContent = {
                    Icon(Icons.Rounded.DataUsage, contentDescription = "Icon")
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    leadingIconColor = MaterialTheme.colorScheme.primary
                ),
                trailingContent = {
                    Text(text = "100+", style = MaterialTheme.typography.labelSmall)
                }
            )
        }
    }
}
