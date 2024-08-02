package com.didebbo.mappify.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.didebbo.mappify.data.model.UserAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await


class FirebaseDataProvider {
    private val auth: FirebaseAuth = Firebase.auth
    private val _currentUser: MutableLiveData<FirebaseUser?> = MutableLiveData(auth.currentUser)
    private val currentUser: LiveData<FirebaseUser?> = _currentUser

    fun getUser(): LiveData<FirebaseUser?> {
        return currentUser
    }

    suspend fun createUserWithEmailAndPassword(userAuth: UserAuth): Result<FirebaseUser?> {
        return try {
            userAuth.exception()?.let { return Result.failure(it) }
            val user = auth.createUserWithEmailAndPassword(userAuth.email, userAuth.password).await().user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend  fun signInWithEmailAndPassword(userAuth: UserAuth): Result<FirebaseUser?> {
        return try {
            userAuth.exception()?.let { return Result.failure(it) }
            val user = auth.signInWithEmailAndPassword(userAuth.email,userAuth.password).await().user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
        _currentUser.postValue(auth.currentUser)
    }
}