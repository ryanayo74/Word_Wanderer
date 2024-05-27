package com.translator.wordwanderer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.translator.wordwanderer.R

class UpgradeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upgrade)

        val checkbx: CheckBox = findViewById(R.id.checkBox)
        val termsText = "I agree to the Terms and Conditions"
        val spannableString = SpannableString(termsText)
        val upgradeButton: Button = findViewById(R.id.upgradeButton)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Intent to navigate to Terms and Conditions page
                val intent = Intent(this@UpgradeActivity, TermsAndConditionActivity::class.java)
                startActivity(intent)
            }
        }
        val startIndex = termsText.indexOf("Terms and Conditions")
        val endIndex = startIndex + "Terms and Conditions".length

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        checkbx.text = spannableString
        checkbx.movementMethod = LinkMovementMethod.getInstance()

        // Set the checkbox to be checked by default
        checkbx.isChecked = true

        upgradeButton.setOnClickListener{
            // Check if the checkbox is checked
            if (checkbx.isChecked) {
                // Proceed to PaymentActivity
                val intent = Intent(this@UpgradeActivity, PaymentActivity::class.java)
                startActivity(intent)
            } else {
                // Show a message indicating that the checkbox needs to be checked
                Toast.makeText(this@UpgradeActivity, "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Handle back press to navigate to DashboardActivity
        navigateToDashboard()
    }

    private fun navigateToDashboard() {
        val intent = Intent(this@UpgradeActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}

