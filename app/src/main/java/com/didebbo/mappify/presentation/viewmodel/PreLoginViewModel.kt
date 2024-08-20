package com.didebbo.mappify.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didebbo.mappify.R
import com.didebbo.mappify.data.model.AvatarColor
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.data.model.UserDocument
import com.didebbo.mappify.domain.repository.PreLoginRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PreLoginViewModel @Inject constructor(
    private val preLoginRepository: PreLoginRepository
): ViewModel() {

    fun getUser(): LiveData<FirebaseUser?> {
        return preLoginRepository.getUser()
    }

    suspend fun getOwnerUserDocument(): Result<UserDocument> {
        return  preLoginRepository.getOwnerUserDocument()
    }

    suspend fun createUserWithEmailAndPassword(userAuth: UserAuth): Result<Unit?> {
        return preLoginRepository.createUserWithEmailAndPassword(userAuth)
    }

    suspend fun signInWithEmailAndPassword(userAuth: UserAuth): Result<Unit?> {
        return preLoginRepository.signInWithEmailAndPassword(userAuth)
    }

    fun signOut() {
        preLoginRepository.signOut()
    }

    suspend fun getAvatarColor(id: String): Result<AvatarColor> {
        return withContext(Dispatchers.IO) {
            preLoginRepository.getAvatarColor(id)
        }
    }
}