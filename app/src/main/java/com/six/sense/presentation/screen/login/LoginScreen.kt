package com.six.sense.presentation.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.six.sense.ui.theme.SixSenseAndroidTheme

/**
 * Login screen
 *
 * @param modifier Modifier for the screen.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val emailTextFieldState = rememberTextFieldState()
    val passwordTextFieldState = rememberTextFieldState()
    var passwordVisible = remember { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Login")
        }, expandedHeight = TopAppBarDefaults.TopAppBarExpandedHeight)
    }) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AuthRegularField(
                modifier = Modifier.fillMaxWidth(),
                textFieldState = emailTextFieldState,
                label = "Email"
            )
            AuthSecureField(
                modifier = Modifier.fillMaxWidth(),
                textFieldState = passwordTextFieldState,
                label = "Password",
                isPasswordVisible = passwordVisible,
            )
            TextButton(modifier = Modifier.align(Alignment.End), onClick = {}) {
                Text(
                    "Forgot password?",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                    Text("Login", style = MaterialTheme.typography.bodyMedium)
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Don't have an account?")
                    TextButton(onClick = {}) {
                        Text(
                            "Create account",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DefPrev() {
    SixSenseAndroidTheme {
        LoginScreen()
    }
}

/**
 * Auth regular field.
 *
 * @param modifier Modifier for the field.
 * @param textFieldState State of the text field.
 * @param label Label of the field.
 * @param prefix Prefix of the field.
 */
@Composable
fun AuthRegularField(
    modifier: Modifier = Modifier, textFieldState: TextFieldState, label: String,
    prefix: @Composable (() -> Unit)? = null,
) {
    TextField(
        modifier = modifier,
        state = textFieldState,
        lineLimits = TextFieldLineLimits.SingleLine,
        label = { Text(label) },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        prefix = prefix
    )
}

/**
 * Auth secure field.
 *
 * @param modifier Modifier for the field.
 * @param textFieldState State of the text field.
 * @param label Label of the field.
 * @param isPasswordVisible Visibility of the password.
 */
@Composable
fun AuthSecureField(
    modifier: Modifier = Modifier, textFieldState: TextFieldState, label: String,
    isPasswordVisible: MutableState<Boolean>,
) {
    SecureTextField(
        modifier = modifier,
        state = textFieldState,
        label = { Text(label) },
        textObfuscationMode = if (!isPasswordVisible.value) TextObfuscationMode.RevealLastTyped else TextObfuscationMode.Visible,
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                Icon(
                    if (isPasswordVisible.value) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = null
                )
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}