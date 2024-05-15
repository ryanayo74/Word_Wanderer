package com.translator.wordwanderer

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_log_in)

        auth = Firebase.auth

        val buttonSignIn: Button = findViewById(R.id.buttonSignIn)
        val editTextUserName: EditText = findViewById(R.id.textUserName)
        val editTextPassword: EditText = findViewById(R.id.textPassword)
        val checkBoxShowPassword: CheckBox = findViewById(R.id.checkBoxShowPassword)
        val forgotPassword: TextView = findViewById(R.id.textForgotPassword)
        val register: TextView = findViewById(R.id.textRegister)

        // Show or hide password
        checkBoxShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

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
                        val user = auth.currentUser
                        if (user != null && user.isEmailVerified) {
                            // Email is verified, proceed to the next activity
                            Toast.makeText(this, "Log in successful.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, DashboardActivity::class.java)
                            intent.putExtra("Username", inputUserName)
                            startActivity(intent)
                            finish()
                        } else {
                            // Email is not verified, log out and show message
                            auth.signOut()
                            Toast.makeText(this, "Please verify your email address.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Incorrect Email or Password.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}