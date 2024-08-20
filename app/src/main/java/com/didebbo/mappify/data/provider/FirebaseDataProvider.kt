package com.didebbo.mappify.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.didebbo.mappify.data.model.AvatarColor
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
    private val userCollection = fireStore.collection("user_collection")
    private val markerPostCollection = fireStore.collection("marker_collection")
    private val avatarColorCollection = fireStore.collection("avatar_color_collection")

    private val _currentUser: MutableLiveData<FirebaseUser?> = MutableLiveData(auth.currentUser)

    fun getUserAuth(): LiveData<FirebaseUser?> {
        return _currentUser
    }

    suspend fun fetchMarkerPostDocuments(): Result<List<MarkerPostDocument>> {
        return try {
            val markerPostDocuments = markerPostCollection.get().await().documents.mapNotNull { it.toObject(MarkerPostDocument::class.java) }
            Result.success(markerPostDocuments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchUserDocuments(): Result<List<UserDocument>> {
        return try {
            val userDocuments = userCollection.get().await().documents.mapNotNull { it.toObject(UserDocument::class.java) }
            Result.success(userDocuments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUserWithEmailAndPassword(userAuth: UserAuth): Result<Unit> {
        return try {
            userAuth.registerException()?.let { throw it }
            auth.createUserWithEmailAndPassword(userAuth.email, userAuth.password).await()
            val userDocument = addUserDocument(userAuth)
            userDocument.exceptionOrNull()?.let { throw it }
            _currentUser.postValue(auth.currentUser)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend  fun signInWithEmailAndPassword(userAuth: UserAuth): Result<Unit> {
        return try {
            userAuth.loginException()?.let { return Result.failure(it) }
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
            throw Exception("getOwnerUserDocument() userDocument not fount")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun addUserDocument(userAuth: UserAuth): Result<UserDocument> {
        return try {
            val userDocument = userCollection.document()
            val randomColor = getRandomAvatarColor()
            randomColor.exceptionOrNull()?.let { throw it }
            randomColor.getOrNull()?.let { safeAvatarColor ->
                val data = UserDocument(id = userDocument.id, name = userAuth.name ?: "", surname = userAuth.surname ?: "", email = userAuth.email, avatarColorId = safeAvatarColor.id)
                userDocument.set(data).await()
                userDocument.get().await().toObject(UserDocument::class.java)?.let {
                    Result.success(it)
                } ?:
                throw Exception("addUserDocument() userDocument is null")
            } ?: throw Exception("addUserDocument() randomColor is null")
        } catch(e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserDocument(id: String): Result<UserDocument> {
        return try {
            val userDocument = userCollection.document(id).get().await().toObject(UserDocument::class.java)
            userDocument?.let { Result.success(it) } ?:
            throw Exception("getUserDocument() userDocument not fount")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserMarkerPosts(id: String): Result<List<MarkerPostDocument>> {
        return try {
            val documents = markerPostCollection.whereEqualTo("ownerId",id).get().await().toObjects(MarkerPostDocument::class.java)
            Result.success(documents)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMarkerPostDocument(id: String): Result<MarkerPostDocument> {
        return try {
            val document = markerPostCollection.document(id).get().await().toObject(MarkerPostDocument::class.java)
            document?.let { Result.success(it) } ?:
            throw Exception("getMarkerPostDocument() markerDocument not found")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addMarkerPostDocument(markerPostDocument: MarkerPostDocument): Result<MarkerPostDocument> {
        return try {
            val reference = markerPostCollection.document()
            val data = markerPostDocument.copy(id = reference.id)
            reference.set(data).await()
            val addedMarkerPostDocument = reference.get().await().toObject(MarkerPostDocument::class.java)
            addedMarkerPostDocument?.let { Result.success(it) } ?:
            throw Exception("addMarkerPostDocument() markerPostDocument not found")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateOwnerUserDocument(data: UserDocument): Result<UserDocument> {
        return try {
            val reference = userCollection.whereEqualTo("email",getUserAuth().value?.email).get().await().documents.firstOrNull()?.reference
            reference?.let {
                it.set(data).await()
                val ownerUserDocument = it.get().await().toObject(UserDocument::class.java)
                ownerUserDocument?.let { userDocument ->
                    Result.success(userDocument)
                } ?:
                throw Exception("updateUserDocument() ownerUserDocument not found")
            } ?:
            throw Exception("updateUserDocument() userDocument not found")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun addAvatarColors(): Result<List<AvatarColor>> {
        return try {
            val colors: MutableList<AvatarColor> = mutableListOf()
            AvatarColor.AvatarColors.entries.forEach {
                val ref = avatarColorCollection.document(it.avatarColor.id)
                val exist = ref.get().await().toObject(AvatarColor::class.java)
                exist?.let { safeColor ->
                    colors.add(safeColor)
                } ?: ref.set(it.avatarColor).await()
                ref.get().await().toObject(AvatarColor::class.java)?.let { safeColor ->
                    colors.add(safeColor)
                }
            }
            Result.success(colors)
        } catch(e: Exception) {
            return Result.failure(e)
        }
    }
    private suspend fun getRandomAvatarColor(): Result<AvatarColor> {
        return try {
            val ref = avatarColorCollection.get().await()
            val colors = addAvatarColors()
            colors.exceptionOrNull()?.let { throw Exception(it) }
            colors.getOrNull()?.let { safeColors ->
                val color = safeColors.randomOrNull()
                color?.let { Result.success(it) } ?:
                throw Exception("getRandomColor() AvatarColor not found")
            } ?:
            throw Exception("getRandomColor() AvatarColors not found")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAvatarColor(id: String): Result<AvatarColor> {
        return try {
            avatarColorCollection.document(id).get().await().toObject(AvatarColor::class.java)?.let {
                Result.success(it)
            } ?: throw Exception("getAvatarColor(id) avatarColor not found")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}