package com.translator.wordwanderer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val displayText: TextView = findViewById(R.id.textViewDisplay)
        val name = intent.getStringExtra("Username")

        val logoutButton: ImageView = findViewById(R.id.buttonLogout)

        displayText.text = name

        logoutButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, LoginGuestActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
}