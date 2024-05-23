package com.translator.wordwanderer.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.translator.wordwanderer.R

data class Question(val imageResId: Int, val questionText: String, val choices: List<String>, val correctAnswer: String)

class FirstGameActivity : AppCompatActivity() {

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
        Question(R.drawable.philippines, "", listOf("Philippines", "Japan", "Brazil", "Korea"), "Philippines"),
        Question(R.drawable.india, "", listOf("Korea", "Australia", "India", "Vietnam"), "India"),
        Question(R.drawable.southkorea, "", listOf("South Korea", "Germany", "Argentina", "North Korea"), "South Korea"),
        Question(R.drawable.japan, "", listOf("Japan", "China", "Thailand", "Vietnam"), "Japan"),
        Question(R.drawable.australia, "", listOf("Australia", "New Zealand", "Fiji", "Samoa"), "Australia"),
        Question(R.drawable.brazil, "", listOf("Brazil", "Argentina", "Chile", "Peru"), "Brazil"),
        Question(R.drawable.chad, "", listOf("Chad", "Niger", "Burkina Faso", "Mali"), "Chad"),
        Question(R.drawable.turkmenistan, "", listOf("Turkmenistan", "Kazakhstan", "Uzbekistan", "Tajikistan"), "Turkmenistan"),
        Question(R.drawable.kiribati, "", listOf("Kiribati", "Tuvalu", "Marshall Islands", "Micronesia"), "Kiribati"),
        Question(R.drawable.seychelles, "", listOf("Seychelles", "Mauritius", "Comoros", "Maldives"), "Seychelles")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_game)

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

        restartGame()
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
                // Navigate back to the games dashboard
                val intent = Intent(this@FirstGameActivity, GamesActivity::class.java)
                startActivity(intent)
                finish()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Game Over")
        alert.show()
    }

}