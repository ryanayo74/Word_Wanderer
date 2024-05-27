package com.translator.wordwanderer.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.translator.wordwanderer.adapter.TranslationAdapterModuleThree
import com.translator.wordwanderer.model.ModuleThreeTranslation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.translator.wordwanderer.R

class ModuleThreeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_module_three)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val translations = listOf(
            ModuleThreeTranslation("In Japan, bowing is a common form of greeting.", "En Japón, inclinarse es una forma común de saludo.") ,
            ModuleThreeTranslation("In France, it is customary to greet people with a kiss on both cheeks.","En France, il est de coutume de saluer les gens avec une bise sur les deux joues."),
            ModuleThreeTranslation("In China, it is polite to leave a bit of food on your plate.", "En Chine, il est poli de laisser un peu de nourriture dans votre assiette."),
            ModuleThreeTranslation("In Italy, it is customary to finish all the food on your plate.", "En Italie, il est de coutume de finir toute la nourriture dans votre assiette."),
            ModuleThreeTranslation("In India, it is considered rude to open a gift in front of the giver.", "En Inde, il est considéré comme impoli d'ouvrir un cadeau devant la personne qui l'offre."),
            ModuleThreeTranslation("In Brazil, avoid giving purple flowers as they are associated with mourning.", "Au Brésil, évitez de donner des fleurs violettes car elles sont associées au deuil."),
            ModuleThreeTranslation("In Greece, nodding your head means no, and shaking it means yes.", "En Grèce, hocher la tête signifie non, et la secouer signifie oui."),
            ModuleThreeTranslation("In the Middle East, showing the sole of your shoe is considered offensive.", "Au Moyen-Orient, montrer la semelle de votre chaussure est considéré comme offensant."),
            ModuleThreeTranslation("In Germany, it is important to be punctual for business meetings.", "En Allemagne, il est important d'être ponctuel pour les réunions d'affaires."),
            ModuleThreeTranslation("In Japan, business cards are exchanged with both hands and a bow.", "Au Japon, les cartes de visite sont échangées avec les deux mains et une révérence."),

            )
        val listView : ListView = findViewById(R.id.listView)
        val adapter = TranslationAdapterModuleThree(this, translations)
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
                            val intent = Intent(this@ModuleThreeActivity, PremiumGamesActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@ModuleThreeActivity, GamesActivity::class.java)
                            startActivity(intent)
                        }
                        finish() // Finish current activity to prevent going back to it with back button
                    } else {
                        // Fallback in case the document does not exist or userType is not found
                        val intent = Intent(this@ModuleThreeActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Log.d("FirstGameActivity", "Error fetching user data", exception)
                    val intent = Intent(this@ModuleThreeActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        } else {
            // Fallback in case the user is not authenticated
            val intent = Intent(this@ModuleThreeActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}