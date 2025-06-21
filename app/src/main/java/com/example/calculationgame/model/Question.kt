package com.example.calculationgame.model

data class Question(
    val firstNumber: Int,
    val secondNumber: Int,
    val operation: String, // "+", "-", "*", "/"
    val correctAnswer: Int
) {
    fun getQuestionText(): String {
        return "$firstNumber $operation $secondNumber = ?"
    }
    
    fun checkAnswer(userAnswer: Int): Boolean {
        return userAnswer == correctAnswer
    }
}