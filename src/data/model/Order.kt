package com.RestaurantServerApi.data.model

data class Order(
    val id:String,
    val orderTitle:String,
    val orderDescription:String,
    val orderDate:Long
)