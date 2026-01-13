package com.example.studysynaps

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SuccessPresensiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_success_presensi)
        
        // Hide potential ugly status bars or match theme if needed, but white bg + default is fine.
        // Set Status Bar Icons to Black (Light Background)
        androidx.core.view.WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        // Display for 2 seconds then go Home
        Handler(Looper.getMainLooper()).postDelayed({
            // Go to Home (clearing stack above it if needed, or just new task)
            val intent = Intent(this, home::class.java)
            // If home is singleInstance or singleTop, this brings it to front. 
            // If we want to clear back stack so user can't "Back" to success screen:
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }, 2000)
    }
}
