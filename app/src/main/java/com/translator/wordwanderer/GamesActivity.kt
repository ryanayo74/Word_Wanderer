package com.translator.wordwanderer

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class GamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        val homeImageView: ImageView = findViewById(R.id.homeButton)
        val accountImageView: ImageView = findViewById(R.id.accountButton)

        homeImageView.setOnClickListener {
            navigateToDashboard()
        }

        accountImageView.setOnClickListener {
            val username = intent.getStringExtra("Username")

            // Start ProfileActivity and pass the username as an extra
            val profileIntent = Intent(this@GamesActivity, ProfileActivity::class.java)
            profileIntent.putExtra("Username", username)
            startActivity(profileIntent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Handle back press to navigate to DashboardActivity
        navigateToDashboard()
    }

    private fun navigateToDashboard() {
        val username = intent.getStringExtra("Username") // Retrieve username
        val intent = Intent(this@GamesActivity, DashboardActivity::class.java)
        intent.putExtra("Username", username) // Pass username to DashboardActivity
        startActivity(intent)
        finish()
    }
}

