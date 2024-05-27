package com.translator.wordwanderer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.translator.wordwanderer.R

class AboutAppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)
        val btn: ImageView = findViewById(R.id.bckbtn)

        btn.setOnClickListener{
            val intent = Intent(this@AboutAppActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

    }
}