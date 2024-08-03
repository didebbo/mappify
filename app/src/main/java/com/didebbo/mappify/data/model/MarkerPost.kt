package com.didebbo.mappify.data.model

import java.util.UUID

data class MarkerPost(
    val title: String,
    val description: String,
    // val position: TODO,
    val owner: UserDocument
) {
    val id: String = UUID.randomUUID().toString()
}