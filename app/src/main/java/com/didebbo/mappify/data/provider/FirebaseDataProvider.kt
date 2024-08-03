package com.didebbo.mappify.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.data.model.UserDocument
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


class FirebaseDataProvider {
    private val auth: FirebaseAuth = Firebase.auth
    private val fireStore = Firebase.firestore
    private val userCollection = fireStore.collection("users")

    private val _currentUser: MutableLiveData<FirebaseUser?> = MutableLiveData(auth.currentUser)

    fun getUser(): LiveData<FirebaseUser?> {
        return _currentUser
    }

    suspend fun createUserWithEmailAndPassword(userAuth: UserAuth): Result<FirebaseUser?> {
        return try {
            userAuth.exception()?.let { return Result.failure(it) }
            val firebaseUser = auth.createUserWithEmailAndPassword(userAuth.email, userAuth.password).await().user
            val userDocument = UserDocument(userAuth.name ?: "", userAuth.surname ?: "", userAuth.email)
            userDocument.exception()?.let { return  Result.failure(it)}
            userCollection.add(userDocument)
            _currentUser.postValue(auth.currentUser)
            Result.success(firebaseUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend  fun signInWithEmailAndPassword(userAuth: UserAuth): Result<FirebaseUser?> {
        return try {
            userAuth.exception()?.let { return Result.failure(it) }
            val fireBaseUser = auth.signInWithEmailAndPassword(userAuth.email,userAuth.password).await().user
            _currentUser.postValue(auth.currentUser)
            Result.success(fireBaseUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
        _currentUser.postValue(auth.currentUser)
    }
}