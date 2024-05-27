package com.translator.wordwanderer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.translator.wordwanderer.R

class FirstScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)

        val buttonExplore: Button = findViewById(R.id.exploreButton)

        val intent = Intent(this, LoginGuestActivity::class.java)

        buttonExplore.setOnClickListener {
            startActivity(intent)
        }

    }
}