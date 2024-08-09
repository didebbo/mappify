package com.didebbo.mappify.domain.repository

import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.data.provider.FirebaseDataProvider
import javax.inject.Inject


class MarkerPostRepository @Inject constructor(
 private val firebaseDataProvider: FirebaseDataProvider
) {
    private suspend fun getOwnerUserDocument(): Result<UserDocument> {
        return firebaseDataProvider.getOwnerUserDocument()
    }
    suspend fun getUserDocument(id: String): Result<UserDocument> {
        return firebaseDataProvider.getUserDocument(id)
    }

    suspend fun addMarkerPostDocument(markerPostDocument: MarkerPostDocument): Result<MarkerPostDocument> {

        markerPostDocument.newMarkerPostException()?.let {
            return Result.failure(it)
        }

        val userDocumentResult = getOwnerUserDocument()
        userDocumentResult.exceptionOrNull()?.let {
            return Result.failure(it)
        }
        userDocumentResult.getOrNull()?.let { userDocument ->
            val newMarkerPostDocument = markerPostDocument.copy(ownerId = userDocument.id)
            val markerPostResult = firebaseDataProvider.addMarkerPostDocument(newMarkerPostDocument)
            markerPostResult.exceptionOrNull()?.let {
                return Result.failure(it)
            }
            markerPostResult.getOrNull()?.let { markerPostDocument ->
                val markerPostsIds = userDocument.markerPostsIds.toMutableList()
                markerPostsIds.add(markerPostDocument.id)
                val updatedUserDocument = userDocument.copy(markerPostsIds = markerPostsIds)
                val updatedUserDocumentResult = updateUserDocument(updatedUserDocument)
                updatedUserDocumentResult.exceptionOrNull()?.let {
                    return Result.failure(it)
                }
                updatedUserDocumentResult.getOrNull()?.let {
                    return Result.success(markerPostDocument)
                }
            }
            return Result.failure(Exception("addMarkerPostDocument() markerPostDocument Not Found"))
        }
        return Result.failure(Exception("addMarkerPostDocument() userDocument Not Found"))
    }

    private suspend fun updateUserDocument(userDocument: UserDocument): Result<UserDocument> {
        return firebaseDataProvider.updateOwnerUserDocument(userDocument)
    }
}