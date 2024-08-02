package com.didebbo.mappify.data.model

data class UserAuth(
    val name: String? = null,
    val surname: String? = null,
    val email: String,
    val password: String
) {
    private  fun isNameValid(): Boolean = name?.isNotEmpty() == true
    private  fun isSurnameValid(): Boolean = surname?.isNotEmpty() == true
    private fun isEmailValid(): Boolean = email.contains("@")
    private fun isPasswordValid(): Boolean = password.isNotEmpty()
    private fun isUserAuthValid(): Boolean = isEmailValid() && isPasswordValid()
    fun signInsException(): Exception? {
        if(!isEmailValid()) return Exception("Invalid Email")
        if(!isPasswordValid()) return Exception("Invalid Password")
        if(!isUserAuthValid()) return Exception("Invalid Authentication")
        return null
    }

    fun createUserException(): Exception? {
        if(!isNameValid()) return Exception("Invalid Name")
        if(!isSurnameValid()) return Exception("Invalid Surname")
        if(!isEmailValid()) return Exception("Invalid Email")
        if(!isPasswordValid()) return Exception("Invalid Password")
        if(!isUserAuthValid()) return Exception("Invalid Authentication")
        return null
    }
}
