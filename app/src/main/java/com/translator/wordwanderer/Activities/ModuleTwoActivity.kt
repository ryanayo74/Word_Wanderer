package com.translator.wordwanderer.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.translator.wordwanderer.adapter.TranslationAdapterModuleTwo
import com.translator.wordwanderer.model.ModuleTwoTranslation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.translator.wordwanderer.R

class ModuleTwoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_module_two)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val translations = listOf(
        ModuleTwoTranslation("I'm looking for a good restaurant.", "Je cherche un bon restaurant.") ,
        ModuleTwoTranslation("Can you show me the way to the train station?", "Pouvez-vous me montrer le chemin de la gare ?"),
        ModuleTwoTranslation("Where can I find a taxi?", "Où puis-je trouver un taxi ?"),
        ModuleTwoTranslation("Could you repeat that, please?", "Pourriez-vous répéter, s'il vous plaît ?"),
        ModuleTwoTranslation("I need some help with this problem.", "J'ai besoin d'aide avec ce problème."),
        ModuleTwoTranslation("This food is delicious!", "Cette nourriture est délicieuse !"),
        ModuleTwoTranslation("What do you recommend?", "Que recommandez-vous ?"),
        ModuleTwoTranslation("How long does it take to get there?", "Combien de temps faut-il pour y arriver ?"),
        ModuleTwoTranslation("Can I pay by credit card?", "Puis-je payer par carte de crédit ?"),
        ModuleTwoTranslation("I'm here on vacation.", "Je suis ici en vacances."),
        ModuleTwoTranslation("Where can I buy tickets?", "Où puis-je acheter des billets ?"),
        ModuleTwoTranslation("Do you speak English?", "Parlez-vous anglais ?"),
        ModuleTwoTranslation("I'll have the same as him/her.", "Je prendrai la même chose que lui/elle."),
        ModuleTwoTranslation("Could you please turn down the music?", "Pourriez-vous baisser la musique, s'il vous plaît ?"),
        ModuleTwoTranslation("Could you recommend a good book to read?", "Pourriez-vous recommander un bon livre à lire ?"),
        )

        val listView : ListView = findViewById(R.id.listView)
        val adapter = TranslationAdapterModuleTwo(this, translations)
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
                            val intent = Intent(this@ModuleTwoActivity, PremiumGamesActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@ModuleTwoActivity, GamesActivity::class.java)
                            startActivity(intent)
                        }
                        finish() // Finish current activity to prevent going back to it with back button
                    } else {
                        // Fallback in case the document does not exist or userType is not found
                        val intent = Intent(this@ModuleTwoActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Log.d("FirstGameActivity", "Error fetching user data", exception)
                    val intent = Intent(this@ModuleTwoActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        } else {
            // Fallback in case the user is not authenticated
            val intent = Intent(this@ModuleTwoActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}