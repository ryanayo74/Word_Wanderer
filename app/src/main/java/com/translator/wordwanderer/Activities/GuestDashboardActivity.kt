package com.translator.wordwanderer.Activities

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.translator.wordwanderer.R
import com.translator.wordwanderer.databinding.ActivityGuestDashboardBinding
import java.util.Locale

class GuestDashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityGuestDashboardBinding
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var progressBar: ProgressBar

    private var items = arrayOf(
        "English", "Tagalog"
    )

    private var conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guest_dashboard)

        progressBar = findViewById(R.id.progressBar)

        val signIn: Button = findViewById(R.id.signInButton)
        val signUp: Button = findViewById(R.id.signUpButton)
        val intent = Intent(this, LogInActivity::class.java)
        val intentSignUp = Intent(this, RegistrationActivity::class.java)

        signIn.setOnClickListener {
            startActivity(intent)
        }
        signUp.setOnClickListener {
            startActivity(intentSignUp)
        }

        binding.languageFrom.setOnClickListener {
            showDialog()
        }

        binding.languageTo.setOnClickListener {
            showDialog()
        }

        binding.translate.setOnClickListener {
            showLoading(true)

            val options = TranslatorOptions.Builder()
                .setSourceLanguage(selectFrom())
                .setTargetLanguage(selectTo())
                .build()

            val englishGermanTranslator = Translation.getClient(options)

            englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    englishGermanTranslator.translate(binding.input.text.toString())
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
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Language is not supported", Toast.LENGTH_LONG).show()
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

        binding.copyInput.setOnClickListener {
            copyToClipboard(binding.input.text.toString())
        }

        binding.copyOutput.setOnClickListener {
            copyToClipboard(binding.output.text.toString())
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

    private fun selectFrom(): String {
        return when (binding.languageFrom.text.toString()) {
            "" -> TranslateLanguage.ENGLISH
            "English" -> TranslateLanguage.ENGLISH
            "Tagalog" -> TranslateLanguage.TAGALOG
            else -> TranslateLanguage.ENGLISH
        }
    }

    private fun selectTo(): String {
        return when (binding.languageTo.text.toString()) {
            "" -> TranslateLanguage.ENGLISH
            "English" -> TranslateLanguage.ENGLISH
            "Tagalog" -> TranslateLanguage.TAGALOG
            else -> TranslateLanguage.ENGLISH
        }
    }

    private fun showDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Register for More Features")
            .setMessage("To access more features, register now.")
            .setPositiveButton("Register") { dialog, which ->
                val intent = Intent(this, RegistrationActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
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

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    companion object {
        private const val REQUEST_CODE_SPEECH_INPUT = 100
    }
}
