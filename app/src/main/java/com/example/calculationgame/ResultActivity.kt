package com.example.calculationgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calculationgame.util.ThemeManager

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // テーマを適用
        ThemeManager.applyTheme(this)
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        
        // スコアと統計情報の取得
        val score = intent.getIntExtra("SCORE", 0)
        val questionsAnswered = intent.getIntExtra("QUESTIONS_ANSWERED", 0)
        val accuracy = intent.getFloatExtra("ACCURACY", 0f)
        
        // UIコンポーネントの初期化
        val scoreTextView = findViewById<TextView>(R.id.finalScoreTextView)
        val statsTextView = findViewById<TextView>(R.id.statsTextView)
        val playAgainButton = findViewById<Button>(R.id.playAgainButton)
        val mainMenuButton = findViewById<Button>(R.id.mainMenuButton)
        
        // スコアと統計情報の表示
        scoreTextView.text = "最終スコア: $score"
        statsTextView.text = "解答数: $questionsAnswered\n正答率: ${String.format("%.1f", accuracy)}%"
        
        // ハイスコアの更新
        val prefs = getSharedPreferences("game_prefs", MODE_PRIVATE)
        val highScore = prefs.getInt("high_score", 0)
        
        if (score > highScore) {
            prefs.edit().putInt("high_score", score).apply()
            findViewById<TextView>(R.id.newHighScoreTextView).visibility = android.view.View.VISIBLE
        }
        
        // もう一度プレイボタン
        playAgainButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        // メインメニューボタン
        mainMenuButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}