package com.translator.wordwanderer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide();
        setContentView(R.layout.activity_log_in)

        val button1:Button = findViewById(R.id.buttonSignIn)
        val input:EditText = findViewById(R.id.textUserName)
        val input2:EditText = findViewById(R.id.textPassword)
        val forgotPassword:TextView = findViewById(R.id.textForgotPassword)
        val register:TextView = findViewById(R.id.textRegister)

        forgotPassword.setOnClickListener {
            val intent = Intent(this@LogInActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        register.setOnClickListener {
            val intent = Intent(this@LogInActivity, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

        button1.setOnClickListener {
            val textInput: String = input.text.toString().trim()
            val textInput2: String = input2.text.toString().trim()

            if (textInput.isEmpty() || textInput2.isEmpty()) {
                Toast.makeText(this, "Please input email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(textInput).matches()
            val containsCapital = textInput2.any { it.isUpperCase() }
            val containsCharacter = textInput2.any { it.isLetter() }
            val containsNumber = textInput2.any { it.isDigit() }

            if (!isValidEmail || textInput != textInput.toLowerCase()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!containsCapital || !containsCharacter || !containsNumber) {
                Toast.makeText(
                    this,
                    "Password must contain at least one capital letter, one character, and one number",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // If both email and password are valid, proceed to DashboardActivity
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("Username", textInput)
            intent.putExtra("Password", textInput2)
            startActivity(intent)

        }

        }
    }
