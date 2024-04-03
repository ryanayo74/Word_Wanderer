package com.translator.wordwanderer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class GuestDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_dashboard)

        val button1: Button = findViewById(R.id.signInButton)
        val button2: Button = findViewById(R.id.signUpButton)
        val textButton:Button = findViewById(R.id.textButton)

        var intent = Intent(this, LogInActivity::class.java)
        button1.setOnClickListener {
            startActivity(intent)
        }

        var intent2 = Intent(this, RegistrationActivity::class.java)
        button2.setOnClickListener {
            startActivity(intent2)
        }

        var intent3 = Intent(this, TextTranslationActivity::class.java)
        textButton.setOnClickListener {
            startActivity(intent3)
        }
    }
}