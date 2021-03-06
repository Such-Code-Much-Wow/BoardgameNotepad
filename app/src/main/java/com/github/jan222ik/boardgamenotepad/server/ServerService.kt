package com.github.jan222ik.boardgamenotepad.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.github.jan222ik.boardgamenotepad.R
import com.github.jan222ik.boardgamenotepad.game.Game
import com.github.jan222ik.boardgamenotepad.game.GameHandler
import com.google.gson.Gson
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.request.receive
import io.ktor.request.receiveText
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import java.io.File
import java.util.concurrent.TimeUnit


class ServerService : Service() {

    lateinit var server: Server

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Thread(
            Runnable {
                server = Server()
                server.start(Helper(applicationContext))
            }).start()
    }

    override fun onDestroy() {
        server.stop()
    }
}

class Server {

    private var server: NettyApplicationEngine? = null

    fun start(helper: Helper) {
        server = embeddedServer(Netty, port = 8080) {

            routing {
                get("/") {
                    //TODO Serve Index Page
                    call.respondText(ContentType.Text.Html) { html() }
                    //call.respondText("Hello World!", ContentType.Text.Plain)
                }
                get("/playInput.js") {
                    //TODO Serve Index Page
                    call.respondText(ContentType.Text.JavaScript) { js() }
                    //call.respondText("Hello World!", ContentType.Text.Plain)
                }
                get("/fancypants.png") {
                    //TODO Serve Index Page
                    call.respondBytes(ContentType.Image.PNG) {
                        helper.LoadBytes(R.mipmap.fancypants)
                    }
                }
                //get(path = "/favicon.ico") {
                //TODO Serve Favicon
                // }
                get("/game/reset") {
                    GameHandler.reset()
                    call.respond(HttpStatusCode.OK, HttpStatusCode.OK.description)
                }
                get("/game/restart") {
                    GameHandler.restart()
                    call.respond(HttpStatusCode.OK, HttpStatusCode.OK.description)
                }
                get("/game/reveal") {
                    val reveal = Game.reveal()
                    if (reveal) {
                        call.respond(HttpStatusCode.OK, HttpStatusCode.OK.description)
                    } else {
                        call.respond(HttpStatusCode.Forbidden, HttpStatusCode.Forbidden.description)
                    }
                }
                // /game/join?realname=<name>
                get("/game/join") {
                    val realname = call.parameters["realname"]!!
                    val playerID = GameHandler.addPlayerToGame(realname)
                    call.respondText { playerID.toString() }
                }
                get("/game/endjoin") {
                    val endJoin = Game.endJoin()
                    if (endJoin) {
                        call.respond(HttpStatusCode.OK, HttpStatusCode.OK.description)
                    } else {
                        call.respond(HttpStatusCode.ExpectationFailed, HttpStatusCode.ExpectationFailed.description)
                    }
                }
                get("/game/players/{playerID}/moderator") {
                    val plID = call.parameters["playerID"]!!.toInt()
                    Game.pickModerator(Game.players.find{ it.playerId == plID }!!)
                    call.respond(HttpStatusCode.OK, HttpStatusCode.OK.description)
                }
                get("/game/players/status") {
                    call.respondText { Gson().toJson(Game.toShareAbleState()) }
                }
                get("/game/players/{playerID}/msg") {
                    //Get msg
                    val plID = call.parameters["playerID"]!!.toInt()
                    val msgs = Game.getMessagesForPlayer(Game.players.find{ it.playerId == plID }!!)
                    if (msgs != null) {
                        call.respondText { Gson().toJson(msgs) }
                    } else {
                        call.respond(HttpStatusCode.NoContent, HttpStatusCode.NoContent.description)
                    }
                }
                post("/game/players/{playerID}/msg") {
                    //Post msg
                    val text: String = call.receiveText()
                    if (text.isEmpty()) {
                        call.respond(HttpStatusCode.BadRequest, HttpStatusCode.BadRequest.description)
                    } else {
                        val plID = call.parameters["playerID"]!!.toInt()
                        Game.addMessage(Game.players.find{ it.playerId == plID }!!, text)
                        call.respond(HttpStatusCode.OK, HttpStatusCode.OK.description)
                    }
                }
            }
        }
        server!!.start(wait = false)
        /*
        listOf("Anton", "Dieter", "Anna").forEach {
            GameHandler.addPlayerToGame(it)
        }
        Game.endJoin()
        Game.pickModerator(Game.players.first { it.playerId == 1 })

         */
    }

    fun stop() {
        server?.stop(1000L, 1000L, TimeUnit.MILLISECONDS)
    }

    fun html(): String {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <title>Sexy Unicorns</title>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
                <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
                <script src="playInput.js"></script>
                <link rel="icon" href="icon.png">
                <style>
                    .wrapper {
                        width: 960px;
                        margin: 0 auto;
                    }

                    header {
                        width: 960px;
                    }

                    nav, section {
                        float: left;
                    }

                    nav {
                        width: 200px;
                        margin-right: 10px;
                    }

                    section {
                        width: 750px;
                    }

                    *, *:before, *:after {
                        -moz-box-sizing: border-box;
                        -webkit-box-sizing: border-box;
                        box-sizing: border-box;
                    }

                    body {
                        background: #2E2E2E;
                        color: #00fff7;
                        font-family: Helvetica;
                        text-align: center;
                        margin: 20px;
                    }

                    header,
                    nav,
                    section {
                        border: 1px solid rgba(255, 255, 255, 0.8);
                        margin-bottom: 10px;
                        border-radius: 3px;
                    }

                    header {
                        padding: 20px 0;
                    }

                    nav, section {
                        padding: 200px 0;
                    }

                    #playerInput {
                        height: 150px;
                        width: 90%;
                        font-size: 14pt;
                        background-color: #A4A4A4;
                        color: #00fff7;
                    }

