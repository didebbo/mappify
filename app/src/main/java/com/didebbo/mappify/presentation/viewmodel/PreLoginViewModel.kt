package com.didebbo.mappify.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.didebbo.mappify.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreLoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel() {

    fun createUserWithEmailAndPassword(email: String, password: String) {
        Log.i("GN","createUserWithEmailAndPassword")
        loginRepository.createUserWithEmailAndPassword(email, password)
    }
}