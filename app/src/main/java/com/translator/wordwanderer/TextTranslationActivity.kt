package com.translator.wordwanderer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TextTranslationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_translation)

        val button1: Button = findViewById(R.id.signInButton)
        val button2: Button = findViewById(R.id.signUpButton)

        val intent = Intent(this, LogInActivity::class.java)
        button1.setOnClickListener {
            startActivity(intent)
        }

        val intent2 = Intent(this, RegistrationActivity::class.java)
        button2.setOnClickListener {
            startActivity(intent2)
        }
    }
}