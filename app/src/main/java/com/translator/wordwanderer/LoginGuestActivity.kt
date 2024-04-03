package com.translator.wordwanderer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class LoginGuestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_guest)


        val button1:Button = findViewById(R.id.button_signin)
        val button2:Button = findViewById(R.id.  button_login_guest)

        var intent = Intent(this, GuestDashboardActivity::class.java)
        button2.setOnClickListener {
            startActivity(intent)
        }

        var intent2 = Intent(this, LogInActivity::class.java)
        button1.setOnClickListener {
            startActivity(intent2)
        }
    }
}