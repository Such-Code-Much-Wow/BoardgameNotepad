package com.github.jan222ik.boardgamenotepad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.text.method.ScrollingMovementMethod
import com.github.jan222ik.boardgamenotepad.server.ServerService
import com.github.jan222ik.boardgamenotepad.utils.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var intentField : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intentField = Intent(this, ServerService::class.java)
        playerNamesLabel.movementMethod = ScrollingMovementMethod()
        ipLabel.setOnLongClickListener {
            val s = ipLabel.text
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, s)
            startActivity(Intent.createChooser(sharingIntent, "Share text via"))
            return@setOnLongClickListener true
        }
    }

    fun startGame(view: View) {
        startService(intentField)
        ipLabel.text = Utils.getIPAddress(true)
    }

    fun stopGame(view: View) {
        stopService(intentField)
        ipLabel.text = ""
    }

    fun resetGame(view: View) {
        stopService(intentField)
        startService(intentField)
    }

}
