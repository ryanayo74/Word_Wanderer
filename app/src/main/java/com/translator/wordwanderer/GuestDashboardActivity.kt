package com.translator.wordwanderer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.translator.wordwanderer.databinding.ActivityGuestDashboardBinding
import java.util.Locale

class GuestDashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityGuestDashboardBinding
    private lateinit var textToSpeech: TextToSpeech

    private var items = arrayOf(
        "Arabic", "Bengali", "Chinese (Simplified)", "Chinese (Traditional)", "Danish",
        "Dutch", "English", "Finnish", "French", "German", "Greek", "Gujarati", "Hebrew",
        "Hindi", "Indonesian", "Italian", "Japanese", "Korean", "Malay", "Norwegian",
        "Portuguese", "Russian", "Spanish", "Swedish", "Tagalog", "Tamil", "Telugu",
        "Thai", "Turkish", "Vietnamese"
    )

    private var conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guest_dashboard)

        val itemsAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line, items)


        binding.languageFrom.setAdapter(itemsAdapter)
        binding.languageTo.setAdapter(itemsAdapter)

        binding.translate.setOnClickListener {

            val options = TranslatorOptions.Builder()
                .setSourceLanguage(selectFrom())
                .setTargetLanguage(selectTo())
                .build()

            val englishGermanTranslator = Translation.getClient(options)

            englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {

                    englishGermanTranslator.translate(binding.input.text.toString())
                        .addOnSuccessListener { translatedText ->

                            binding.output.text = translatedText

                        }
                        .addOnFailureListener { exception ->

                            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()

                        }


                }
                .addOnFailureListener { exception ->

                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()

                }
        }


        binding.micButton.setOnClickListener {
            startSpeechToText()
        }

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Toast.makeText(this, "language is not supported", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.sound.setOnClickListener {
            if (binding.output.text.toString().trim().isNotEmpty()) {
                textToSpeech.speak(
                    binding.output.text.toString().trim(),
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
                )
            } else {
                Toast.makeText(this, "Required", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...")

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                applicationContext,
                "Speech to text not supported",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun selectFrom(): String {
        return when (binding.languageFrom.text.toString()) {
            "" -> TranslateLanguage.ENGLISH
            "Arabic" -> TranslateLanguage.ARABIC
            "Bengali" -> TranslateLanguage.BENGALI
            "Chinese (Simplified)" -> TranslateLanguage.CHINESE
            "Danish" -> TranslateLanguage.DANISH
            "Dutch" -> TranslateLanguage.DUTCH
            "English" -> TranslateLanguage.ENGLISH
            "Finnish" -> TranslateLanguage.FINNISH
            "French" -> TranslateLanguage.FRENCH
            "German" -> TranslateLanguage.GERMAN
            "Greek" -> TranslateLanguage.GREEK
            "Gujarati" -> TranslateLanguage.GUJARATI
            "Hebrew" -> TranslateLanguage.HEBREW
            "Hindi" -> TranslateLanguage.HINDI
            "Indonesian" -> TranslateLanguage.INDONESIAN
            "Italian" -> TranslateLanguage.ITALIAN
            "Japanese" -> TranslateLanguage.JAPANESE
            "Korean" -> TranslateLanguage.KOREAN
            "Malay" -> TranslateLanguage.MALAY
            "Norwegian" -> TranslateLanguage.NORWEGIAN
            "Portuguese" -> TranslateLanguage.PORTUGUESE
            "Russian" -> TranslateLanguage.RUSSIAN
            "Spanish" -> TranslateLanguage.SPANISH
            "Swedish" -> TranslateLanguage.SWEDISH
            "Tagalog" -> TranslateLanguage.TAGALOG
            "Tamil" -> TranslateLanguage.TAMIL
            "Telugu" -> TranslateLanguage.TELUGU
            "Thai" -> TranslateLanguage.THAI
            "Turkish" -> TranslateLanguage.TURKISH
            "Vietnamese" -> TranslateLanguage.VIETNAMESE
            else -> TranslateLanguage.ENGLISH
        }
    }

    private fun selectTo(): String {
        return when (binding.languageTo.text.toString()) {
            "" -> TranslateLanguage.ENGLISH
            "Arabic" -> TranslateLanguage.ARABIC
            "Bengali" -> TranslateLanguage.BENGALI
            "Chinese (Simplified)" -> TranslateLanguage.CHINESE
            "Danish" -> TranslateLanguage.DANISH
            "Dutch" -> TranslateLanguage.DUTCH
            "English" -> TranslateLanguage.ENGLISH
            "Finnish" -> TranslateLanguage.FINNISH
            "French" -> TranslateLanguage.FRENCH
            "German" -> TranslateLanguage.GERMAN
            "Greek" -> TranslateLanguage.GREEK
            "Gujarati" -> TranslateLanguage.GUJARATI
            "Hebrew" -> TranslateLanguage.HEBREW
            "Hindi" -> TranslateLanguage.HINDI
            "Indonesian" -> TranslateLanguage.INDONESIAN
            "Italian" -> TranslateLanguage.ITALIAN
            "Japanese" -> TranslateLanguage.JAPANESE
            "Korean" -> TranslateLanguage.KOREAN
            "Malay" -> TranslateLanguage.MALAY
            "Norwegian" -> TranslateLanguage.NORWEGIAN
            "Portuguese" -> TranslateLanguage.PORTUGUESE
            "Russian" -> TranslateLanguage.RUSSIAN
            "Spanish" -> TranslateLanguage.SPANISH
            "Swedish" -> TranslateLanguage.SWEDISH
            "Tagalog" -> TranslateLanguage.TAGALOG
            "Tamil" -> TranslateLanguage.TAMIL
            "Telugu" -> TranslateLanguage.TELUGU
            "Thai" -> TranslateLanguage.THAI
            "Turkish" -> TranslateLanguage.TURKISH
            "Vietnamese" -> TranslateLanguage.VIETNAMESE
            else -> TranslateLanguage.ENGLISH
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val spokenText = result?.get(0)
                binding.input.setText(spokenText)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_SPEECH_INPUT = 100
    }
}
