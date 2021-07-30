package com.RestaurantServerApi

import com.RestaurantServerApi.authentication.JwtService
import com.RestaurantServerApi.authentication.hash
import com.RestaurantServerApi.repository.DatabaseFactory
import com.RestaurantServerApi.repository.Repo
import com.RestaurantServerApi.routes.OrderRoutes
import com.RestaurantServerApi.routes.UserRoutes
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.sessions.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.locations.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    DatabaseFactory.init()
    val db = Repo()
    val jwtService = JwtService()
    val hashFunction = { s:String -> hash(s) }

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    install(Authentication) {

        jwt("jwt") {

            verifier(jwtService.varifier)
            realm = "Order Server"
            validate {
                val payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = db.findUserByEmail(email)
                user
            }

        }

    }

    install(ContentNegotiation) {
        gson {
        }
    }
    install(Locations)
    routing {

        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        UserRoutes(db,jwtService,hashFunction)
        OrderRoutes(db, hashFunction)

        route("/orders"){

            route("/create") {
                // localhost:8081/notes/create
                post {
                    val body = call.receive<String>()
                    call.respond(body)
                }
            }

            delete{
                val body = call.receive<String>()
                call.respond(body)
            }
        }


    }
}

data class MySession(val count: Int = 0)

