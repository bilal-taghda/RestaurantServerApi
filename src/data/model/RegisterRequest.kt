package com.RestaurantServerApi.data.model

import java.util.*

data class RegisterRequest(
    val email:String,
    val name:String,
    val password:String
)
