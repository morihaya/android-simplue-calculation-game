package com.example.calculationgame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // ゲーム開始ボタン
        findViewById<Button>(R.id.startButton).setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
        
        // 難易度選択
        findViewById<RadioGroup>(R.id.difficultyGroup).setOnCheckedChangeListener { _, checkedId ->
            val difficulty = when (checkedId) {
                R.id.easyRadio -> "EASY"
                R.id.mediumRadio -> "MEDIUM"
                R.id.hardRadio -> "HARD"
                else -> "MEDIUM"
            }
            // 難易度を保存
            getSharedPreferences("game_prefs", Context.MODE_PRIVATE).edit()
                .putString("difficulty", difficulty).apply()
        }
        
        // ハイスコアを表示
        updateHighScore()
    }
    
    override fun onResume() {
        super.onResume()
        // 画面に戻ってきたときにハイスコアを更新
        updateHighScore()
    }
    
    private fun updateHighScore() {
        val prefs = getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        val highScore = prefs.getInt("high_score", 0)
        findViewById<TextView>(R.id.highScoreTextView).text = highScore.toString()
    }
}