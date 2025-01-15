package com.six.sense.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.navigation.graph.SetupMainNavGraph
import com.six.sense.presentation.screen.chat.ChatView
import com.six.sense.ui.theme.SixSenseAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity of the application.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SixSenseAndroidTheme {
                val navController = rememberNavController()
                SetupMainNavGraph(
                    startDestination = if(Firebase.auth.currentUser == null) Screens.Login else Screens.Home,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SixSenseAndroidTheme {
        Greeting("Android")
    }
}