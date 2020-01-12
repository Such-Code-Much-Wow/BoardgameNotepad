package com.github.jan222ik.boardgamenotepad

import com.github.jan222ik.boardgamenotepad.game.Game
import com.github.jan222ik.boardgamenotepad.server.Server
//import io.restassured.RestAssured.given
import org.junit.Test

/*

class RestApiTest {


    @Test
    fun testRestLoop() {
        Server().start()
        val base = "http://localhost:8080"
        given().get("$base/game/reset").then().statusCode(200)
        mapOf(Pair("Name0", 0), Pair("Name1", 1), Pair("Name2", 2), Pair("Name3", 3)).forEach {
            val res = given().get("$base/game/join?realname=${it.key}")
            println(res.body.prettyPrint())
            res.then().statusCode(200)
        }
    }
}
 */