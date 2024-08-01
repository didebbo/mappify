package com.didebbo.mappify.data.model

data class UserAuth(
    val email: String,
    val password: String
) {
    fun isEmailValid(): Boolean = email.contains("@")
    fun isPasswordValid(): Boolean = password.isNotEmpty()
    fun isUserAuthValid(): Boolean = isEmailValid() && isPasswordValid()
}
