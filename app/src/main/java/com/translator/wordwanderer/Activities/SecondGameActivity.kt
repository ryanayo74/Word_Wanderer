package com.translator.wordwanderer.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.translator.wordwanderer.R
import pl.droidsonroids.gif.GifImageView
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SecondGameActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var imageView: ImageView
    private lateinit var answerEditText: EditText
    private lateinit var submitAnswerButton: Button
    private lateinit var scoreTextView: TextView
    private lateinit var gifImageView: GifImageView
    private lateinit var anotherGifImageView: GifImageView

    private var currentQuestionIndex = 0
    private var score = 0

    private val questions = mutableListOf(
        SecondGameQuestions(R.drawable.eritrea, "", "Eritrea"),
        SecondGameQuestions(R.drawable.belarus, "", "Belarus"),
        SecondGameQuestions(R.drawable.tajikistan, "", "Tajikistan"),
        SecondGameQuestions(R.drawable.comoros, "", "Comoros"),
        SecondGameQuestions(R.drawable.suriname, "", "Suriname"),
        SecondGameQuestions(R.drawable.guinea_bissau, "", "Guinea-Bissau"),
        SecondGameQuestions(R.drawable.vanuatu, "", "Vanuatu"),
        SecondGameQuestions(R.drawable.bhutan, "", "Bhutan"),
        SecondGameQuestions(R.drawable.lesotho, "", "Lesotho"),
        SecondGameQuestions(R.drawable.micronesia, "", "Micronesia")
    )

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_game)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        imageView = findViewById(R.id.guestimageView)
        answerEditText = findViewById(R.id.answerEditText)
        submitAnswerButton = findViewById(R.id.submitAnswerButton)
        scoreTextView = findViewById(R.id.score)
        gifImageView = findViewById(R.id.gifImageView)
        anotherGifImageView = findViewById(R.id.anotherGifImageView)

        submitAnswerButton.setOnClickListener { checkAnswer() }

        // Initialize MediaPlayer and start playing background music
        mediaPlayer = MediaPlayer.create(this, R.raw.game2)
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
        questions.shuffle()
        currentQuestionIndex = 0
        score = 0
        gifImageView.visibility = ImageView.GONE
        anotherGifImageView.visibility = ImageView.GONE
        showQuestion()
    }

    private fun showQuestion() {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            imageView.setImageResource(question.imageResId)
            scoreTextView.text = "$score/${questions.size}"
            answerEditText.text.clear()

            // Show or hide GIFs based on the current question index and score
            if (currentQuestionIndex >= 5) {
                if (score == 0) {
                    gifImageView.visibility = ImageView.VISIBLE
                    anotherGifImageView.visibility = ImageView.GONE
                } else {
                    gifImageView.visibility = ImageView.GONE
                    anotherGifImageView.visibility = ImageView.VISIBLE
                }
            } else {
                gifImageView.visibility = ImageView.GONE
                anotherGifImageView.visibility = ImageView.GONE
            }

        } else {
            showFinalScoreDialog()
        }
    }

    private fun checkAnswer() {
        val selectedAnswer = answerEditText.text.toString().trim()
        val correctAnswer = questions[currentQuestionIndex].correctAnswer
        if (selectedAnswer.equals(correctAnswer, ignoreCase = true)) {
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
                            val intent = Intent(this@SecondGameActivity, PremiumGamesActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@SecondGameActivity, GamesActivity::class.java)
                            startActivity(intent)
                        }
                        finish() // Finish current activity to prevent going back to it with back button
                    } else {
                        // Fallback in case the document does not exist or userType is not found
                        val intent = Intent(this@SecondGameActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Log.d("FirstGameActivity", "Error fetching user data", exception)
                    val intent = Intent(this@SecondGameActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        } else {
            // Fallback in case the user is not authenticated
            val intent = Intent(this@SecondGameActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}