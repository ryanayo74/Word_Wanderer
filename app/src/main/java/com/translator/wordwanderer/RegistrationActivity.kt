package com.translator.wordwanderer

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val buttonRegister: Button = findViewById(R.id.buttonRegister)
        val inputEmail: EditText = findViewById(R.id.emailAddress)
        val inputPhoneNumber: EditText = findViewById(R.id.phoneNumber)
        val inputPassWord: EditText = findViewById(R.id.password)
        var emailInput: String?
        var numInput: String?
        var passInput: String?

        val textLogin: TextView = findViewById(R.id.textLogin)

        textLogin.setOnClickListener {
            val intent = Intent(this@RegistrationActivity, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonRegister.setOnClickListener {
            emailInput = inputEmail.text.toString()
            numInput = inputPhoneNumber.text.toString()
            passInput = inputPassWord.text.toString()

            if (isEmailValid(emailInput!!) && isPasswordValid(passInput!!) && isPhoneValid(numInput!!)) {
                // Create the user in Firebase Authentication
                auth.createUserWithEmailAndPassword(emailInput!!, passInput!!)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Account creation success, save additional user information
                            val user = auth.currentUser
                            val userProfileChangeRequest = UserProfileChangeRequest.Builder()
                                .setDisplayName(numInput) // Save phone number as display name
                                .build()

                            user?.updateProfile(userProfileChangeRequest)
                                ?.addOnCompleteListener { profileTask ->
                                    if (profileTask.isSuccessful) {
                                        // Send email verification
                                        user.sendEmailVerification()
                                            .addOnCompleteListener { emailTask ->
                                                if (emailTask.isSuccessful) {
                                                    // Save user information to Firestore
                                                    saveUserToFirestore(user.uid, emailInput!!, numInput!!)
                                                    Toast.makeText(
                                                        baseContext,
                                                        "Account created. Please verify your email.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    // Navigate to login page
                                                    val intent = Intent(this@RegistrationActivity, LogInActivity::class.java)
                                                    startActivity(intent)
                                                    finish() // Finish the current activity to prevent going back to it with back button
                                                } else {
                                                    // Email verification sending failed
                                                    Log.e(
                                                        TAG,
                                                        "Error sending verification email",
                                                        emailTask.exception
                                                    )
                                                    Toast.makeText(
                                                        baseContext,
                                                        "Failed to send verification email.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    } else {
                                        // Profile update failed
                                        Log.e(
                                            TAG,
                                            "Error updating user profile",
                                            profileTask.exception
                                        )
                                        Toast.makeText(
                                            baseContext,
                                            "Error saving user information",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            // If account creation fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Email already exists.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                // Show validation errors
                if (!isEmailValid(emailInput!!)) {
                    showAlert("Invalid email address")
                }
                if (!isPasswordValid(passInput!!)) {
                    showAlert("Password must contain at least 6 characters, including one uppercase letter and one number.")
                }
                if (!isPhoneValid(numInput!!)) {
                    showAlert("Invalid phone number")
                }
            }
        }
    }

    private fun saveUserToFirestore(userId: String, email: String, phoneNumber: String) {
        val user = hashMapOf(
            "email" to email,
            "phoneNumber" to phoneNumber
        )

        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG, "User document successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing user document", e)
            }
    }

    private fun showAlert(message: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Error")
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton("OK", null)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    //function for email validation
    private fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return emailRegex.toRegex().matches(email)
    }

    //function for password validation
    private fun isPasswordValid(password: String): Boolean {
        if (password.length < 8) {
            return false
        }
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }

        return hasUpperCase && hasLowerCase && hasDigit
    }

    //Function for phone number validation
    private fun isPhoneValid(number: String): Boolean {
        val digitsOnly = number.all { it.isDigit() }
        val length = number.length == 11

        return digitsOnly && length
    }
}




