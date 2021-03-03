package com.pidh.a5plus.provider

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

interface IAuthProvider {
    suspend fun signIn(email: String, password: String)

    suspend fun signInGoogle(idToken: String)

    suspend fun signInFacebook(token: String)

    suspend fun linkGoogle(idToken: String)

    suspend fun linkFacebook(token: String)

    suspend fun signUp(email: String, password: String)

    suspend fun forgotPassword(email: String)

    fun signOut()

    fun currentUser(): FirebaseUser?
}