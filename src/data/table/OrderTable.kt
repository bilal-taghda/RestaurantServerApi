package com.RestaurantServerApi.data.table

import org.jetbrains.exposed.sql.Table

object OrderTable:Table() {

    val id = varchar("id",512)
    val userEmail = varchar("userEmail",512).references(UserTable.email)
    val orderTitle = varchar("orderTitle",500)
    val orderDescription = text("orderDescription")
    val orderDate = long("orderDate")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}