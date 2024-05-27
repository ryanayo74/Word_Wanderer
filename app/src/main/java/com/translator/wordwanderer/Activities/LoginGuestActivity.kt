package com.translator.wordwanderer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.translator.wordwanderer.R

class LoginGuestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_guest)


        val loginButton:Button = findViewById(R.id.button_signin)
        val guestButton:Button = findViewById(R.id.button_login_guest)
        val registerTextView:TextView = findViewById(R.id.textRegister)


        guestButton.setOnClickListener {
            val intent = Intent(this, GuestDashboardActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        registerTextView.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}