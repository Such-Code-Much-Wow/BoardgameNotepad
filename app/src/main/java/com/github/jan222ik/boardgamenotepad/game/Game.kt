package com.github.jan222ik.boardgamenotepad.game

data class Game(
    val gameId: Int,
    var players: List<RemotePlayer> = listOf(),
    var messages: HashMap<Player, String> = hashMapOf()
)