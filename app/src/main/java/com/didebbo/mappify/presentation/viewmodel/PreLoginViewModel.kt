package com.didebbo.mappify.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PreLoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel() {

    fun getUser(): LiveData<FirebaseUser?> {
        return loginRepository.getUser()
    }

    suspend fun createUserWithEmailAndPassword(userAuth: UserAuth): Result<FirebaseUser?> {
        return loginRepository.createUserWithEmailAndPassword(userAuth)
    }

    suspend fun signInWithEmailAndPassword(userAuth: UserAuth): Result<FirebaseUser?> {
        return loginRepository.signInWithEmailAndPassword(userAuth)
    }

    fun signOut() {
        loginRepository.signOut()
    }
}