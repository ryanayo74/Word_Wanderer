package com.translator.wordwanderer.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import com.translator.wordwanderer.R

class PremiumDashboardActivity : AppCompatActivity() {


    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var displayNameTextView: TextView
    private lateinit var profileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium_dashboard)

        val modulesImageView: ImageView = findViewById(R.id.modules)
        val gamesImageView: ImageView = findViewById(R.id.games)
        val translationImageView: ImageView = findViewById(R.id.translationImageView)
        val logoutButton: ImageView = findViewById(R.id.buttonLogout)
        displayNameTextView = findViewById(R.id.textViewUserName)
        profileImageView = findViewById(R.id.profile)


        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Load user profile
        val user = auth.currentUser
        if (user != null) {
            loadUserProfile(user.uid)
        }

        profileImageView.setOnClickListener {
            val intent = Intent(this@PremiumDashboardActivity, PremiumProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        logoutButton.setOnClickListener {
            val intent = Intent(this@PremiumDashboardActivity, LoginGuestActivity::class.java)
            startActivity(intent)
            finish()
        }

        modulesImageView.setOnClickListener {
            val intent = Intent(this@PremiumDashboardActivity, PremiumModulesActivity::class.java)
            startActivity(intent)
            finish()
        }

        gamesImageView.setOnClickListener {
            val intent = Intent(this@PremiumDashboardActivity, PremiumGamesActivity::class.java)
            startActivity(intent)
            finish()
        }

        translationImageView.setOnClickListener {
            val intent = Intent(this@PremiumDashboardActivity, PremiumTextTranslation::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadUserProfile(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val fullName = document.getString("fullName")
                    val profileImageUrl = document.getString("profileImageUrl")

                    displayNameTextView.text = fullName ?: "No name found"

                    if (profileImageUrl != null) {
                        Picasso.get()
                            .load(profileImageUrl)
                            .transform(CropCircleTransformation())
                            .into(profileImageView)
                    } else {
                        showToast("Profile image URL not found.")
                    }
                } else {
                    showToast("User profile does not exist.")
                    displayNameTextView.text = "No name found"
                }
            }
            .addOnFailureListener { exception ->
                showToast("Failed to load profile: ${exception.message}")
                displayNameTextView.text = "Error loading name"
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { dialog, which ->
                super.onBackPressed()
            }
            .setNegativeButton("No", null)
            .show()
    }
}