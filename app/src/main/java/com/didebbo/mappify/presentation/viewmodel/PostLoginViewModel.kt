package com.didebbo.mappify.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.domain.repository.PostLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class PostLoginViewModel @Inject constructor(
    private val postLoginRepository: PostLoginRepository
): ViewModel() {
    private val _editingMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val editingMode: LiveData<Boolean> get() = _editingMode

    fun setEditingMode() {
        editingMode.value?.let { editingMode ->
            _editingMode.postValue(!editingMode)
        }
    }

    suspend fun setMarkerPoint(geoPoint: GeoPoint): Result<MarkerPostDocument> {
        return withContext(Dispatchers.IO) {
            val userDocumentResult = postLoginRepository.getOwnerUserDocument()
            userDocumentResult.exceptionOrNull()?.let {
                return@withContext Result.failure(it)
            }
            userDocumentResult.getOrNull()?.let { userDocument ->
                val markerPostDocument = MarkerPostDocument(position = MarkerPostDocument.GeoPoint(geoPoint.latitude,geoPoint.longitude), ownerId = userDocument.id)
                val markerPostDocumentResult = postLoginRepository.addMarkerPostDocument(markerPostDocument)
                markerPostDocumentResult.exceptionOrNull()?.let {
                    return@withContext Result.failure(it)
                }
                markerPostDocumentResult.getOrNull()?.let { markerDocument ->
                    val markerPostsIds = userDocument.markerPostsIds.toMutableList()
                    markerPostsIds.add(markerDocument.id)
                    val updatedUserDocument = userDocument.copy(markerPostsIds = markerPostsIds)
                    val updatedUserDocumentResult = postLoginRepository.updateUserDocument(updatedUserDocument)
                    updatedUserDocumentResult.exceptionOrNull()?.let {
                        return@withContext Result.failure(it)
                    }
                    updatedUserDocumentResult.getOrNull()?.let {
                        return@withContext Result.success(markerDocument)
                    }
                }
            }
            Result.failure(Exception("1 UserDocument not found"))
        }
    }
}