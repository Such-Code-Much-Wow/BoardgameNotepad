package com.github.jan222ik.boardgamenotepad.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.github.jan222ik.boardgamenotepad.game.GameHandler
import com.google.gson.Gson
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import java.util.concurrent.TimeUnit


class ServerService : Service() {

    lateinit var server: Server

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        server = Server()
        server.start()
    }

    override fun onDestroy() {
        server.stop()
    }
}

class Server {

    private lateinit var server: NettyApplicationEngine

    fun start() {
        server = embeddedServer(Netty, port = 8080) {
            routing {
                get("/") {
                    call.respondText("Hello World!", ContentType.Text.Plain)
                }
                get("/round/new") {
                    RestApi.startNewRound()
                    call.respond(HttpStatusCode.OK, HttpStatusCode.OK.description)
                }
                get("/games/{id}/join") {
                    val realname = call.receive<Parameters>()["realname"]!!
                    val id = call.parameters["id"]!!.toInt()
                    if (id < 1) call.respond(HttpStatusCode.BadRequest)
                    val playerID = GameHandler.addPlayerToGame(GameHandler.getGameByID(id), realname)
                    call.respondRedirect("/games/$id/players/$playerID")
                }
                get("/games/{id}/join") {
                    val realname = call.receive<Parameters>()["realname"]!!
                    val id = call.parameters["id"]!!.toInt()
                    if (id < 1) call.respond(HttpStatusCode.BadRequest)
                    GameHandler.addPlayerToGame(GameHandler.getGameByID(id), realname)
                    call.respond(HttpStatusCode.OK, HttpStatusCode.OK.description)
                }
                get("/games/{id}/status") {
                    val id = call.parameters["id"]!!.toInt()
                    if (id < 1) call.respond(HttpStatusCode.BadRequest)
                    val gson = Gson()
                    call.respondText(
                        gson.toJson(GameHandler.getGameByID(id)),
                        ContentType.Application.Json,
                        HttpStatusCode.OK
                    )
                }
            }
        }
        server.start(wait = false)
    }

    fun stop() {
        server.stop(1000L, 1000L, TimeUnit.MILLISECONDS)
    }
}

object RestApi {

    fun startNewRound() {
        GameHandler.newGame()
    }


}