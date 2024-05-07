package com.translator.wordwanderer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LogInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide();
        setContentView(R.layout.activity_log_in)

        auth = Firebase.auth

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

            // Authenticate user with Firebase
            auth.signInWithEmailAndPassword(inputUserName, inputPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        Toast.makeText(this, "Log in successful.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.putExtra("Username", inputUserName)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Incorrect Email or Password.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
