package com.didebbo.mappify.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didebbo.mappify.data.model.MarkerPostDocument
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

    enum class Position(var geoPoint: IGeoPoint) {
        ROME(GeoPoint(41.9028, 12.4964)),
        MILAN(GeoPoint(45.4642, 9.1900)),
        NAPLES(GeoPoint(40.8518, 14.2681)),
        TURIN(GeoPoint(45.0703, 7.6869)),
        PALERMO(GeoPoint(38.1157, 13.3615)),
        GENOA(GeoPoint(44.4056, 8.9463)),
        BOLOGNA(GeoPoint(44.4949, 11.3426)),
        FLORENCE(GeoPoint(43.7696, 11.2558)),
        BARI(GeoPoint(41.1171, 16.8719)),
        CATANIA(GeoPoint(37.5079, 15.0830));
    }

    var pointerPosition: Position = Position.BARI


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