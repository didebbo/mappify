package com.didebbo.mappify.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.domain.repository.PreLoginRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreLoginViewModel @Inject constructor(
    private val preLoginRepository: PreLoginRepository
): ViewModel() {

    fun getUser(): LiveData<FirebaseUser?> {
        return preLoginRepository.getUser()
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
}