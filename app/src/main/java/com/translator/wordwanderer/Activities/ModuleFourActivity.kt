package com.translator.wordwanderer.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.translator.wordwanderer.adapter.TranslationAdapterModuleFour
import com.translator.wordwanderer.model.ModuleFourTranslation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.translator.wordwanderer.R

class ModuleFourActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_four)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val translations = listOf(
            ModuleFourTranslation("I need a doctor.", "Necesito un doctor."),
            ModuleFourTranslation("I am allergic to peanuts.", "Soy alérgico a los cacahuetes."),
            ModuleFourTranslation("Call the police!", "¡Llame a la policía!"),
            ModuleFourTranslation("I have been robbed.", "Me han robado."),
            ModuleFourTranslation("Is there a shelter nearby?", "¿Hay un refugio cerca?"),
            ModuleFourTranslation("What should I do in case of an earthquake?", "¿Qué debo hacer en caso de un terremoto?"),
            ModuleFourTranslation("I am lost. Can you help me?", "Estoy perdido. ¿Puedes ayudarme?"),
            ModuleFourTranslation("I lost my passport.", "Perdí mi pasaporte."),
            ModuleFourTranslation("Where is the nearest hospital?", "¿Dónde está el hospital más cercano?"),
            ModuleFourTranslation("Can you speak English?", "¿Hablas inglés?"),
            ModuleFourTranslation("I need to contact my embassy.", "Necesito contactar con mi embajada."),
            ModuleFourTranslation("Is there a public phone nearby?", "¿Hay un teléfono público cerca?")
        )

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TranslationAdapterModuleFour(translations)
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
                            val intent = Intent(this@ModuleFourActivity, PremiumGamesActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@ModuleFourActivity, GamesActivity::class.java)
                            startActivity(intent)
                        }
                        finish() // Finish current activity to prevent going back to it with back button
                    } else {
                        // Fallback in case the document does not exist or userType is not found
                        val intent = Intent(this@ModuleFourActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Log.d("FirstGameActivity", "Error fetching user data", exception)
                    val intent = Intent(this@ModuleFourActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        } else {
            // Fallback in case the user is not authenticated
            val intent = Intent(this@ModuleFourActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
