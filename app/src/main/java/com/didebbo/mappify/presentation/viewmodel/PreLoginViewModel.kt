package com.didebbo.mappify.presentation.viewmodel

import android.util.Log
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

    private var userAuth: UserAuth? = null

    fun getUser(): FirebaseUser? {
        return loginRepository.getUser()
    }

    fun createUserWithEmailAndPassword(userAuth: UserAuth) {
        if(userAuth.isUserAuthValid())
            loginRepository.createUserWithEmailAndPassword(userAuth)
    }

    suspend fun signInWithEmailAndPassword(userAuth: UserAuth): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            if(userAuth.isUserAuthValid()) loginRepository.signInWithEmailAndPassword(userAuth)
            else Result.failure(Exception("Invalid Credentials"))
        }
    }
}