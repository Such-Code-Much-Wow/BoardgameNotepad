package com.github.jan222ik.boardgamenotepad.game

data class Game (
    val gameId: Int,
    var players: MutableList<Player> = mutableListOf(),
    var messages: HashMap<Player, String> = hashMapOf()
)