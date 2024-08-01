package com.didebbo.mappify.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.didebbo.mappify.data.model.UserAuth
import com.didebbo.mappify.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun signInWithEmailAndPassword(userAuth: UserAuth) {
        if(userAuth.isUserAuthValid())
            loginRepository.signInWithEmailAndPassword(userAuth)
    }
}