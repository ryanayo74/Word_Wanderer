package com.translator.wordwanderer.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.translator.wordwanderer.R
import java.util.Calendar
import java.util.Date

class LogInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val db = Firebase.firestore // Initialize Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_log_in)

        auth = Firebase.auth
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        val buttonSignIn: Button = findViewById(R.id.buttonSignIn)
        val editTextUserName: EditText = findViewById(R.id.textUserName)
        val editTextPassword: EditText = findViewById(R.id.textPassword)
        val radioButtonRememberMe: RadioButton = findViewById(R.id.remember_me_radio_button)
        val forgotPassword: TextView = findViewById(R.id.textForgotPassword)
        val register: TextView = findViewById(R.id.textRegister)
        val checkBoxShowPassword: CheckBox = findViewById(R.id.checkBoxShowPassword)

        // Retrieve saved login details if "Remember Me" is checked
        if (sharedPreferences.getBoolean("rememberMe", false)) {
            val savedUserName = sharedPreferences.getString("username", "")
            val savedPassword = sharedPreferences.getString("password", "")
            if (!savedUserName.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                editTextUserName.setText(savedUserName)
                editTextPassword.setText(savedPassword)
                radioButtonRememberMe.isChecked = true
            }
        }

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

            // Check for internet connectivity
            if (!isInternetAvailable()) {
                showOfflineDialog()
                return@setOnClickListener
            }

            // Authenticate user with Firebase
            auth.signInWithEmailAndPassword(inputUserName, inputPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null && user.isEmailVerified) {
                            // Save login details if "Remember Me" is checked
                            if (radioButtonRememberMe.isChecked) {
                                with(sharedPreferences.edit()) {
                                    putString("username", inputUserName)
                                    putString("password", inputPassword)
                                    putBoolean("rememberMe", true)
                                    apply()
                                }
                            } else {
                                // Clear saved login details if "Remember Me" is not checked
                                with(sharedPreferences.edit()) {
                                    remove("username")
                                    remove("password")
                                    putBoolean("rememberMe", false)
                                    apply()
                                }
                            }
                            // Email is verified, proceed to fetch userType
                            val userId = user.uid
                            db.collection("users").document(userId)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document != null && document.exists()) {
                                        val userType = document.getString("userType")
                                        val paymentTimestamp = document.getTimestamp("paymentTimestamp")?.toDate()

                                        if (userType == "premium" && paymentTimestamp != null) {
                                            val calendar = Calendar.getInstance()
                                            calendar.time = paymentTimestamp
                                            calendar.add(Calendar.MONTH, 1)
                                            val expirationDate = calendar.time

                                            if (Date().after(expirationDate)) {
                                                // If the current date is after the expiration date, update to free
                                                db.collection("users").document(userId)
                                                    .update("userType", "free")
                                                    .addOnSuccessListener {
                                                        navigateToDashboard("free")
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Log.d(TAG, "Error updating user type", e)
                                                        Toast.makeText(this, "Error updating user type", Toast.LENGTH_SHORT).show()
                                                    }
                                            } else {
                                                navigateToDashboard("premium")
                                            }
                                        } else {
                                            navigateToDashboard(userType ?: "free")
                                        }
                                    } else {
                                        Log.d(TAG, "No such document")
                                        Toast.makeText(this, "User document doesn't exist", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(TAG, "get failed with ", exception)
                                    Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show()
                                }
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

    private fun navigateToDashboard(userType: String) {
        val intent = when (userType) {
            "premium" -> Intent(this, PremiumDashboardActivity::class.java)
            else -> Intent(this, DashboardActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showOfflineDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("No Internet Connection")
        builder.setMessage("You are not connected to the internet. Would you like to proceed in offline mode?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}