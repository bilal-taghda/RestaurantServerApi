package com.RestaurantServerApi.routes

import com.RestaurantServerApi.data.model.Order
import com.RestaurantServerApi.data.model.OrderRequest
import com.RestaurantServerApi.data.model.SimpleResponse
import com.RestaurantServerApi.data.model.User
import com.RestaurantServerApi.repository.Repo
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*
import kotlin.collections.ArrayList

const val ORDERS = "$API_VERSION/orders"
const val CREATE_ORDERS = "$ORDERS/create"
const val UPDATE_ORDERS = "$ORDERS/update"
const val DELETE_ORDERS = "$ORDERS/delete"

@Location(CREATE_ORDERS)
class OrderCreateRoute

@Location(ORDERS)
class OrderGetRoute

@Location(UPDATE_ORDERS)
class OrderUpdateRoute

@Location(DELETE_ORDERS)
class OrderDeleteRoute




fun Route.OrderRoutes(
    db:Repo,
    hashFunction: (String)->String
) {

    authenticate("jwt"){

        post<OrderCreateRoute>{

            val or = try {
                call.receive<OrderRequest>()
            } catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Missing Fields"))
                return@post
            }

            try {

                val listOf_orderTitle: String =
                    Arrays.toString(or.orderTitle.toTypedArray())
                    .replace("[", "")
                    .replace("]", "")
                val email = call.principal<User>()!!.email
                val note = Order(or.id,listOf_orderTitle, or.orderDescription,or.orderDate)
                db.addOrder(note,email)
                call.respond(HttpStatusCode.OK,SimpleResponse(true,"Order Added Successfully!"))

            } catch (e:Exception){

                call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message ?: "Some Problem Occurred!"))
            }

        }


        var listOrderRequest : List<OrderRequest>
        get<OrderGetRoute>{

            listOrderRequest = ArrayList()
            try {
                var listOrders = ArrayList<String>()
                val email = call.principal<User>()!!.email
                val orders = db.getAllOrders(email)
                orders.forEach {
                    it.orderTitle
                        .split(",")
                        .toTypedArray()
                        .forEach { listOrders.add(it) }
                    (listOrderRequest as ArrayList<OrderRequest>)
                        .add(OrderRequest(it.id, listOrders, it.orderDescription, it.orderDate))
                }
                call.respond(HttpStatusCode.OK,listOrderRequest)
            } catch (e:Exception){

                call.respond(HttpStatusCode.Conflict, emptyList<Order>())
            }
        }



        put <OrderUpdateRoute> {

            val nr = try {
                call.receive<OrderRequest>()
            } catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Missing Fields"))
                return@put
            }

            try {

                val listOf_orderTitle: String =
                    Arrays.toString(nr.orderTitle.toTypedArray())
                        .replace("[", "")
                        .replace("]", "")
                val email = call.principal<User>()!!.email
                val order = Order(nr.id,listOf_orderTitle, nr.orderDescription,nr.orderDate)
                db.updateOrders(order,email)
                call.respond(HttpStatusCode.OK,SimpleResponse(true,"Order updated Successfully!"))

            } catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message ?: "Some Problem Occurred!"))
            }

        }


        delete<OrderDeleteRoute> {

            val orderId = try{
                call.request.queryParameters["id"]!!
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"QueryParameter:id is not present"))
                return@delete
            }


            try {

                val email = call.principal<User>()!!.email
                db.deleteOrder(orderId,email)
                call.respond(HttpStatusCode.OK,SimpleResponse(true,"Order Deleted Successfully!"))

            } catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false, e.message ?: "Some problem Occurred!"))
            }

        }

















    }

}