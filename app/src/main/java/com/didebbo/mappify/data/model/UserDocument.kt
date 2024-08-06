package com.didebbo.mappify.data.model

import java.util.UUID
data class UserDocument(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "Undefined Name",
    val surname: String = "Undefined Surname",
    val email: String = "Undefined Email",
    val description: String = "",
    val markerPostsIds: List<String> = listOf()
) {

    fun getAvatarName(): String = "${name.first().uppercase()}${surname.first().uppercase()}"
    fun getFullName(): String = "$name $surname"
    private fun hasValidName(): Boolean {
        return  name.isNotEmpty()
    }

    private fun hasValidSurname(): Boolean {
        return surname.isNotEmpty()
    }

    private fun  hasValidEmail(): Boolean {
        return email.contains("@")
    }
    fun exception(): Exception? {
        if(!hasValidName())  return Exception("Invalid Name")
        if(!hasValidSurname())  return Exception("Invalid Surname")
        if(!hasValidEmail())  return Exception("Invalid Email")
        return null
    }
}