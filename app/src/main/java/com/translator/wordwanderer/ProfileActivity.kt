package com.translator.wordwanderer

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val topArrow1: TextView = findViewById(R.id.topArrow1)
        val signOut: TextView = findViewById(R.id.topSignOut)
        val userEmail: TextView = findViewById(R.id.userEmail)

        val username = intent.getStringExtra("Username")
        userEmail.text = username

        topArrow1.setOnClickListener {
            navigateToDashboard()
        }

        signOut.setOnClickListener {
            val intent = Intent(this@ProfileActivity, LoginGuestActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateToDashboard()
    }

    private fun navigateToDashboard() {
        val username = intent.getStringExtra("Username") // Retrieve username
        val intent = Intent(this@ProfileActivity, DashboardActivity::class.java)
        intent.putExtra("Username", username) // Pass username to DashboardActivity
        startActivity(intent)
        finish()
    }
}
