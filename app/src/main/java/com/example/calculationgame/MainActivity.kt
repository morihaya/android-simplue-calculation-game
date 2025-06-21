package com.example.calculationgame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.calculationgame.util.ThemeManager
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // テーマを適用
        ThemeManager.applyTheme(this)
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // ツールバーの設定
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        
        // カードのアニメーション
        animateCards()
        
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
        
        // ゲーム開始ボタン
        findViewById<Button>(R.id.startButton).setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            
            // ボタンのアニメーション効果
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .start()
                }
                .start()
        }
        
        // ハイスコアを表示
        updateHighScore()
    }
    
    private fun animateCards() {
        val titleCard = findViewById<MaterialCardView>(R.id.titleCard)
        val difficultyCard = findViewById<MaterialCardView>(R.id.difficultyCard)
        val scoreCard = findViewById<MaterialCardView>(R.id.scoreCard)
        val startButton = findViewById<Button>(R.id.startButton)
        
        // タイトルカードのアニメーション
        titleCard.alpha = 0f
        titleCard.translationY = -100f
        titleCard.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .start()
        
        // 難易度カードのアニメーション
        difficultyCard.alpha = 0f
        difficultyCard.translationX = -100f
        difficultyCard.animate()
            .alpha(1f)
            .translationX(0f)
            .setDuration(500)
            .setStartDelay(200)
            .start()
        
        // スコアカードのアニメーション
        scoreCard.alpha = 0f
        scoreCard.translationX = 100f
        scoreCard.animate()
            .alpha(1f)
            .translationX(0f)
            .setDuration(500)
            .setStartDelay(400)
            .start()
        
        // スタートボタンのアニメーション
        startButton.alpha = 0f
        startButton.scaleX = 0.5f
        startButton.scaleY = 0.5f
        startButton.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setStartDelay(600)
            .start()
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_theme_toggle -> {
                ThemeManager.toggleNightMode(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onResume() {
        super.onResume()
        // 画面に戻ってきたときにハイスコアを更新
        updateHighScore()
    }
    
    private fun updateHighScore() {
        val prefs = getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        val highScore = prefs.getInt("high_score", 0)
        val highScoreTextView = findViewById<TextView>(R.id.highScoreTextView)
        
        // 前の値と比較して変更があれば、アニメーションを適用
        val oldScore = highScoreTextView.text.toString().toIntOrNull() ?: 0
        if (highScore > oldScore) {
            val pulseAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
            highScoreTextView.startAnimation(pulseAnimation)
        }
        
        highScoreTextView.text = highScore.toString()
    }
}