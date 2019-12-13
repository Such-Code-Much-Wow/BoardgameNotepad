package com.github.jan222ik.boardgamenotepad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.jan222ik.boardgamenotepad.server.Server
import kotlinx.android.synthetic.main.activity_main.*
import android.text.method.ScrollingMovementMethod
import com.github.jan222ik.boardgamenotepad.server.ServerService


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerNamesLabel.movementMethod = ScrollingMovementMethod()

        startService(Intent(this, ServerService::class.java))
    }

    fun startGame(view: View) {
        ipLabel.text = Server().getIPAddress(true)
    }

    fun stopGame(view: View) {
        ipLabel.text = ""
    }

    fun resetGame(view: View) {

    }

}
