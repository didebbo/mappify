package com.didebbo.mappify.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.domain.repository.MarkerPostRepository
import com.didebbo.mappify.domain.repository.UserDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userDetailRepository: UserDetailRepository
): ViewModel() {

    lateinit var userId: String
    var ownerUserDocument: UserDocument? = null

    private val _editingMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val editingMode: LiveData<Boolean> get() = _editingMode
    suspend fun fetchOwnerUserDocument(): Result<UserDocument> {
        return withContext(Dispatchers.IO) {
            userDetailRepository.getOwnerUserDocument()
        }
    }
    suspend fun fetchUserDocument(id: String): Result<UserDocument> {
        return withContext(Dispatchers.IO) {
            userDetailRepository.getUserDocument(id)
        }
    }

    private fun setEditingMode(value: Boolean? = null) {
        val toggleValue = !(editingMode.value ?: true)
        _editingMode.postValue(value ?: toggleValue)
    }

    suspend fun updateOwnerUser(description: String) {
        ownerUserDocument?.let { ownerUserDocument ->
            if(editingMode.value == true) {
                val newData = ownerUserDocument.copy(description = description)
                withContext(Dispatchers.IO) {
                    userDetailRepository.updateOwnerUserDocument(newData)
                }
            }
        }
        setEditingMode()
    }

    suspend fun fetchUserMarkerPosts(id: String): Result<List<MarkerPostDocument>> {
        return withContext(Dispatchers.IO) {
            userDetailRepository.getUserMarkerPosts(id)
        }
    }
}