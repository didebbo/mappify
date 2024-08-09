package com.didebbo.mappify.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.didebbo.mappify.data.model.MarkerPostDocument
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.domain.repository.MarkerPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val markerPostRepository: MarkerPostRepository
): ViewModel() {

    lateinit var userId: String
    suspend fun fetchUserDocument(id: String): Result<UserDocument> {
        return withContext(Dispatchers.IO) {
            markerPostRepository.getUserDocument(id)
        }
    }
}