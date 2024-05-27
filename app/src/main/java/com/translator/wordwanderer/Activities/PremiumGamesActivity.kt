package com.translator.wordwanderer.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.translator.wordwanderer.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class PremiumGamesActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium_games)

        val username = intent.getStringExtra("Username")
        val homeImageView: ImageView = findViewById(R.id.homeButton)
        val accountImageView: ImageView = findViewById(R.id.accountButton)
        val imageViewGame1: ImageView = findViewById(R.id.imageViewGame1)
        val imageViewGame2: ImageView = findViewById(R.id.imageViewGame2)
        val imageViewGame3: ImageView = findViewById(R.id.imageViewGame3)
        val imageViewGame4: ImageView = findViewById(R.id.imageViewGame4)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        homeImageView.setOnClickListener {
            navigateToDashboard()
        }

        accountImageView.setOnClickListener {
            // Start ProfileActivity and pass the username as an extra
            val profileIntent = Intent(this@PremiumGamesActivity, ProfileActivity::class.java)
            profileIntent.putExtra("Username", username)
            startActivity(profileIntent)
            finish()
        }

        imageViewGame1.setOnClickListener {
            val intent = Intent(this@PremiumGamesActivity, FirstGameActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageViewGame2.setOnClickListener {
            val intent = Intent(this@PremiumGamesActivity, SecondGameActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageViewGame3.setOnClickListener {
            val intent = Intent(this@PremiumGamesActivity, ThirdGameActivity::class.java)
            intent.putExtra("Username", username)
            startActivity(intent)
            finish()
        }

        imageViewGame4.setOnClickListener {
            val intent = Intent(this@PremiumGamesActivity, FourthGameActivity::class.java)
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
        val intent = Intent(this@PremiumGamesActivity, PremiumDashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}

