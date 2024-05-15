package com.translator.wordwanderer

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.translator.wordwanderer.databinding.ActivityTextTranslationBinding
import java.util.Locale


class TextTranslationActivity : AppCompatActivity() {
    lateinit var binding: TextTranslationActivity
    private lateinit var textToSpeech: TextToSpeech

    private var items = arrayOf(
        "English","Tagalog"
    )

    private var conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_translation)

        val intent = Intent(this, LogInActivity::class.java)
        val intentSignUp = Intent(this, RegistrationActivity::class.java)



        //For UnderConstruction Page
        val upgradeVipImageView: ImageView = findViewById(R.id.upgradeVipImageView)

        upgradeVipImageView.setOnClickListener {
            val intent = Intent(this@TextTranslationActivity, UnderConstruction::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Handle back press to navigate to DashboardActivity
        navigateToDashboard()
    }

    private fun navigateToDashboard() {
        val username = intent.getStringExtra("Username") // Retrieve username
        val intent = Intent(this@TextTranslationActivity, DashboardActivity::class.java)
        intent.putExtra("Username", username) // Pass username to DashboardActivity
        startActivity(intent)
        finish()
    }
}