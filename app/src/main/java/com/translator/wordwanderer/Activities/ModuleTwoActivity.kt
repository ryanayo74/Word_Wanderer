package com.translator.wordwanderer.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.translator.wordwanderer.Adapter.TranslationAdapterModuleTwo
import com.translator.wordwanderer.Model.ModuleTwoTranslation

import com.translator.wordwanderer.R

class ModuleTwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_module_two)

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
        // Handle back press to navigate to DashboardActivity
        NavigateToModules()
    }

    private fun NavigateToModules() {
        val intent = Intent(this@ModuleTwoActivity, ModulesActivity::class.java)
        startActivity(intent)
        finish()
    }
}