package com.translator.wordwanderer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.translator.wordwanderer.R

class TermsConditionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_conditions)

        val img: ImageView = findViewById(R.id.back)

        img.setOnClickListener{
            val intent = Intent(this@TermsConditionsActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}