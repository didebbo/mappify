package com.didebbo.mappify.data.model

data class UserAuth(
    val name: String? = null,
    val surname: String? = null,
    val email: String,
    val password: String
) {
    private fun hasValidEmail(): Boolean {
        return email.contains("@")
    }
    private fun hasValidPassword(): Boolean {
        return password.isNotEmpty()
    }
    fun exception(): Exception? {
        if(!hasValidEmail()) return Exception("Invalid Email")
        if(!hasValidPassword()) return Exception("Invalid Password")
        return null
    }
}
