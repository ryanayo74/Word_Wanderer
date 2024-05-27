package com.translator.wordwanderer.activities

data class FirstGameQuestions(val imageResId: Int, val questionText: String, val choices: List<String>, val correctAnswer: String)

data class SecondGameQuestions(val imageResId: Int, val questionText: String, val correctAnswer: String)

data class ThirdGameQuestions(
    val questionText: String,
    val choices: List<String>,
    val correctAnswer: String
)

data class FourthGameQuestions(
    val questionText: String,
    val choices: List<String>,
    val correctAnswer: String
)
