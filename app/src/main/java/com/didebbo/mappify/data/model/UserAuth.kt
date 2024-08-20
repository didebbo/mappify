package com.didebbo.mappify.data.model

import com.didebbo.mappify.R

data class UserAuth(
    val name: String? = null,
    val surname: String? = null,
    val email: String,
    val password: String
) {

    private fun hasValidName(): Boolean {
        return name?.isNotEmpty() ?: false
    }

    private fun hasValidSurname(): Boolean {
        return surname?.isNotEmpty() ?: false
    }
    private fun hasValidEmail(): Boolean {
        return email.contains("@")
    }
    private fun hasValidPassword(): Boolean {
        return password.isNotEmpty()
    }

    fun registerException(): Exception? {
        if(!hasValidName()) return Exception("Invalid Name")
        if(!hasValidSurname()) return Exception("Invalid Surname")
        if(!hasValidEmail()) return Exception("Invalid Email")
        if(!hasValidPassword()) return Exception("Invalid Password")
        return null
    }
    fun loginException(): Exception? {
        if(!hasValidEmail()) return Exception("Invalid Email")
        if(!hasValidPassword()) return Exception("Invalid Password")
        return null
    }
}
