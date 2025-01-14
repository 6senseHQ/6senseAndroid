package com.six.sense.presentation.screen.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.six.sense.R

/**
 * A composable function that renders a chat message item.
 * @param modifier [Modifier] Modifier for the layout.
 * @param itemResponseText [String] The text to be displayed in the chat message item fetched from the model.
 */
@Composable
fun ChatMessageItem(modifier: Modifier = Modifier, itemResponseText: String = "Hello World") {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Companion.Center
        ) {
            Icon(
                modifier = Modifier.requiredSize(16.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_polyline_outline),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceContainer
            )
        }
        Card(
            modifier = modifier.heightIn(36.dp),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                text = itemResponseText.trim(' ', '.'),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}