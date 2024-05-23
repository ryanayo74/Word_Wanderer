package com.translator.wordwanderer.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.translator.wordwanderer.R

class ChangeUserNameActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_name)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val firstNameEditText: EditText = findViewById(R.id.editFirstName)
        val lastNameEditText: EditText = findViewById(R.id.editLastName)
        val saveButton: Button = findViewById(R.id.changeUserNameBtn)
        val cancelButton: Button = findViewById(R.id.btnCancel)

        saveButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(this, "First name and Last name cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                val fullName = "$firstName $lastName"
                updateUserFullName(fullName)
            }
        }

        cancelButton.setOnClickListener {
            val intent = Intent(this@ChangeUserNameActivity, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateUserFullName(fullName: String) {
        val user = auth.currentUser
        user?.let {
            db.collection("users").document(user.uid)
                .update("fullName", fullName)
                .addOnSuccessListener {
                    Toast.makeText(this, "Full name updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to update full name: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}