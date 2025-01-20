package com.six.sense.presentation.screen.materialComponents.primaryTab

import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.six.sense.presentation.screen.materialComponents.MaterialComponents

/**
 * Primary tab items
 *
 * @param modifier Modifier for the layout.
 * @param selectedTabIndex Index.
 * @param onClick On click.
 * @receiver Modifier for the layout.
 */
@Composable
fun PrimaryTabItems(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onClick: (Int) -> Unit,
) {
    MaterialComponents.componentsList.forEachIndexed { index, title ->
        Tab(
            selected = selectedTabIndex == index,
            onClick = { onClick(index) },
            modifier = modifier,
            text = { Text(title) }
        )
    }
}
