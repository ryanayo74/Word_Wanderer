package com.translator.wordwanderer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog


class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val buttonRegister: Button = findViewById(R.id.buttonRegister)
        val inputEmail: EditText = findViewById(R.id.emailAddress)
        val inputPhoneNumber: EditText = findViewById(R.id.phoneNumber)
        val inputPassWord: EditText = findViewById(R.id.password)
        var emailInput:String?
        var numInput:String?
        var passInput: String?

        val textLogin: TextView = findViewById(R.id.textLogin)

        textLogin.setOnClickListener {
            val intent = Intent(this@RegistrationActivity, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonRegister.setOnClickListener{
            emailInput = inputEmail.text.toString()
            numInput = inputPhoneNumber.text.toString()
            passInput = inputPassWord.text.toString()

            //validate all inputs
            if (isEmailValid(emailInput!!) && isPasswordValid(passInput!!) && isPhoneValid(numInput!!)) {
                // All conditions are true, proceed to start the other activity
                intent.putExtra("EMAIL", emailInput)
                intent.putExtra("PASSWORD", passInput)
                intent.putExtra("PHONE_NUM", numInput)
                startActivity(intent)
            } else {
                // At least one condition is false, show corresponding error message
                if (!isEmailValid(emailInput!!)) {
                    showAlert("Invalid email address")
                }
                if (!isPasswordValid(passInput!!)) {
                    showAlert("Invalid password")
                }
                if (!isPhoneValid(numInput!!)) {
                    showAlert("Invalid phone number")
                }
            }

            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
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
}
//function for email validation
private fun isEmailValid(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    return emailRegex.toRegex().matches(email)
}
//function for password validation
private fun isPasswordValid(password: String): Boolean{
    if (password.length < 8){
        return false
    }
    val hasUpperCase = password.any{it.isUpperCase()}
    val hasLowerCase = password.any{it.isLowerCase()}
    val hasDigit = password.any{it.isDigit()}

    return  hasUpperCase && hasLowerCase && hasDigit
}
//Function for phone number validation
private fun isPhoneValid(number: String): Boolean{
    val digitsOnly = number.all{it. isDigit()}

    val length =  number.length == 11

    return digitsOnly && length
}

