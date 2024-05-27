package com.translator.wordwanderer.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.translator.wordwanderer.model.Translation
import com.translator.wordwanderer.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.translator.wordwanderer.adapter.TranslationAdapter

class ModuleOneActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_module_one)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()



        val translations = listOf(
            Translation("Astonished", "Nabigla"),
            Translation("Benevolent", "Mapagbigay"),
            Translation("Conundrum", "Bugtong"),
            Translation("Discombobulate", "Malito"),
            Translation("Ebullient", "Masigla"),
            Translation("Facetious", "Tuwang-tuwa"),
            Translation("Garrulous", "Madaldal"),
            Translation("Halcyon", "Mapayapa"),
            Translation("Insidious", "Kadena"),
            Translation("Juxtaposition", "Pagkakatabi"),
            Translation("Keen", "Masigasig"),
            Translation("Lugubrious", "Malungkot"),
            Translation("Mellifluous", "Matamis pakinggan"),
            Translation("Nebulous", "Malabo"),
            Translation("Ostentatious", "Mapagpanggap"),
            Translation("Pedantic", "Mapagtutol"),
            Translation("Quintessential", "Pinakatampok"),
            Translation("Resplendent", "Maningning"),
            Translation("Serendipity", "Swerte"),
            Translation("Taciturn", "Tahimik"),
            Translation("Ubiquitous", "Lagahanap"),
            Translation("Vivacious", "Masigla"),
            Translation("Wistful", "Nanaghoy"),
            Translation("Xenial", "Magiliw sa bisita"),
            Translation("Yearn", "Mangarap"),
            Translation("Zealous", "Masigasig"),

            )

       val listView : ListView = findViewById(R.id.listView)
        val adapter = TranslationAdapter(this, translations)
        listView.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Handle back press to navigate to the appropriate dashboard
        navigateToDashboard()
    }

    private fun navigateToDashboard() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userType = document.getString("userType")
                        if (userType == "premium") {
                            val intent = Intent(this@ModuleOneActivity, PremiumGamesActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@ModuleOneActivity, GamesActivity::class.java)
                            startActivity(intent)
                        }
                        finish() // Finish current activity to prevent going back to it with back button
                    } else {
                        // Fallback in case the document does not exist or userType is not found
                        val intent = Intent(this@ModuleOneActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Log.d("FirstGameActivity", "Error fetching user data", exception)
                    val intent = Intent(this@ModuleOneActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        } else {
            // Fallback in case the user is not authenticated
            val intent = Intent(this@ModuleOneActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}