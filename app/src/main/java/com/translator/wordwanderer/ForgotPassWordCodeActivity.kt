package com.translator.wordwanderer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class ForgotPassWordCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass_word_code)

        val email = intent.getStringExtra("Email")
        val buttonContinue: Button = findViewById(R.id.ButtonContinue)
        val buttonResendCode: Button = findViewById(R.id.ButtonResendCode)
        val firstBox: EditText = findViewById(R.id.firstbox)
        val secondBox: EditText = findViewById(R.id.secondbox)
        val thirdBox: EditText = findViewById(R.id.thirdbox)
        val fourthBox: EditText = findViewById(R.id.fourthbox)

        firstBox.addTextChangedListener(MoveFocusTextWatcher(firstBox, secondBox))
        secondBox.addTextChangedListener(MoveFocusTextWatcher(secondBox, thirdBox))
        thirdBox.addTextChangedListener(MoveFocusTextWatcher(thirdBox, fourthBox))

        buttonContinue.setOnClickListener {
            if (validateFields(firstBox, secondBox, thirdBox, fourthBox)) {
                val intent = Intent(this, CreateNewPasswordActivity::class.java)
                intent.putExtra("Email", email)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter the verification code", Toast.LENGTH_SHORT).show()
            }
        }

        buttonResendCode.setOnClickListener {
            firstBox.text.clear()
            secondBox.text.clear()
            thirdBox.text.clear()
            fourthBox.text.clear()

            firstBox.requestFocus()
        }
    }

    inner class MoveFocusTextWatcher(private val currentView: EditText, private val nextView: EditText) :
        TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.length == 1) {
                nextView.requestFocus()
            }
        }
        override fun afterTextChanged(s: Editable?) {

        }
    }
    private fun validateFields(vararg editTexts: EditText): Boolean {
        for (editText in editTexts) {
            if (editText.text.isNullOrEmpty()) {
                return false
            }
        }
        return true
    }
}