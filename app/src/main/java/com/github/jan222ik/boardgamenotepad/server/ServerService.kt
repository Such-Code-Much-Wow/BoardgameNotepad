package com.github.jan222ik.boardgamenotepad.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.github.jan222ik.boardgamenotepad.utils.Utils
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


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
    fun start() {
        val server = embeddedServer(Netty, port = 8080) {
            routing {
                get("/") {
                    call.respondText("Hello World!", ContentType.Text.Plain)
                }
                get("/demo") {
                    call.respondText(
                        "HELLO WORLD! V4: ${Utils.getIPAddress(true)} <br/> V6: ${Utils.getIPAddress(
                            false
                        )}"
                    )
                }
            }
        }
        server.start(wait = false)
    }

    fun stop() {

    }
}