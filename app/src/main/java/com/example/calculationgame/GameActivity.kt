package com.example.calculationgame

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculationgame.model.GameSession
import com.example.calculationgame.model.Question

class GameActivity : AppCompatActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var scoreTextView: TextView
    
    private lateinit var gameSession: GameSession
    private var currentQuestion: Question? = null
    private var gameTimeSeconds = 60 // 1分間のゲーム
    
    private lateinit var countDownTimer: CountDownTimer
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        
        // UIコンポーネントの初期化
        questionTextView = findViewById(R.id.questionTextView)
        answerEditText = findViewById(R.id.answerEditText)
        submitButton = findViewById(R.id.submitButton)
        timerTextView = findViewById(R.id.timerTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        
        // 難易度の取得
        val difficulty = getSharedPreferences("game_prefs", MODE_PRIVATE)
            .getString("difficulty", "MEDIUM") ?: "MEDIUM"
        
        // ゲームセッションの初期化
        gameSession = GameSession(difficulty)
        
        // 最初の問題を表示
        nextQuestion()
        
        // 回答送信ボタンのリスナー
        submitButton.setOnClickListener {
            checkAnswer()
        }
        
        // 数字キーパッドの設定
        setupNumericKeypad()
        
        // タイマーの開始
        startTimer()
    }
    
    private fun setupNumericKeypad() {
        // 数字ボタン (0-9)
        for (i in 0..9) {
            val buttonId = resources.getIdentifier("button$i", "id", packageName)
            findViewById<Button>(buttonId).setOnClickListener {
                appendToAnswer(i.toString())
            }
        }
        
        // クリアボタン
        findViewById<Button>(R.id.buttonClear).setOnClickListener {
            answerEditText.setText("")
        }
        
        // バックスペースボタン
        findViewById<Button>(R.id.buttonBackspace).setOnClickListener {
            val currentText = answerEditText.text.toString()
            if (currentText.isNotEmpty()) {
                answerEditText.setText(currentText.substring(0, currentText.length - 1))
                answerEditText.setSelection(answerEditText.text.length)
            }
        }
    }
    
    private fun appendToAnswer(digit: String) {
        val currentText = answerEditText.text.toString()
        answerEditText.setText(currentText + digit)
        answerEditText.setSelection(answerEditText.text.length)
    }
    
    private fun nextQuestion() {
        currentQuestion = gameSession.nextQuestion()
        questionTextView.text = currentQuestion?.getQuestionText()
        answerEditText.text.clear()
        answerEditText.requestFocus()
    }
    
    private fun checkAnswer() {
        val userAnswerStr = answerEditText.text.toString()
        if (userAnswerStr.isEmpty()) return
        
        val userAnswer = userAnswerStr.toIntOrNull() ?: return
        val currentQ = currentQuestion ?: return
        
        val isCorrect = gameSession.submitAnswer(currentQ, userAnswer)
        
        if (isCorrect) {
            scoreTextView.text = "スコア: ${gameSession.score}"
            Toast.makeText(this, "正解!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "不正解! 正解は ${currentQ.correctAnswer}", Toast.LENGTH_SHORT).show()
        }
        
        nextQuestion()
    }
    
    private fun startTimer() {
        countDownTimer = object : CountDownTimer(gameTimeSeconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerTextView.text = "残り時間: $secondsLeft 秒"
            }
            
            override fun onFinish() {
                endGame()
            }
        }.start()
    }
    
    private fun endGame() {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("SCORE", gameSession.score)
            putExtra("QUESTIONS_ANSWERED", gameSession.questionsAnswered)
            putExtra("ACCURACY", gameSession.getAccuracy())
        }
        startActivity(intent)
        finish()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}