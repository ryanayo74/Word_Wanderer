package com.translator.wordwanderer.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.translator.wordwanderer.R

class FourthGameActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var questionTextView: TextView
    private lateinit var firstChoice: Button
    private lateinit var secondChoice: Button
    private lateinit var thirdChoice: Button
    private lateinit var fourthChoice: Button
    private lateinit var scoreTextView: TextView

    private var currentQuestionIndex = 0
    private var score = 0

    private val questions = mutableListOf(
        FourthGameQuestions("The ___________ of Canada ___________ a rich cultural heritage.",
            listOf("Culture, has", "Society, have", "Nation, has", "Country, have"), "Culture, has"),

        FourthGameQuestions("The ___________ of France ___________ renowned for its cuisine and art.",
            listOf("City, have", "People, has", "Region, have", "Country, has"), "Country, has"),

        FourthGameQuestions("The ___________ of Japan ___________ famous for its technological advancements.",
            listOf("Island, have", "Nation, has", "Land, have", "Country, has"), "Nation, has"),

        FourthGameQuestions("The ___________ of Brazil ___________ home to the Amazon rainforest.",
            listOf("Region, have", "Country, has", "Land, have", "Nation, has"), "Country, has"),

        FourthGameQuestions("The ___________ of Egypt ___________ the Pyramids of Giza.",
            listOf("History, have", "Civilization, has", "Country, has", "Land, have"), "Country, has"),

        FourthGameQuestions("The ___________ of Germany ___________ a significant role in European history.",
            listOf("Nation, has", "Region, have", "Country, has", "Land, have"), "Country, has"),

        FourthGameQuestions("The ___________ of Italy ___________ known for its rich historical sites.",
            listOf("City, have", "Country, has", "People, have", "Region, has"), "Country, has"),

        FourthGameQuestions("The ___________ of India ___________ diverse cultures and languages.",
            listOf("Country, has", "Nation, have", "Region, has", "Land, have"), "Country, has"),

        FourthGameQuestions("The ___________ of Australia ___________ unique wildlife and natural wonders.",
            listOf("Continent, has", "Country, has", "Land, have", "Nation, has"), "Country, has"),

        FourthGameQuestions("The ___________ of Russia ___________ the largest land area in the world.",
            listOf("Nation, has", "Region, have", "Country, has", "Territory, have"), "Country, has")
    )

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth_game)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        questionTextView = findViewById(R.id.questionTextView)
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
            questionTextView.text = question.questionText
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
                            val intent = Intent(this@FourthGameActivity, PremiumGamesActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@FourthGameActivity, GamesActivity::class.java)
                            startActivity(intent)
                        }
                        finish() // Finish current activity to prevent going back to it with back button
                    } else {
                        // Fallback in case the document does not exist or userType is not found
                        val intent = Intent(this@FourthGameActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Log.d("FirstGameActivity", "Error fetching user data", exception)
                    val intent = Intent(this@FourthGameActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        } else {
            // Fallback in case the user is not authenticated
            val intent = Intent(this@FourthGameActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}