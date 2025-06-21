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
import com.example.calculationgame.util.ThemeManager
import com.google.android.material.progressindicator.LinearProgressIndicator

class GameActivity : AppCompatActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var timerProgressBar: LinearProgressIndicator
    
    private lateinit var choice1Button: Button
    private lateinit var choice2Button: Button
    private lateinit var choice3Button: Button
    
    private lateinit var gameSession: GameSession
    private var currentQuestion: Question? = null
    private var gameTimeSeconds = 60 // 1分間のゲーム
    
    private lateinit var countDownTimer: CountDownTimer
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // テーマを適用
        ThemeManager.applyTheme(this)
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        
        // UIコンポーネントの初期化
        questionTextView = findViewById(R.id.questionTextView)
        timerTextView = findViewById(R.id.timerTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        timerProgressBar = findViewById(R.id.timerProgressBar)
        
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
        val clickListener = { button: Button ->
            // ボタンをタップしたときのアニメーション効果
            button.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction {
                    button.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .withEndAction {
                            // アニメーション完了後に回答をチェック
                            checkAnswer(button.text.toString().toInt())
                        }
                        .start()
                }
                .start()
        }
        
        choice1Button.setOnClickListener { clickListener(choice1Button) }
        choice2Button.setOnClickListener { clickListener(choice2Button) }
        choice3Button.setOnClickListener { clickListener(choice3Button) }
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
            // 正解時のアニメーション効果
            showCorrectAnswerFeedback()
        } else {
            // 不正解時のアニメーション効果
            showIncorrectAnswerFeedback(currentQ.correctAnswer)
        }
        
        nextQuestion()
    }
    
    private fun showCorrectAnswerFeedback() {
        // 正解時のトースト表示
        Toast.makeText(this, "正解!", Toast.LENGTH_SHORT).show()
        
        // スコア表示のアニメーション
        scoreTextView.animate()
            .scaleX(1.5f)
            .scaleY(1.5f)
            .setDuration(200)
            .withEndAction {
                scoreTextView.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(200)
                    .start()
            }
            .start()
            
        // 問題カード全体を一瞬緑色に
        val questionCard = findViewById<com.google.android.material.card.MaterialCardView>(R.id.questionCard)
        val originalCardColor = questionCard.cardBackgroundColor.defaultColor
        questionCard.setCardBackgroundColor(getColor(R.color.correct_answer))
        questionCard.postDelayed({
            questionCard.setCardBackgroundColor(originalCardColor)
        }, 300)
    }
    
    private fun showIncorrectAnswerFeedback(correctAnswer: Int) {
        // 不正解時のトースト表示
        Toast.makeText(this, "不正解! 正解は $correctAnswer", Toast.LENGTH_SHORT).show()
        
        // 問題カード全体を一瞬赤色に
        val questionCard = findViewById<com.google.android.material.card.MaterialCardView>(R.id.questionCard)
        val originalCardColor = questionCard.cardBackgroundColor.defaultColor
        questionCard.setCardBackgroundColor(getColor(R.color.wrong_answer))
        questionCard.postDelayed({
            questionCard.setCardBackgroundColor(originalCardColor)
        }, 300)
    }
    
    private fun startTimer() {
        // プログレスバーの初期設定
        timerProgressBar.max = gameTimeSeconds
        timerProgressBar.progress = gameTimeSeconds
        
        countDownTimer = object : CountDownTimer(gameTimeSeconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerTextView.text = "残り時間: $secondsLeft 秒"
                
                // プログレスバーを更新
                timerProgressBar.progress = secondsLeft.toInt()
                
                // 残り時間が少なくなったら警告色に変更
                if (secondsLeft <= 10) {
                    timerProgressBar.setIndicatorColor(getColor(R.color.wrong_answer))
                }
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