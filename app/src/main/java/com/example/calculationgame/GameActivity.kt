package com.example.calculationgame

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculationgame.model.GameSession
import com.example.calculationgame.model.Question

class GameActivity : AppCompatActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var scoreTextView: TextView
    
    private lateinit var choice1Button: Button
    private lateinit var choice2Button: Button
    private lateinit var choice3Button: Button
    
    private lateinit var gameSession: GameSession
    private var currentQuestion: Question? = null
    private var gameTimeSeconds = 60 // 1分間のゲーム
    
    private lateinit var countDownTimer: CountDownTimer
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        
        // UIコンポーネントの初期化
        questionTextView = findViewById(R.id.questionTextView)
        timerTextView = findViewById(R.id.timerTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        
        choice1Button = findViewById(R.id.choice1Button)
        choice2Button = findViewById(R.id.choice2Button)
        choice3Button = findViewById(R.id.choice3Button)
        
        // 難易度の取得
        val difficulty = getSharedPreferences("game_prefs", MODE_PRIVATE)
            .getString("difficulty", "MEDIUM") ?: "MEDIUM"
        
        // ゲームセッションの初期化
        gameSession = GameSession(difficulty)
        
        // 最初の問題を表示
        nextQuestion()
        
        // 選択肢ボタンのリスナー設定
        setupChoiceButtons()
        
        // タイマーの開始
        startTimer()
    }
    
    private fun setupChoiceButtons() {
        choice1Button.setOnClickListener {
            checkAnswer(choice1Button.text.toString().toInt())
        }
        
        choice2Button.setOnClickListener {
            checkAnswer(choice2Button.text.toString().toInt())
        }
        
        choice3Button.setOnClickListener {
            checkAnswer(choice3Button.text.toString().toInt())
        }
    }
    
    private fun nextQuestion() {
        currentQuestion = gameSession.nextQuestion()
        questionTextView.text = currentQuestion?.getQuestionText()
        
        // 選択肢を設定
        val choices = currentQuestion?.choices ?: listOf(0, 0, 0)
        choice1Button.text = choices[0].toString()
        choice2Button.text = choices[1].toString()
        choice3Button.text = choices[2].toString()
    }
    
    private fun checkAnswer(userAnswer: Int) {
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