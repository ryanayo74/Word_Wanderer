package com.translator.wordwanderer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class GuestDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_dashboard)

        val buttonSignIn: Button = findViewById(R.id.signInButton)
        val buttonSignUp: Button = findViewById(R.id.signUpButton)
        val buttonTextTranslate:Button = findViewById(R.id.buttonTextTranslate)

        val intent = Intent(this, LogInActivity::class.java)
        buttonSignIn.setOnClickListener {
            startActivity(intent)
        }

        val intent2 = Intent(this, RegistrationActivity::class.java)
        buttonSignUp.setOnClickListener {
            startActivity(intent2)
        }

        val intent3 = Intent(this, TextTranslationActivity::class.java)
        buttonTextTranslate.setOnClickListener {
            startActivity(intent3)
        }
    }
}