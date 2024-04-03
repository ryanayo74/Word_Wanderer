package com.translator.wordwanderer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val email: EditText = findViewById(R.id.Email)
        val buttonContinue: Button = findViewById(R.id.ButtonContinue)
        val buttonCancel: Button = findViewById(R.id.Cancel)


        buttonContinue.setOnClickListener {
            val emailInput: String = email.text.toString()

            if (isValidEmail(emailInput)) {
                val intent = Intent(this, ForgotPassWordCodeActivity::class.java)
                intent.putExtra("Email", emailInput)
                startActivity(intent)
            } else {
                email.error = "Invalid email address"
            }
        }

        buttonCancel.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }
        private fun isValidEmail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
