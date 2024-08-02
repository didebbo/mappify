package com.didebbo.mappify.data.provider

import com.didebbo.mappify.data.model.UserAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await


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

    suspend  fun signInWithEmailAndPassword(userAuth: UserAuth): Result<FirebaseUser> {
        return  try {
            val user = auth.signInWithEmailAndPassword(userAuth.email,userAuth.password).await().user
            if(user != null) Result.success(user)
            else Result.failure(Exception("User not Found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}