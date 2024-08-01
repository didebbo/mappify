package com.didebbo.mappify.data.provider

import com.didebbo.mappify.data.model.UserAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import kotlin.coroutines.suspendCoroutine


class FirebaseDataProvider {
    private val auth: FirebaseAuth = Firebase.auth
    private val currentUser = auth.currentUser

    init {
        auth.signOut()
    }

    fun isSignedIn(): Boolean {
        return currentUser != null
    }

    fun getUser(): FirebaseUser? {
        return currentUser
    }

    fun createUserWithEmailAndPassword(userAuth: UserAuth) {
        auth.createUserWithEmailAndPassword(userAuth.email, userAuth.password)
    }

    suspend fun signInWithEmailAndPassword(userAuth: UserAuth): Boolean {
        return try {
            auth.signInWithEmailAndPassword(userAuth.email,userAuth.password).await().user != null
        } catch (e: Exception) {
            false
        }

    }


}