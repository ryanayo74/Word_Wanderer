package com.translator.wordwanderer

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ModulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modules)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val homeImageView: ImageView = findViewById(R.id.homeButton)
        val accountImageView: ImageView = findViewById(R.id.accountButton)

        homeImageView.setOnClickListener {
            navigateToDashboard()
        }

        accountImageView.setOnClickListener {
            val username = intent.getStringExtra("Username")

            // Start ProfileActivity and pass the username as an extra
            val profileIntent = Intent(this@ModulesActivity, ProfileActivity::class.java)
            profileIntent.putExtra("Username", username)
            startActivity(profileIntent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateToDashboard()
    }

    private fun navigateToDashboard() {
        val username = intent.getStringExtra("Username") // Retrieve username
        val intent = Intent(this@ModulesActivity, DashboardActivity::class.java)
        intent.putExtra("Username", username) // Pass username to DashboardActivity
        startActivity(intent)
        finish()
    }
}
