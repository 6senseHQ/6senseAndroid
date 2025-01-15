package com.six.sense.data.repo

import android.app.Activity
import android.content.Context
import android.credentials.GetCredentialException
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.six.sense.domain.ConnectivityObserver
import com.six.sense.domain.repo.AuthRepo
import com.six.sense.utils.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Repository implementation for handling authentication-related operations.
 * @property context The application context.
 * @property dispatcher The coroutine dispatcher for performing asynchronous operations.
 * @property connectivity The connectivity observer for monitoring network connectivity.
 */
class AuthRepoImpl(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
    private val connectivity: ConnectivityObserver,
) : AuthRepo {

    override suspend fun firebaseSignIn(credential: Any): FirebaseUser = withContext(dispatcher) {
        connectivity.checkAndThrow4NoInternet()
        suspendCancellableCoroutine { continuation ->
            val firebaseCredential = when (credential) {
                is AccessToken -> FacebookAuthProvider.getCredential(credential.token)
                is GoogleIdTokenCredential -> GoogleAuthProvider.getCredential(
                    credential.idToken,
                    null
                )

                else -> error("Invalid credential type")
            }
            Firebase.auth.signInWithCredential(firebaseCredential)
                .addOnSuccessListener { result ->
                    Log.d("TAG", "firebase:onSuccess:${result.user}")
                    val user = result.user
                    if (user != null)
                        continuation.resume(user)
                    else
                        error("User is null")
                }.addOnFailureListener {
                    Log.d("TAG", "firebase:onFailed:${it.localizedMessage}")
                    continuation.resumeWithException(it)
                }
        }
    }


    override suspend fun googleSignIn(activity: Activity): GoogleIdTokenCredential =
        withContext(dispatcher) {
            connectivity.checkAndThrow4NoInternet()
            val signInGoogleOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(Constants.WEB_CLIENT_ID)
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(signInGoogleOption)
                .build()
            try {
                val result = CredentialManager
                    .create(activity)
                    .getCredential(activity, request)
                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    googleCredential
                } else
                    error("Only Google Account Allowed")
            } catch (e: GetCredentialCancellationException) {
                if (e.type == GetCredentialException.TYPE_USER_CANCELED) error("null")
                else throw e
            }
        }

    private val callbackManager by lazy { CallbackManager.Factory.create() }

    override suspend fun facebookSignIn(
        button: LoginButton,
    ): LoginResult = withContext(dispatcher) {
        connectivity.checkAndThrow4NoInternet()
        suspendCancellableCoroutine { continuation ->
            button.permissions = listOf("public_profile", "user_link")

            button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.d("TAG1", "facebook:onSuccess:${result.accessToken} $result ")
                    continuation.resume(result)
                }

                override fun onCancel() {
                    Log.d("TAG2", "facebook:onCancel")
                    //"Canceled by user."
                    continuation.resumeWithException(Exception("null"))
                }

                override fun onError(error: FacebookException) {
                    Log.d("TAG3", "facebook:onError", error)
                    continuation.resumeWithException(error)
                }
            })
        }
    }

}