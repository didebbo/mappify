package com.didebbo.mappify.data.model

import java.util.UUID

data class UserDocument(
    val name: String,
    val surname: String,
    val email: String
) {

    val id: String = UUID.randomUUID().toString()
    val avatarName: String = "${name.first().uppercase()}${surname.first().uppercase()}"
    var description: String? = null
    val markerDocuments: MutableList<MarkerDocument> = mutableListOf()

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
    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun addMarkerPost(newMarkerDocument: MarkerDocument) {
        markerDocuments.add(newMarkerDocument)
    }

    fun deleteMarkerPost(oldMarkerDocument: MarkerDocument) {
        markerDocuments.remove(oldMarkerDocument)
    }
}

