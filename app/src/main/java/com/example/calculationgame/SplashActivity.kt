package com.example.calculationgame

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calculationgame.util.ThemeManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // テーマを適用
        ThemeManager.applyTheme(this)
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        // タイトルのアニメーション
        val titleTextView = findViewById<TextView>(R.id.splashTitle)
        val subtitleTextView = findViewById<TextView>(R.id.splashSubtitle)
        
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 1000
        
        titleTextView.startAnimation(fadeIn)
        
        // サブタイトルは少し遅れて表示
        Handler(Looper.getMainLooper()).postDelayed({
            subtitleTextView.startAnimation(fadeIn)
        }, 500)
        
        // 2秒後にメイン画面に遷移
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}