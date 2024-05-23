package com.translator.wordwanderer.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.translator.wordwanderer.Model.Translation
import com.translator.wordwanderer.R
import com.translator.wordwanderer.TranslationAdapter

class ModuleOneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_module_one)

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
        // Handle back press to navigate to DashboardActivity
         NavigateToModules()
    }

    private fun NavigateToModules() {
        val intent = Intent(this@ModuleOneActivity, ModulesActivity::class.java)
        startActivity(intent)
        finish()
    }
}