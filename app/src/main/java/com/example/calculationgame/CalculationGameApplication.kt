package com.example.calculationgame

import android.app.Application
import com.example.calculationgame.util.ThemeManager

class CalculationGameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // アプリ起動時にテーマを適用
        ThemeManager.applyTheme(this)
    }
}