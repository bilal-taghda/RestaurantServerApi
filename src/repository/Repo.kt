package com.RestaurantServerApi.repository

import com.RestaurantServerApi.data.model.Order
import com.RestaurantServerApi.data.model.User
import com.RestaurantServerApi.data.table.OrderTable
import com.RestaurantServerApi.data.table.UserTable
import com.RestaurantServerApi.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*


class Repo {

    suspend fun addUser(user:User){
        dbQuery{
            UserTable.insert { ut->
                ut[UserTable.uid] = user.uid
                ut[UserTable.email] = user.email
                ut[UserTable.hashPassword] = user.hashPassword
                ut[UserTable.name] = user.userName
            }
        }
    }

    suspend fun findUserByEmail(email:String) = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    private fun rowToUser(row:ResultRow?):User?{
        if(row == null){
            return null
        }

        return User(
            email =  row[UserTable.email],
            uid =  row[UserTable.uid],
            hashPassword = row[UserTable.hashPassword],
            userName = row[UserTable.name]
        )
    }


//    ============== Orders ==============


    suspend fun addOrder(order:Order, email: String){
        dbQuery {

            OrderTable.insert { nt->
                nt[OrderTable.id] = order.id
                nt[OrderTable.userEmail] = email
                nt[OrderTable.orderTitle] = order.orderTitle
                nt[OrderTable.orderDescription] = order.orderDescription
                nt[OrderTable.orderDate] = order.orderDate
            }

        }

    }


    suspend fun getAllOrders(email:String):List<Order> = dbQuery {

        OrderTable
            .select { OrderTable.userEmail.eq(email) }
            .mapNotNull { rowToOrder(it) }

    }


    suspend fun updateOrders(order:Order, email: String){

        dbQuery {

            OrderTable.update(
                where = {
                    OrderTable.userEmail.eq(email) and OrderTable.id.eq(order.id)
                }
            ){ or->
                or[OrderTable.orderTitle] = order.orderTitle
                or[OrderTable.orderDescription] = order.orderDescription
                or[OrderTable.orderDate] = order.orderDate
            }

        }

    }

    suspend fun deleteOrder(id:String, email: String){
        dbQuery {
            OrderTable.deleteWhere { OrderTable.userEmail.eq(email) and OrderTable.id.eq(id) }
        }
    }


    private fun rowToOrder(row:ResultRow?): Order? {

        if(row == null){
            return null
        }

        return Order(
            id = row[OrderTable.id],
            orderTitle = row[OrderTable.orderTitle],
            orderDescription =  row[OrderTable.orderDescription],
            orderDate = row[OrderTable.orderDate]
        )

    }





}