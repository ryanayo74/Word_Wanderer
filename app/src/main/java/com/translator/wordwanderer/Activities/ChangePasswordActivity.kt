package com.translator.wordwanderer.Activities

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.translator.wordwanderer.R

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var newPassword: EditText
    private lateinit var repeatNewPassword: EditText
    private lateinit var btnChangePassword: Button
    private lateinit var btnCancel: Button
    private lateinit var checkBoxNewShowPassword: CheckBox
    private lateinit var checkBoxRepeatShowPassword: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)  // Set your layout here

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        newPassword = findViewById(R.id.newPassword)
        repeatNewPassword = findViewById(R.id.repeatNewPassword)
        btnChangePassword = findViewById(R.id.btnChangePassword)
        btnCancel = findViewById(R.id.btnCancel)
        checkBoxNewShowPassword = findViewById(R.id.checkBoxNewShowPassword)
        checkBoxRepeatShowPassword = findViewById(R.id.checkBoxRepeatShowPassword)

        val user = auth.currentUser

        btnChangePassword.setOnClickListener {
            changeUserPassword(user)
        }
        btnCancel.setOnClickListener {
            val intent = Intent(this@ChangePasswordActivity, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        checkBoxNewShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                newPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                newPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        checkBoxRepeatShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                repeatNewPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                repeatNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    private fun changeUserPassword(user: FirebaseUser?) {
        if (user != null) {
            val newPasswordText = newPassword.text.toString().trim()
            val repeatNewPasswordText = repeatNewPassword.text.toString().trim()

            if (newPasswordText.isEmpty() || repeatNewPasswordText.isEmpty()) {
                Toast.makeText(this, "New password fields cannot be empty", Toast.LENGTH_SHORT).show()
                Log.d("PasswordUpdate", "New password fields are empty.")
                return
            }

            if (newPasswordText != repeatNewPasswordText) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                Log.d("PasswordUpdate", "Passwords do not match.")
                return
            }

            if (newPasswordText.length < 6) { // Example minimum length validation
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                Log.d("PasswordUpdate", "Password is too short.")
                return
            }

            val hasUpperCase = newPasswordText.any { it.isUpperCase() }
            val hasLowerCase = newPasswordText.any { it.isLowerCase() }
            val hasDigit = newPasswordText.any { it.isDigit() }

            if (!hasUpperCase || !hasLowerCase || !hasDigit) {
                Toast.makeText(this, "Password must contain at least one uppercase letter, one lowercase letter, and one digit", Toast.LENGTH_LONG).show()
                Log.d("PasswordUpdate", "Password does not meet complexity requirements.")
                return
            }

            user.updatePassword(newPasswordText).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("PasswordUpdate", "User password updated.")
                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("PasswordUpdate", "Error password not updated", task.exception)
                    Toast.makeText(this, "Error: Password not updated", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Log.d("PasswordUpdate", "User is not signed in.")
            Toast.makeText(this, "User is not signed in", Toast.LENGTH_SHORT).show()
        }
    }
}