package com.six.sense.presentation.screen.auth

import android.annotation.SuppressLint
import android.os.LocaleList
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.google.android.gms.common.SignInButton
import com.six.sense.R
import com.six.sense.databinding.FbLoginBtnBinding
import com.six.sense.databinding.GoogleLoginBtnBinding
import com.six.sense.ui.theme.SixSenseAndroidTheme
import ir.kaaveh.sdpcompose.sdp

/**
 * A composable function that renders the login screen.
 *
 * This screen provides options for users to log in using Facebook or Google.
 *
 * @param onClickFacebookLogin A callback function that is invoked when the Facebook login button is clicked.
 * @param onClickGoogleLogin A callback function that is invoked when the Google login button is clicked.
 * @param modifier Modifier for the layout.
 */
@SuppressLint("InflateParams", "LocalContextConfigurationRead")
@Composable
fun LoginScreen(
    onClickFacebookLogin: (View) -> Unit,
    onClickGoogleLogin: (View) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var local by remember {mutableStateOf("en") }
    val localizedContext = run {
        val locales = LocaleList.forLanguageTags(local)
        val configuration = context.resources.configuration
        configuration.setLocales(locales)
        context.createConfigurationContext(configuration)
    }
    val localizedString = localizedContext.resources.getString(R.string.app_name)
    CompositionLocalProvider(){

    }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxSize()
                .imePadding()
                .padding(vertical = 50.sdp)
        ) {
            Text(
                text = localizedString,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 80.sdp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.sdp)
            ) {
                Button(onClick = {
                    local = "bn"
//                    AppCompatDelegate.setApplicationLocales(
//                        LocaleListCompat.create(Locale("bn"))
//                    )
                }) {
                    Text(text = "বাংলা")
                }
                AndroidViewBinding(
                    factory = { li, vg, atr ->
                        FbLoginBtnBinding.inflate(li, vg, atr).apply {
                            loginButton.setOnClickListener(onClickFacebookLogin)
                        }
                    },
                    update = {
                    }
                )
                Text(text = stringResource(R.string.or))
                AndroidViewBinding(
                    factory = { li, vg, atr ->
                        GoogleLoginBtnBinding.inflate(li, vg, atr).apply {
                            signInButton.setSize(SignInButton.SIZE_WIDE)
                            signInButton.setOnClickListener(onClickGoogleLogin)
                        }
                    }
                )

            }
    }
}

/**
 * A preview composable function for the LoginScreen.
 */
@Preview(showBackground = true)
@Composable
fun Previews() {
    SixSenseAndroidTheme {
        LoginScreen({
//            val btn = view.findViewById<LoginButton>(R.id.login_button)
//            btn.setPermissions("id", "name", "link")
        }, {})
    }
}