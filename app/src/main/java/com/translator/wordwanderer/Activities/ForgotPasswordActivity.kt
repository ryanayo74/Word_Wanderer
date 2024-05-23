package com.translator.wordwanderer.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.translator.wordwanderer.R

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val email: EditText = findViewById(R.id.Email)
        val buttonContinue: Button = findViewById(R.id.ButtonContinue)
        val buttonCancel: Button = findViewById(R.id.Cancel)

        auth = FirebaseAuth.getInstance()

        buttonContinue.setOnClickListener {
            val emailInput: String = email.text.toString()

            if (isValidEmail(emailInput)) {
                sendPasswordResetEmail(emailInput)
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

    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully
                    Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LogInActivity::class.java)
                    startActivity(intent)
                } else {
                    // Failed to send password reset email
                    val exception = task.exception
                    if (exception is FirebaseAuthInvalidUserException) {
                        // Email doesn't exist in Firebase user database
                        Toast.makeText(this, "Email doesn't exist.", Toast.LENGTH_SHORT).show()
                    } else {
                        // Other failure
                        Toast.makeText(this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}

