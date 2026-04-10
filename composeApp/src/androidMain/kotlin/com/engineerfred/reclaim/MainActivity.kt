package com.engineerfred.reclaim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.engineerfred.reclaim.app.ReclaimApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Let the Compose UI handle safe areas and insets naturally
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ReclaimApp()
        }
    }
}