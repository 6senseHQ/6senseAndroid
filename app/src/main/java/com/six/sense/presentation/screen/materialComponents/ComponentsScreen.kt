package com.six.sense.presentation.screen.materialComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.os.ConfigurationCompat
import com.six.sense.presentation.screen.materialComponents.materialBottomNav.MaterialBottomNavigation
import com.six.sense.presentation.screen.materialComponents.materialBottomSheet.MaterialBottomSheet
import com.six.sense.presentation.screen.materialComponents.materialList.MaterialList
import com.six.sense.presentation.screen.materialComponents.materialTopBar.MaterialTopBar
import com.six.sense.presentation.screen.materialComponents.primaryTab.PrimaryTabItems
import com.six.sense.presentation.screen.materialComponents.textFields.MaterialTextFields
import com.six.sense.ui.theme.SixSenseAndroidTheme
import java.util.Locale

/**
 * Components screen.
 *
 * @param modifier Modifier for the layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentsScreen(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val locale = ConfigurationCompat.getLocales(configuration).get(0) ?: Locale.getDefault()
    val layoutDirection = if (locale.language == "ar" || locale.language == "he") {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }
    Scaffold(
        modifier = modifier, contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        /**
         * Selected tab index.
         */
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        /**
         * Pager state.
         */
        val pagerState = rememberPagerState { MaterialComponents.componentsList.size }
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                PrimaryScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                ) {
                    PrimaryTabItems(
                        selectedTabIndex = selectedTabIndex,
                        onClick = { selectedTabIndex = it })
                }
            }
            HorizontalPager(state = pagerState, userScrollEnabled = false) { pagerItems ->
                when (selectedTabIndex) {
                    0 -> MaterialTextFields()
                    1 -> MaterialList()
                    2 -> MaterialBottomNavigation()
                    3 -> MaterialBottomSheet()
                    4 -> MaterialTopBar()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefPrev() {
    SixSenseAndroidTheme {
        ComponentsScreen()
    }
}