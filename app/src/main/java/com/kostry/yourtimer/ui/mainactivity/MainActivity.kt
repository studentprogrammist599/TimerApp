package com.kostry.yourtimer.ui.mainactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.kostry.yourtimer.R
import com.kostry.yourtimer.service.TimerService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ContextCompat.startForegroundService(
            this,
            TimerService.newIntent(this, 25)
        )
    }
}