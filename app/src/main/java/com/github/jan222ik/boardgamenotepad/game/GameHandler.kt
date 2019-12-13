package com.github.jan222ik.boardgamenotepad.game

object GameHandler {
    private var gameCounter = 0
    private var playerIdCounter = 0
    var games: MutableList<Game> = mutableListOf()
    var currentGame: Game? = null
        get() = games.last()

    init {
        newGame()
    }

    fun getGameByID(id: Int): Game {
      return games[id - 1]
    }

    fun newGame(): Game {
        games.add(Game(++gameCounter))
        return currentGame!!
    }

    fun addPlayerToGame(game: Game, realname: String): Player {
        val player = Player(++playerIdCounter, realname)
        game.players.add(player)
        return player
    }
}
