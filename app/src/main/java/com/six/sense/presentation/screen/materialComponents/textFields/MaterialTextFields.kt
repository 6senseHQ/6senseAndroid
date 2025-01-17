package com.six.sense.presentation.screen.materialComponents.textFields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MaterialTextFields(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Both types of text fields use a container to provide a visual cue for interaction and provide the same functionality,")
        TextField(modifier =Modifier.fillMaxWidth(),value = "", onValueChange = { it })
        Text(text = "Outlined text fields have less visual emphasis than filled text fields. When they appear in places like forms (where many text fields are placed together) their reduced emphasis helps simplify the layout.")
        OutlinedTextField(modifier =Modifier.fillMaxWidth(),value = "", onValueChange = { it })
    }
}