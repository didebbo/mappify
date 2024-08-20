package com.didebbo.mappify.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didebbo.mappify.data.model.AvatarColor
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.Position
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.domain.repository.PostLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class PostLoginViewModel @Inject constructor(
    private val postLoginRepository: PostLoginRepository
): ViewModel() {

    private val _editingMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val editingMode: LiveData<Boolean> get() = _editingMode

    private val _markerPostDocuments: MutableLiveData<List<MarkerPostDocument>> = MutableLiveData(
        listOf()
    )
    val markerPostDocuments: LiveData<List<MarkerPostDocument>> get() = _markerPostDocuments

    var ownerUserDocument: UserDocument? = null
    var availablePositions: MutableList<Position> = Position.City.entries.map { it.position }.toMutableList()
    var currentPosition: Position = availablePositions.first()
    var currentGeoPoint: IGeoPoint = currentPosition.geoPoint


    fun setEditingMode(value: Boolean? = null) {
        editingMode.value?.let { editingMode ->
            _editingMode.postValue(value ?: !editingMode)
        }
    }

    suspend fun fetchMarkerPostDocuments(): Result<List<MarkerPostDocument>> {
        return withContext(Dispatchers.IO) {
            val markerPostDocumentsResult = postLoginRepository.fetchMarkerPostDocuments()
            markerPostDocumentsResult.exceptionOrNull()?.let {
                return@withContext Result.failure(it)
            }
            markerPostDocumentsResult.getOrNull()?.let {
                _markerPostDocuments.postValue(it)
                return@withContext Result.success(it)
            }
            return@withContext Result.failure(Exception("fetchMarkerPostDocuments() markerPostDocuments Not Found"))
        }
    }

    suspend fun fetchUserDocuments(): Result<List<UserDocument>> {
        return withContext(Dispatchers.IO) {
            val userDocumentsResult = postLoginRepository.fetchUserDocuments()
            userDocumentsResult.exceptionOrNull()?.let {
                return@withContext Result.failure(it)
            }
            userDocumentsResult.getOrNull()?.let {
                return@withContext Result.success(it)
            }
            return@withContext Result.failure(Exception("fetchUserDocuments() userDocuments Not Found"))
        }
    }

    suspend fun getOwnerUserDocument(): Result<UserDocument> {
        return withContext(Dispatchers.IO) {
            val userDocumentResult = postLoginRepository.getOwnerUserDocument()
            userDocumentResult.getOrNull()?.let { uD -> ownerUserDocument = uD }
            userDocumentResult
        }
    }

    suspend fun getUserDocument(id: String): Result<UserDocument> {
        return withContext(Dispatchers.IO) {
            postLoginRepository.getUserDocument(id)
        }
    }

    suspend fun getAvatarColor(id: String): Result<AvatarColor> {
        return withContext(Dispatchers.IO) {
            postLoginRepository.getAvatarColor(id)
        }
    }
}