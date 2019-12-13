package com.github.jan222ik.boardgamenotepad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            true
        }

        startBtn.setOnClickListener { startGame() }
        stopBtn.setOnClickListener { stopGame() }
        resetBtn.setOnClickListener { resetGame() }
    }

    fun startGame() {
        startService(intentField)
        ipLabel.text = Utils.getIPAddress(true)
        stopBtn.isEnabled = true
        resetBtn.isEnabled = true
        startBtn.isEnabled = false
    }

    fun stopGame() {
        stopService(intentField)
        ipLabel.text = ""
        stopBtn.isEnabled = false
        resetBtn.isEnabled = false
        startBtn.isEnabled = true
    }

    fun resetGame() {
        stopService(intentField)
        startService(intentField)
    }

}
