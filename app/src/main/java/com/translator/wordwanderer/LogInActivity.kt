package com.translator.wordwanderer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide();
        setContentView(R.layout.activity_log_in)

        val buttonSignIn:Button = findViewById(R.id.buttonSignIn)
        val editTextUserName:EditText = findViewById(R.id.textUserName)
        val editTextPassword:EditText = findViewById(R.id.textPassword)
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

        buttonSignIn.setOnClickListener {
            val inputUserName: String = editTextUserName.text.toString().trim()
            val inputPassword: String = editTextPassword.text.toString().trim()

            if (inputUserName.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "Please input email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(inputUserName).matches()
            val containsCapital = inputPassword.any { it.isUpperCase() }
            val containsCharacter = inputPassword.any { it.isLetter() }
            val containsNumber = inputPassword.any { it.isDigit() }

            if (!isValidEmail || inputUserName != inputUserName.toLowerCase()) {
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
            intent.putExtra("Username", inputUserName)
            intent.putExtra("Password", inputPassword)
            startActivity(intent)
        }

        }
    }
