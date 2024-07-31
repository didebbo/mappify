package com.example.mappify.data.model

data class User(
    private val id: String,
    private val name: String,
    private val surname: String,
    private val avatarName: String,
    private val description: String,
    private val markerPosts: ArrayList<MarkerPost>
)

