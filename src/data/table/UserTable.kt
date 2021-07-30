package com.RestaurantServerApi.data.table

import org.jetbrains.exposed.sql.Table
import java.util.*


object UserTable:Table() {

    val uid = varchar("uid",512)
    val email = varchar("email",512)
    val name = varchar("name",512)
    val hashPassword = varchar("hashPassword",512)

    override val primaryKey: PrimaryKey = PrimaryKey(email)
}