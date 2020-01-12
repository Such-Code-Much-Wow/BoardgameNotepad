package com.github.jan222ik.boardgamenotepad.game

object GameHandler {
    private var playerIdCounter = 0

    init {
        reset()
    }

    fun reset() {
        Game.reset()
    }

    fun restart() {
        Game.restart()
    }

    fun addPlayerToGame(realname: String): Int {
        return Game.addPlayer(realname, ++playerIdCounter)
    }
}
