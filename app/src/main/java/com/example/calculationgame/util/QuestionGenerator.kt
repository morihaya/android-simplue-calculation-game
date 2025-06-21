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
        
        // 選択肢を生成（正解を含む3つの選択肢）
        val choices = generateChoices(correctAnswer, operation, range)
        
        return Question(firstNumber, secondNumber, operation, correctAnswer, choices)
    }
    
    private fun generateChoices(correctAnswer: Int, operation: String, range: Int): List<Int> {
        val choices = mutableSetOf(correctAnswer)
        
        // 選択肢の差の範囲を決定
        val diffRange = when (operation) {
            "+" -> 1..5
            "-" -> 1..5
            "*" -> 1..3
            "/" -> 1..2
            else -> 1..5
        }
        
        // 正解に近い2つの選択肢を追加
        while (choices.size < 3) {
            val diff = Random.nextInt(diffRange.first, diffRange.last + 1) * if (Random.nextBoolean()) 1 else -1
            val wrongAnswer = correctAnswer + diff
            
            // 負の数や0は選択肢に含めない（割り算の場合）
            if (wrongAnswer > 0) {
                choices.add(wrongAnswer)
            }
        }
        
        // シャッフルして順番をランダムにする
        return choices.shuffled()
    }
}