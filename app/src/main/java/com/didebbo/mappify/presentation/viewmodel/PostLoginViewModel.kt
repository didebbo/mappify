package com.didebbo.mappify.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.Position
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.domain.repository.PostLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    var currentPosition: Position = Position.ROME
    val allCityPositions: List<Position> = Position.entries


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

    suspend fun getUserDocument(id: String): Result<UserDocument> {
        return withContext(Dispatchers.IO) {
            postLoginRepository.getUserDocument(id)
        }
    }
}