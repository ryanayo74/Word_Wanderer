package com.translator.wordwanderer.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.translator.wordwanderer.R



class FirstGameActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var imageView: ImageView
    private lateinit var questionTextView: TextView
    private lateinit var firstChoice: Button
    private lateinit var secondChoice: Button
    private lateinit var thirdChoice: Button
    private lateinit var fourthChoice: Button
    private lateinit var scoreTextView: TextView


    private var currentQuestionIndex = 0
    private var score = 0

    private val questions = mutableListOf(
        FirstGameQuestions(R.drawable.philippines, "", listOf("Philippines", "Japan", "Brazil", "Korea"), "Philippines"),
        FirstGameQuestions(R.drawable.india, "", listOf("Korea", "Australia", "India", "Vietnam"), "India"),
        FirstGameQuestions(R.drawable.southkorea, "", listOf("South Korea", "Germany", "Argentina", "North Korea"), "South Korea"),
        FirstGameQuestions(R.drawable.japan, "", listOf("Japan", "China", "Thailand", "Vietnam"), "Japan"),
        FirstGameQuestions(R.drawable.australia, "", listOf("Australia", "New Zealand", "Fiji", "Samoa"), "Australia"),
        FirstGameQuestions(R.drawable.brazil, "", listOf("Brazil", "Argentina", "Chile", "Peru"), "Brazil"),
        FirstGameQuestions(R.drawable.chad, "", listOf("Chad", "Niger", "Burkina Faso", "Mali"), "Chad"),
        FirstGameQuestions(R.drawable.turkmenistan, "", listOf("Turkmenistan", "Kazakhstan", "Uzbekistan", "Tajikistan"), "Turkmenistan"),
        FirstGameQuestions(R.drawable.kiribati, "", listOf("Kiribati", "Tuvalu", "Marshall Islands", "Micronesia"), "Kiribati"),
        FirstGameQuestions(R.drawable.seychelles, "", listOf("Seychelles", "Mauritius", "Comoros", "Maldives"), "Seychelles")
    )

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_game)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        imageView = findViewById(R.id.guestimageView)
        firstChoice = findViewById(R.id.firstChoice)
        secondChoice = findViewById(R.id.secondChoice)
        thirdChoice = findViewById(R.id.thirdChoice)
        fourthChoice = findViewById(R.id.fourthChoice)
        scoreTextView = findViewById(R.id.score)

        firstChoice.setOnClickListener { checkAnswer(firstChoice.text.toString()) }
        secondChoice.setOnClickListener { checkAnswer(secondChoice.text.toString()) }
        thirdChoice.setOnClickListener { checkAnswer(thirdChoice.text.toString()) }
        fourthChoice.setOnClickListener { checkAnswer(fourthChoice.text.toString()) }

        // Initialize MediaPlayer and start playing background music
        mediaPlayer = MediaPlayer.create(this, R.raw.game1)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        restartGame()
    }

    override fun onPause() {
        super.onPause()
        // Pause the music when the activity is paused
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        // Resume the music when the activity is resumed
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer when the activity is destroyed
        mediaPlayer.release()
    }

    private fun restartGame() {
        questions.shuffle() // Shuffle the questions list
        currentQuestionIndex = 0
        score = 0
        showQuestion()
    }

    private fun showQuestion() {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            imageView.setImageResource(question.imageResId)
            val shuffledChoices = question.choices.shuffled()
            firstChoice.text = shuffledChoices[0]
            secondChoice.text = shuffledChoices[1]
            thirdChoice.text = shuffledChoices[2]
            fourthChoice.text = shuffledChoices[3]
            scoreTextView.text = "$score/${questions.size}"
        } else {
            // All questions answered, show final score
            showFinalScoreDialog()
        }
    }

    private fun checkAnswer(selectedAnswer: String) {
        val correctAnswer = questions[currentQuestionIndex].correctAnswer
        if (selectedAnswer == correctAnswer) {
            score++
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Wrong! The correct answer was $correctAnswer.", Toast.LENGTH_SHORT).show()
        }
        currentQuestionIndex++
        showQuestion()
    }

    private fun showFinalScoreDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Game over! Your score: $score/${questions.size}")
            .setCancelable(false)
            .setPositiveButton("Restart") { dialog, id ->
                restartGame()
            }
                .setNegativeButton("Exit") { dialog, id ->
            navigateToDashboard()
        }

        val alert = dialogBuilder.create()
        alert.setTitle("Game Over")
        alert.show()
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
                            val intent = Intent(this@FirstGameActivity, PremiumGamesActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@FirstGameActivity, GamesActivity::class.java)
                            startActivity(intent)
                        }
                        finish() // Finish current activity to prevent going back to it with back button
                    } else {
                        // Fallback in case the document does not exist or userType is not found
                        val intent = Intent(this@FirstGameActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Log.d("FirstGameActivity", "Error fetching user data", exception)
                    val intent = Intent(this@FirstGameActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        } else {
            // Fallback in case the user is not authenticated
            val intent = Intent(this@FirstGameActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}