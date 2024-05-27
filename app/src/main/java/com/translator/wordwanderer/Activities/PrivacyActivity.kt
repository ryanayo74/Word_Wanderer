package com.translator.wordwanderer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.translator.wordwanderer.R

class PrivacyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)
        val btn: ImageView = findViewById(R.id.bckbtn)

        btn.setOnClickListener{
            val intent = Intent(this@PrivacyActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

    }
}