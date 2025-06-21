package com.example.calculationgame

import android.app.Application
import android.app.Activity
import android.os.Bundle
import com.example.calculationgame.util.ThemeManager

class CalculationGameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // アプリ起動時にテーマを適用
        ThemeManager.applyTheme(this)
        
        // アクティビティ遷移のアニメーションを設定
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // アクティビティ遷移のアニメーションを設定
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            
            override fun onActivityDestroyed(activity: Activity) {
                // アクティビティ終了時のアニメーションを設定
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        })
    }
}