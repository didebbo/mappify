package com.didebbo.mappify.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.didebbo.mappify.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreLoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel() {

    fun getUser(): FirebaseUser? {
        return loginRepository.getUser()
    }

    fun createUserWithEmailAndPassword(email: String, password: String) {
        loginRepository.createUserWithEmailAndPassword(email, password)
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) return
        loginRepository.signInWithEmailAndPassword(email, password)
    }
}