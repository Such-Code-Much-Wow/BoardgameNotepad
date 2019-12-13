package com.github.jan222ik.boardgamenotepad.game

object GameHandler {
    private var playerIdCounter = 0

    init {
        reset()
    }

    fun reset() {
        Game.reset()
    }

    fun addPlayerToGame(realname: String): Player {
        val player = Player(++playerIdCounter, realname)
        //Game.addPlayer(player)
        return player
    }
}
