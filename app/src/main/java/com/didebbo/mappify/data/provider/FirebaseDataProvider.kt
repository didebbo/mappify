package com.didebbo.mappify.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.data.model.UserDocument
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await


class FirebaseDataProvider {
    private val auth: FirebaseAuth = Firebase.auth
    private val fireStore = Firebase.firestore
    private val userCollection = fireStore.collection("users")
    private val markerPosCollection = fireStore.collection("markerPosts")

    private val _currentUser: MutableLiveData<FirebaseUser?> = MutableLiveData(auth.currentUser)

    fun getUserAuth(): LiveData<FirebaseUser?> {
        return _currentUser
    }

    suspend fun createUserWithEmailAndPassword(userAuth: UserAuth): Result<Unit> {
        return try {
            userAuth.exception()?.let { return Result.failure(it) }
            auth.createUserWithEmailAndPassword(userAuth.email, userAuth.password).await().user
            val reference = userCollection.document()
            val data = UserDocument(reference.id,userAuth.name ?: "", userAuth.surname ?: "", userAuth.email)
            reference.set(data).await()
            _currentUser.postValue(auth.currentUser)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend  fun signInWithEmailAndPassword(userAuth: UserAuth): Result<Unit> {
        return try {
            userAuth.exception()?.let { return Result.failure(it) }
            auth.signInWithEmailAndPassword(userAuth.email,userAuth.password).await()
            _currentUser.postValue(auth.currentUser)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
        _currentUser.postValue(auth.currentUser)
    }

    suspend fun getOwnerUserDocument(): Result<UserDocument> {
        return try {
            val email = getUserAuth().value?.email
            val userDocument = userCollection.whereEqualTo("email",email).get().await().documents.firstOrNull()?.toObject(UserDocument::class.java)
            userDocument?.let { Result.success(it) } ?:
            Result.failure(Exception("getOwnerUserDocument() UserDocument not fount"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserDocument(id: String): Result<UserDocument> {
        return try {
            val userDocument = userCollection.document(id).get().await().toObject(UserDocument::class.java)
            userDocument?.let { Result.success(it) } ?:
            Result.failure(Exception("getUserDocument() UserDocument not fount"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMarkerPostDocument(id: String): Result<MarkerPostDocument> {
        return try {
            val document = markerPosCollection.document(id).get().await().toObject(MarkerPostDocument::class.java)
            document?.let { Result.success(it) } ?:
            Result.failure(Exception("getMarkerPostDocument() MarkerDocument not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addMarkerPostDocument(markerPostDocument: MarkerPostDocument): Result<MarkerPostDocument> {
        return try {
            val reference = markerPosCollection.document()
            val data = markerPostDocument.copy(id = reference.id)
            reference.set(data).await()
            val markerPostDocument = reference.get().await().toObject(MarkerPostDocument::class.java)
            markerPostDocument?.let { Result.success(it) } ?:
            Result.failure(Exception("addMarkerPostDocument() MarkerPostDocument not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserDocument(userDocument: UserDocument): Result<Unit> {
        return try {
            val reference = userCollection.whereEqualTo("email",getUserAuth().value?.email).get().await().documents.firstOrNull()?.reference
            reference?.let {
                it.set(userDocument).await()
                Result.success(Unit)
            } ?:
            Result.failure(Exception("updateUserDocument() MarkerDocument not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}