package com.RestaurantServerApi.data.model

import io.ktor.auth.*

data class User(
    val email:String,
    val uid:String,
    val hashPassword:String,
    val userName:String
):Principal