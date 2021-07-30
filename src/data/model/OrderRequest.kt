package com.RestaurantServerApi.data.model

data class OrderRequest(
    val id:String,
    val orderTitle:List<String>,
    val orderDescription:String,
    val orderDate:Long
)