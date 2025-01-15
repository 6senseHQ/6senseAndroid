package com.six.sense.presentation.screen.auth

import android.annotation.SuppressLint
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.facebook.login.widget.LoginButton
import com.google.android.gms.common.SignInButton
import com.six.sense.R
import com.six.sense.databinding.FbLoginBtnBinding
import com.six.sense.databinding.GoogleLoginBtnBinding
import com.six.sense.ui.theme.SixSenseAndroidTheme

@SuppressLint("InflateParams")
@Composable
fun LoginScreen(
    onClickFacebookLogin: (LoginButton) -> Unit,
    onClickGoogleLogin: (View) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(vertical = 50.dp)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 80.dp)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AndroidViewBinding(
                factory = FbLoginBtnBinding::inflate,
                update = {
                    onClickFacebookLogin(loginButton)
                }
            )
            Text(text = "or", modifier = Modifier.padding(top=5.dp, bottom = 8.dp))
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

@Preview(showBackground = true)
@Composable
fun Previews() {
    SixSenseAndroidTheme {
        LoginScreen ({
//            val btn = view.findViewById<LoginButton>(R.id.login_button)
//            btn.setPermissions("id", "name", "link")
        },{})
    }
}