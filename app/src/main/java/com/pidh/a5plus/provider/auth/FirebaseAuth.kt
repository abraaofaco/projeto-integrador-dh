package com.pidh.a5plus.provider.auth

import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuth
import com.pidh.a5plus.provider.IAuthProvider
import kotlinx.coroutines.tasks.await


class FirebaseAuth : IAuthProvider {

    private val mAuth = FirebaseAuth.getInstance()

    override suspend fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signInGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential).await()
    }

    override suspend fun signInFacebook(token: String) {
        val credential = FacebookAuthProvider.getCredential(token)
        mAuth.signInWithCredential(credential).await()
    }

    override suspend fun linkGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.currentUser!!.linkWithCredential(credential).await()
    }

    override suspend fun linkFacebook(token: String) {
        val credential = FacebookAuthProvider.getCredential(token)
        mAuth.currentUser!!.linkWithCredential(credential).await()
    }

    override suspend fun signUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun forgotPassword(email: String) {
        mAuth.sendPasswordResetEmail(email).await()
    }

    override fun signOut() {
        mAuth.signOut()
    }

    override fun currentUser(): FirebaseUser? {
        return mAuth.currentUser
    }
}