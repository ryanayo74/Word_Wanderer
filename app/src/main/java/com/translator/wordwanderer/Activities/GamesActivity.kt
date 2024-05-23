package com.translator.wordwanderer.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.translator.wordwanderer.R


class GamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        val username = intent.getStringExtra("Username")
        val homeImageView: ImageView = findViewById(R.id.homeButton)
        val accountImageView: ImageView = findViewById(R.id.accountButton)

        //UnderConstruction
        val upgradeVipImageView: ImageView = findViewById(R.id.upgradeVipImageView)
        val imageViewGame1: ImageView = findViewById(R.id.imageViewGame1)
        val imageViewGame2: ImageView = findViewById(R.id.imageViewGame2)
        val imageViewGame3: ImageView = findViewById(R.id.imageViewGame3)
        val imageViewGame4: ImageView = findViewById(R.id.imageViewGame4)

        homeImageView.setOnClickListener {
            navigateToDashboard()
        }

        accountImageView.setOnClickListener {
            // Start ProfileActivity and pass the username as an extra
            val profileIntent = Intent(this@GamesActivity, ProfileActivity::class.java)
            profileIntent.putExtra("Username", username)
            startActivity(profileIntent)
            finish()
        }

        //UnderConstruction
        upgradeVipImageView.setOnClickListener {
            val intent = Intent(this@GamesActivity, UnderConstruction::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
            finish()
        }

        imageViewGame1.setOnClickListener {
            val intent = Intent(this@GamesActivity, FirstGameActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageViewGame2.setOnClickListener {
            val intent = Intent(this@GamesActivity, UnderConstruction::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
            finish()
        }

        imageViewGame3.setOnClickListener {
            val intent = Intent(this@GamesActivity, UnderConstruction::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
            finish()
        }

        imageViewGame4.setOnClickListener {
            val intent = Intent(this@GamesActivity, UnderConstruction::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
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

