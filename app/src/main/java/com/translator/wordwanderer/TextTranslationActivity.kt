package com.translator.wordwanderer

import android.content.*
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.translator.wordwanderer.databinding.ActivityTextTranslationBinding
import java.util.*

class TextTranslationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextTranslationBinding
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var progressBar: ProgressBar

    private val languages = arrayOf(
        "Arabic", "Bengali", "Chinese", "Dutch", "English", "French",
        "German", "Hindi", "Italian", "Japanese", "Korean","Portuguese",
        "Russian", "Tagalog", "Spanish", "Urdu"
    )

    private val downloadConditions = DownloadConditions.Builder()
        .requireWifi()
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_translation)

        setupDropdown(binding.languageFrom, languages)
        setupDropdown(binding.languageTo, languages)

        progressBar = findViewById(R.id.progressBar)

        binding.translate.setOnClickListener {
            showLoading(true)

            val options = TranslatorOptions.Builder()
                .setSourceLanguage(getLanguageCode(binding.languageFrom.text.toString()))
                .setTargetLanguage(getLanguageCode(binding.languageTo.text.toString()))
                .build()

            val translator = Translation.getClient(options)

            translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener {
                    translator.translate(binding.input.text.toString())
                        .addOnSuccessListener { translatedText ->
                            showLoading(false)
                            binding.output.text = translatedText
                        }
                        .addOnFailureListener { exception ->
                            showLoading(false)
                            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { exception ->
                    showLoading(false)
                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                }
        }

        binding.micButton.setOnClickListener {
            startSpeechToText()
        }

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Text-to-Speech engine is initialized successfully
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Language is not supported
                    Toast.makeText(this, "Language is not supported", Toast.LENGTH_LONG).show()
                }
            } else {
                // Text-to-Speech engine initialization failed
                Toast.makeText(this, "Some TextToSpeech not supported yet", Toast.LENGTH_LONG).show()
            }
        }

        binding.sound.setOnClickListener {
            val selectedLanguage = getLanguageCode(binding.languageTo.text.toString())
            val locale = getLocaleForLanguage(selectedLanguage)
            val result = textToSpeech.setLanguage(locale)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported right now", Toast.LENGTH_SHORT).show()
            } else {
                val translatedText = binding.output.text.toString().trim()
                if (translatedText.isNotEmpty()) {
                    val speakResult = textToSpeech.speak(
                        translatedText,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        null
                    )
                    if (speakResult == TextToSpeech.ERROR) {
                        Log.e("TTS", "Error while speaking")
                    }
                } else {
                    Toast.makeText(this, "Translated text is empty. Please translate some text first.", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.copyInput.setOnClickListener {
            copyToClipboard(binding.input.text.toString())
        }

        binding.copyOutput.setOnClickListener {
            copyToClipboard(binding.output.text.toString())
        }
    }

    private fun setupDropdown(autoCompleteTextView: AutoCompleteTextView, items: Array<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.showDropDown()
        }
    }

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...")

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "Speech to text not supported", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLanguageCode(language: String): String {
        return when (language) {
            "Arabic" -> TranslateLanguage.ARABIC
            "Bengali" -> TranslateLanguage.BENGALI
            "Chinese" -> TranslateLanguage.CHINESE
            "Dutch" -> TranslateLanguage.DUTCH
            "English" -> TranslateLanguage.ENGLISH
            "French" -> TranslateLanguage.FRENCH
            "German" -> TranslateLanguage.GERMAN
            "Hindi" -> TranslateLanguage.HINDI
            "Italian" -> TranslateLanguage.ITALIAN
            "Japanese" -> TranslateLanguage.JAPANESE
            "Korean" -> TranslateLanguage.KOREAN
            "Portuguese" -> TranslateLanguage.PORTUGUESE
            "Russian" -> TranslateLanguage.RUSSIAN
            "Spanish" -> TranslateLanguage.SPANISH
            "Tagalog" -> TranslateLanguage.TAGALOG
            "Urdu" -> TranslateLanguage.URDU
            else -> TranslateLanguage.ENGLISH
        }
    }

    private fun getLocaleForLanguage(language: String): Locale {
        return when (language) {
            TranslateLanguage.ARABIC -> Locale("ar")
            TranslateLanguage.BENGALI -> Locale("bn")
            TranslateLanguage.CHINESE -> Locale.SIMPLIFIED_CHINESE
            TranslateLanguage.ENGLISH -> Locale.ENGLISH
            TranslateLanguage.FRENCH -> Locale.FRENCH
            TranslateLanguage.GERMAN -> Locale.GERMAN
            TranslateLanguage.HINDI -> Locale("hi")
            TranslateLanguage.ITALIAN -> Locale.ITALIAN
            TranslateLanguage.JAPANESE -> Locale.JAPANESE
            TranslateLanguage.KOREAN -> Locale.KOREAN
            TranslateLanguage.PORTUGUESE -> Locale("pt")
            TranslateLanguage.RUSSIAN -> Locale("ru")
            TranslateLanguage.SPANISH -> Locale("es")
            TranslateLanguage.TAGALOG -> Locale("tl")
            TranslateLanguage.URDU -> Locale("ur")
            else -> Locale.ENGLISH
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = result?.get(0)
            binding.input.setText(spokenText)
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) {
            Toast.makeText(this, "Downloading language model. Please wait...", Toast.LENGTH_LONG).show()
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    companion object {
        private const val REQUEST_CODE_SPEECH_INPUT = 100
    }

    override fun onBackPressed() {
        super.onBackPressed()
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
