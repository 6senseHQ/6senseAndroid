package com.six.sense.presentation.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.six.sense.ui.theme.SixSenseAndroidTheme

/**
 *mask input, clipboard protection, accessibility, autofill
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SignupScreen(modifier: Modifier = Modifier) {
    val firstNameTextFieldState = rememberTextFieldState()
    val lastNameTextFieldState = rememberTextFieldState()
    val emailTextFieldState = rememberTextFieldState()
    val phoneTextFieldState = rememberTextFieldState()
    val passwordTextFieldState = rememberTextFieldState()
    val confirmPasswordTextFieldState = rememberTextFieldState()

    var passwordVisible = remember { mutableStateOf(false) }
    var confirmPasswordVisible = remember { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Create Account")
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
                textFieldState = firstNameTextFieldState,
                label = "First Name"
            )
            AuthRegularField(
                modifier = Modifier.fillMaxWidth(),
                textFieldState = lastNameTextFieldState,
                label = "Last Name"
            )
            AuthRegularField(
                modifier = Modifier.fillMaxWidth(),
                textFieldState = emailTextFieldState,
                label = "Email Address"
            )
            AuthRegularField(
                modifier = Modifier.fillMaxWidth(),
                textFieldState = phoneTextFieldState,
                label = "Phone Number",
                prefix = {
                    Text(
                        text = "+98",
                    )
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Password must be 9 characters or more and contain at least 1 upper-case, 1 lower-case, & 1 number.",
                style = MaterialTheme.typography.bodySmall
            )
            AuthSecureField(
                modifier = Modifier.fillMaxWidth(),
                textFieldState = passwordTextFieldState,
                label = "Password",
                isPasswordVisible = passwordVisible,
            )
            AuthSecureField(
                modifier = Modifier.fillMaxWidth(),
                textFieldState = confirmPasswordTextFieldState,
                label = "Confirm Password",
                isPasswordVisible = confirmPasswordVisible,
            )
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "By clicking Sign Up, you agree to our Terms, Privacy Policy and Cookies Policy.",
                    style = MaterialTheme.typography.bodySmall
                )
                Button(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                    Text("Create Account", style = MaterialTheme.typography.bodyMedium)
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
        SignupScreen()
    }
}
