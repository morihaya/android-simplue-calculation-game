package com.example.calculationgame.util

import com.example.calculationgame.model.Question
import kotlin.random.Random

class QuestionGenerator(private val difficulty: String) {
    
    fun generateQuestion(): Question {
        // 難易度に応じて数値の範囲を決定
        val range = when (difficulty) {
            "EASY" -> 10
            "MEDIUM" -> 50
            "HARD" -> 100
            else -> 10
        }
        
        // 演算子を選択
        val operations = when (difficulty) {
            "EASY" -> listOf("+", "-")
            "MEDIUM" -> listOf("+", "-", "*")
            "HARD" -> listOf("+", "-", "*", "/")
            else -> listOf("+", "-")
        }
        val operation = operations[Random.nextInt(operations.size)]
        
        // 数値を生成
        var firstNumber = Random.nextInt(range) + 1
        var secondNumber = Random.nextInt(range) + 1
        
        // 引き算の場合は答えが負にならないように調整
        if (operation == "-" && secondNumber > firstNumber) {
            val temp = firstNumber
            firstNumber = secondNumber
            secondNumber = temp
        }
        
        // 割り算の場合は割り切れる組み合わせにする
        if (operation == "/") {
            secondNumber = if (secondNumber == 0) 1 else secondNumber
            firstNumber = secondNumber * (Random.nextInt(10) + 1)
        }
        
        // 正解を計算
        val correctAnswer = when (operation) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "*" -> firstNumber * secondNumber
            "/" -> firstNumber / secondNumber
            else -> 0
        }
        
        return Question(firstNumber, secondNumber, operation, correctAnswer)
    }
}