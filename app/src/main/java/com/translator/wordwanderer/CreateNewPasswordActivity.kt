package com.translator.wordwanderer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class CreateNewPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_password)
        val email = intent.getStringExtra("Email")
        val newPassword: EditText? = findViewById(R.id.createNewPassword)
        val confirmPassword: EditText? = findViewById(R.id.confirmNewPassword)
        val buttonSubmit: Button? = findViewById(R.id.ButtonSubmit)
        val cancelButton: Button? = findViewById(R.id.Cancel)

        buttonSubmit?.setOnClickListener {
            val newPasswordInput: String = newPassword?.text.toString() ?: ""
            val confirmPasswordInput: String = confirmPassword?.text.toString() ?: ""

            if (newPasswordInput.isEmpty()) {
                Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
            } else if (validatePassword(newPasswordInput) && newPasswordInput == confirmPasswordInput) {
                val intent = Intent(this, LogInActivity::class.java)
                intent.putExtra("Email", email)
                intent.putExtra("Password", newPasswordInput)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Password does not meet requirements or passwords do not match", Toast.LENGTH_SHORT).show()
                // Reset passwords
                newPassword?.text?.clear()
                confirmPassword?.text?.clear()
            }
        }

        cancelButton?.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validatePassword(password: String): Boolean {
        val pattern = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+\$")
        return pattern.matches(password)
    }
}
