package com.translator.wordwanderer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FirstScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)

        val button1: Button = findViewById(R.id.exploreButton)

        var intent = Intent(this, LogInActivity::class.java)

        button1.setOnClickListener {
            startActivity(intent)
        }

    }
}