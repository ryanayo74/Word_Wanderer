package com.translator.wordwanderer.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.translator.wordwanderer.R

class PaymentActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val checkoutButton: Button = findViewById(R.id.checkout)
        checkoutButton.setOnClickListener {
            // Update user type to premium and add payment timestamp in Firestore
            val currentUser = auth.currentUser
            currentUser?.let { user ->
                val userId = user.uid
                val updates = hashMapOf(
                    "userType" to "premium",
                    "paymentTimestamp" to FieldValue.serverTimestamp()
                )
                db.collection("users").document(userId)
                    .update(updates)
                    .addOnSuccessListener {
                        // Show popup for payment successful
                        showPaymentSuccessPopup()
                    }
                    .addOnFailureListener { e ->
                        // Handle the error
                    }
            }
        }
    }

    private fun showPaymentSuccessPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Payment Successful")
        builder.setMessage("Thank you for your purchase. You are now a premium user!")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            // Direct to PremiumDashboardActivity
            val intent = Intent(this, PremiumDashboardActivity::class.java)
            startActivity(intent)
            finish() // Optional: Finish PaymentActivity to prevent going back to it with back button
        }
        builder.setCancelable(false)
        builder.show()
    }
}