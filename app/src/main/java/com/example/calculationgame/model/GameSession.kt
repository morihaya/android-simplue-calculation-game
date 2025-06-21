package com.example.calculationgame.model

import com.example.calculationgame.util.QuestionGenerator

class GameSession(difficulty: String) {
    private val questionGenerator = QuestionGenerator(difficulty)
    var score = 0
        private set
    var questionsAnswered = 0
        private set
    var correctAnswers = 0
        private set
    
    fun nextQuestion(): Question {
        return questionGenerator.generateQuestion()
    }
    
    fun submitAnswer(question: Question, userAnswer: Int): Boolean {
        questionsAnswered++
        val isCorrect = question.checkAnswer(userAnswer)
        
        if (isCorrect) {
            score += 10
            correctAnswers++
        }
        
        return isCorrect
    }
    
    fun getAccuracy(): Float {
        return if (questionsAnswered > 0) {
            correctAnswers.toFloat() / questionsAnswered.toFloat() * 100f
        } else {
            0f
        }
    }
}