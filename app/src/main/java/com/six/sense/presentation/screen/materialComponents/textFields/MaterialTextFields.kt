package com.six.sense.presentation.screen.materialComponents.textFields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.six.sense.presentation.screen.materialComponents.ComponentInfo

@Composable
fun MaterialTextFields(modifier: Modifier = Modifier) {
    val (filledTextField, setFilledTextFieldValue) = remember { mutableStateOf("") }
    val (outlinedTextField, setOutlinedTextFieldValue) = remember { mutableStateOf("") }
    Column(
        modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ComponentInfo(
            modifier = Modifier, title = "Material Text Fields",
            description = arrayOf(
                "Both types of text fields use a container to provide a visual cue for interaction and provide the same functionality",
                "",
                "Outlined text fields have less visual emphasis than filled text fields. When they appear in places like forms (where many text fields are placed together) their reduced emphasis helps simplify the layout."
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Filled text field", style = MaterialTheme.typography.titleMedium)
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = filledTextField,
                onValueChange = setFilledTextFieldValue,
                label = { Text(text = "Label") },
                prefix = { Text(text = "Prefix Sample") },
                supportingText = { Text(text = "Supporting text") }
            )
            Text(text = "Outlined text field", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = outlinedTextField,
                onValueChange = setOutlinedTextFieldValue,
                placeholder = { Text(text = "Placeholder") },
                label = { Text(text = "Label") },
                suffix = { Text(text = "Suffix Sample") },
                isError = true,
                supportingText = { Text(text = "Error text") }
            )
        }
    }
}