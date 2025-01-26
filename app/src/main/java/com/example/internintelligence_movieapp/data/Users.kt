package com.example.internintelligence_movieapp.data

data class Users(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val imageUrl: String = "",
    val token:String? = null,
    val phone:String = ""
)
