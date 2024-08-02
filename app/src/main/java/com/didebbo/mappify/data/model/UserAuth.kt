package com.didebbo.mappify.data.model

data class UserAuth(
    val email: String,
    val password: String
) {
    private fun isEmailValid(): Boolean = email.contains("@")
    private fun isPasswordValid(): Boolean = password.isNotEmpty()
    private fun isUserAuthValid(): Boolean = isEmailValid() && isPasswordValid()
    fun exception(): Exception? {
        if(!isEmailValid()) return Exception("Invalid Email")
        if(!isPasswordValid()) return Exception("Invalid Password")
        if(!isUserAuthValid()) return Exception("Invalid Authentication")
        return null
    }
}
