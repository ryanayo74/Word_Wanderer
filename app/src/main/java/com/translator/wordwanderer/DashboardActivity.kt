package com.translator.wordwanderer

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val displayText: TextView = findViewById(R.id.textViewDisplay)
        val name = intent.getStringExtra("Username")

        val imageView : ImageView = findViewById(R.id.modules)
        val gamesImageView : ImageView = findViewById(R.id.games)

        val logoutButton: ImageView = findViewById(R.id.buttonLogout)

        displayText.text = name

        logoutButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, LoginGuestActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageView.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ModulesActivity::class.java)
            intent.putExtra("Username", name)
            startActivity(intent)
            finish()
        }


        gamesImageView.setOnClickListener {
            val intent = Intent(this@DashboardActivity, GamesActivity::class.java)
            intent.putExtra("Username", name)
            startActivity(intent)
            finish()
        }
    }
}