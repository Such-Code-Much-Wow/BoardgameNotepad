package com.github.jan222ik.boardgamenotepad

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.github.jan222ik.boardgamenotepad.game.Game
import com.github.jan222ik.boardgamenotepad.server.ServerService
import com.github.jan222ik.boardgamenotepad.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var intentField: Intent

    @SuppressLint("SetJavaScriptEnabled")
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

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        webView.settings.javaScriptEnabled = true

        startBtn.setOnClickListener { startGameService() }
        stopBtn.setOnClickListener { stopGameService() }
        resetBtn.setOnClickListener { resetGameService() }
        startGameBtn.setOnClickListener { startGame() }
        openInputBtn.setOnClickListener { openInput() }
        webViewCloseBtn.setOnClickListener { closeInput() }
        webViewReloadBtn.setOnClickListener { reloadInputView() }

        Game.onPlayersChanged = {oldValue, newValue ->
            playerCountLabel.setText(newValue?.size)
            playerNamesLabel.text = newValue?.joinToString { player ->  player.realname}
        }
    }

    fun startGameService() {
        startService(intentField)
        ipLabel.text = Utils.getIPAddress(true)
        webView.loadUrl("http://localhost:8080/")
        stopBtn.isEnabled = true
        resetBtn.isEnabled = true
        startBtn.isEnabled = false
        startGameBtn.isEnabled = true
        openInputBtn.isEnabled = true
    }

    fun stopGameService() {
        stopService(intentField)
        ipLabel.text = ""
        stopBtn.isEnabled = false
        resetBtn.isEnabled = false
        startBtn.isEnabled = true
        openInputBtn.isEnabled = false
        openInputBtn.isEnabled = false
    }

    fun resetGameService() {
        stopService(intentField)
        startService(intentField)
    }

    fun startGame() {
        startGameBtn.isEnabled = Game.endJoin()
    }

    fun openInput() {
        commandLayout.visibility = View.GONE
        gameViewLayout.visibility = View.VISIBLE
    }

    fun closeInput() {
        commandLayout.visibility = View.VISIBLE
        gameViewLayout.visibility = View.GONE
    }

    fun reloadInputView() {
        webView.reload()
    }
}
