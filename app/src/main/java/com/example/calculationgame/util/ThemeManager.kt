package com.example.calculationgame.util

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

/**
 * テーマ（ライト/ダーク）を管理するユーティリティクラス
 */
object ThemeManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_NIGHT_MODE = "night_mode"

    /**
     * 現在のテーマモードを取得
     */
    fun getNightMode(context: Context): Int {
        val prefs = getPrefs(context)
        return prefs.getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    /**
     * テーマモードを設定
     */
    fun setNightMode(context: Context, mode: Int) {
        val prefs = getPrefs(context)
        prefs.edit().putInt(KEY_NIGHT_MODE, mode).apply()
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    /**
     * テーマモードを切り替え
     */
    fun toggleNightMode(activity: AppCompatActivity) {
        val currentMode = getNightMode(activity)
        val newMode = if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.MODE_NIGHT_NO
        } else {
            AppCompatDelegate.MODE_NIGHT_YES
        }
        
        setNightMode(activity, newMode)
        activity.recreate() // アクティビティを再作成してテーマを適用
    }

    /**
     * アプリ起動時にテーマを適用
     */
    fun applyTheme(context: Context) {
        val nightMode = getNightMode(context)
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
}