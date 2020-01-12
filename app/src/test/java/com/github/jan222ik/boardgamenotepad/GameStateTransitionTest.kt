package com.github.jan222ik.boardgamenotepad

import com.github.jan222ik.boardgamenotepad.game.Game
import com.github.jan222ik.boardgamenotepad.game.GameState
import org.hamcrest.CoreMatchers.`is` as isIt
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class GameStateTransitionTest {


    @Test
    fun testgameloop() {
        Game.reset()
        assertThat(GameState.JOIN, isIt(Game.gameState))
        mapOf(Pair("Name0", 0), Pair("Name1", 1), Pair("Name2", 2), Pair("Name3", 3)).forEach {
            Game.addPlayer(it.key, it.value)
        }
        val endJoin = Game.endJoin()
        assertTrue(endJoin)
        assertThat(GameState.SELECT_MODERATOR, isIt(Game.gameState))
        Game.pickModerator(Game.players.first { it.playerId == 0 })
        assertThat(GameState.ENTER_TEXT, isIt(Game.gameState))
        try {
            Game.queryMessages()
            fail()
        } catch (e: IllegalStateException) {

        }
        Game.players.forEachIndexed { index, player ->
            Game.addMessage(player, "$index$player")
        }
        assertThat(Game.messages.size, isIt(Game.players.size))
        assertThat(GameState.MODERATOR_PEEK, isIt(Game.gameState))
        val queryMessagesModerator = Game.queryMessagesModerator()
        assertThat(queryMessagesModerator.size, isIt(Game.players.size))
        assertThat(GameState.REVEAL, isIt(Game.gameState))
    }
}