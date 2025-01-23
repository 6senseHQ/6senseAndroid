package com.six.sense.domain.repo

import android.app.Activity
import com.facebook.login.LoginResult
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseUser

/**
 * Repository interface for handling authentication-related operations.
 */
interface AuthRepo {

    /**
     * Signs in a user using Firebase authentication.
     * @param credential The authentication credential.
     * @return [FirebaseUser] object containing user details.
     */
    suspend fun firebaseSignIn(credential: Any): FirebaseUser

    /**
     * Signs in a user using Google Sign-In.
     * @param activity The activity context.
     * @return [GoogleIdTokenCredential] containing the Google ID token.
     */
    suspend fun googleSignIn(activity: Activity): GoogleIdTokenCredential

    /**
     * Signs in a user using Facebook Login.
     * @param activity The activity context.
     * @return [LoginResult] containing the result of the Facebook login attempt.
     */
    suspend fun facebookSignIn(activity: Activity): LoginResult
}