                    input {
                        background-color: #A4A4A4;
                    }

                    #answerButton {
                        color: black;
                        width: 90%;
                        background-color: #DA81F5;
                        color: #00fff7;
                    }

                    #answerButton:hover {
                        background: white;
                        color: #00fff7;
                    }

                    #sponsorParagraph {
                        font-size: 10px;
                        color: #A4A4A4;
                    }
                </style>
            </head>
            <body onload="initialize()">
            <script>
                function initialize() {
                    document.getElementById("nameInput").addEventListener("keyup", function (event) {
                        if(event.code === "Enter"){
                            handlePlayerJoin();
                        }
                    });
                    setInterval(poll, 5000);
                }
            </script>
            <div id="joinDiv">
                <input type="text" placeholder="Name" id="nameInput"/><br><br>
                <input type="submit" onclick="handlePlayerJoin()" id="joinBtn" value="Join">
            </div>
            <div id="inputDiv" hidden="none">
                <h1>Game Input</h1><br>
                <form action="gameInputForm">
                    Use your brain and enter the best answer you could think of:<br><br>
                    <textarea id="playerInput"></textarea><br><br>
                    <input type="button" id="answerButton" onclick="handlePlayerInput()" value="Answer and cross your fingers">
                </form>
                <br>
            </div>
            <div id="resultDiv" hidden="none">
                <ul id="resultList"></ul>
                <input id="continueBtn" type="button" value="Continue" onclick="handleContinueGame()">
            </div>
            <div id="pendingDiv" hidden="none">
                <h1 id="pendingText"></h1>
            </div>
            <img src="fancypants.png" alt="unicorns" width="80%" height="80%"><br>

            <p id="sponsorParagraph">Sponsored by Unicorny</p>
            </body>
            </html>
        """.trimIndent()
    }

    fun js() = """
let gameState = "";
let currentState = "JOIN";
let playerId;
let exists = false;

function poll() {
    let request = new XMLHttpRequest();
    let url = "/game/players/status";
    request.onreadystatechange = async function () {
        await sleep(1000);
        gameState = JSON.parse(this.response).state;
        if (this.status === 200) {
            if (gameState && currentState !== gameState) {
                switch (gameState) {
                    case "JOIN":
                        showDiv("joinDiv");
                        break;
                    case "SELECT_MODERATOR":
                        selectMod();
                        break;
                    case "ENTER_TEXT":
                        showDiv("inputDiv");
                        break;
                    case "MODERATOR_PEEK":
                        getMessages(true);
                        break;
                    case "REVEAL":
                        getMessages(false);
                        break;

                }
                currentState = gameState;
            }
        }
    };

    request.open("GET", url);
    request.send();
}

async function sleep(time) {
    return new Promise((resolve => setInterval(resolve, time)));
}

function getMessages(btn) {
    let request = new XMLHttpRequest();
    let url = "game/players/" + playerId + "/msg";
    request.onreadystatechange = async function () {
        if (this.status === 200) {
            await sleep(1000);
            let list = JSON.parse(this.response);
            toggleContinueBtn(!btn);
            handleResult(list);
            showDiv("resultDiv");
        }
    };
    request.open("GET", url);
    request.send();

}

function handlePlayerInput() {
    let input = document.getElementById("playerInput").value;
    if (input && input.length > 0) {
        let request = new XMLHttpRequest();
        let url = "game/players/" + playerId + "/msg";
        request.open("POST", url);
        request.send(input);
        handlePending("Please Wait...");
        resetInputs();
    }
}

function selectMod() {
    let request = new XMLHttpRequest();
    let url = "game/players/" + playerId + "/moderator";
    request.open("GET", url);
    request.send();
}

function toggleContinueBtn(flag) {
    let btn = document.getElementById("continueBtn");
    btn.hidden = flag;
}

function resetInputs() {
    let htmlList = document.getElementById("resultList");
    while (htmlList.firstChild) {
        htmlList.removeChild(htmlList.firstChild);
    }
    toggleContinueBtn(true);
    exists = false;
}

function handleResult(listData) {
    if(!exists) {
        exists = true;
        let htmlList = document.getElementById("resultList");
        for (let i = 0; i < listData.length; ++i) {
            let name = listData[i].split("§")[0];
            let text = listData[i].split("§")[1];
            let listItem = document.createElement('li');
            listItem.innerHTML = name + ": " + text;
            htmlList.appendChild(listItem);
        }
        showDiv("resultDiv");
    }
}

function handlePlayerJoin() {
    let name = document.getElementById("nameInput").value;

    if (name && name.length > 0) {
        let request = new XMLHttpRequest();
        let url = "game/join?realname=" + name;
        request.onreadystatechange = function () {
            if (this.status === 200) {
                playerId = this.responseText;
                handlePending("Please Wait...");
            }
        };
        request.open("GET", url);
        request.send();
    } else {
        alert("GIVE ME A NAME PEASANT")
    }
}

function handlePending(value) {
    document.getElementById("pendingText").innerHTML = value;
    showDiv("pendingDiv");
}

function handleContinueGame() {
    toggleContinueBtn(true);
    let request = new XMLHttpRequest();
    let url = "game/reveal";
    request.open("GET", url);
    request.send();
}

function showDiv(div) {
    let divs = document.getElementsByTagName("div");
    for (let i = 0; i < divs.length; i++) {
        divs[i].hidden = true;
    }
    document.getElementById(div).hidden = false;
}
    """.trimIndent()


}