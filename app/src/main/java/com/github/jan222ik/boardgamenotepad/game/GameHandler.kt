package com.github.jan222ik.boardgamenotepad.game

object GameHandler {
    private var gameCounter = 0
    private var currentGame: Game

    init {
        currentGame = Game(gameCounter)
    }
}