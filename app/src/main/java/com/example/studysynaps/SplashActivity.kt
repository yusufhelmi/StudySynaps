package com.example.studysynaps

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val mainContainer = findViewById<ConstraintLayout>(R.id.main_container)
        val logo = findViewById<ImageView>(R.id.iv_logo)
        val appNameLayout = findViewById<LinearLayout>(R.id.layout_app_name)

        // Calculate DP to Pixels for consistent animation across devices
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels.toFloat()
        
        // 60dp shift for logo (move left)
        val shiftDistance = android.util.TypedValue.applyDimension(
            android.util.TypedValue.COMPLEX_UNIT_DIP, 
            100f,
            displayMetrics
        )

        // 1. Set Initial State IMMEDIATELY (Before first draw)
        // Logo starts off-screen at bottom
        logo.translationY = screenHeight / 2f + 200f // Push well below center
        
        // Text hidden and initially shifted slightly left to be behind logo
        appNameLayout.alpha = 0f
        appNameLayout.translationX = -shiftDistance / 2 // Start slightly tucked in

        // Background is already #1A2142 from XML

        // 2. Start Animation with a robust delay
        // We use a Handler to guarantee the delay happens AFTER layout is ready
        Handler(Looper.getMainLooper()).postDelayed({
            
            // Phase 1: Logo Up + Color Change
            val logoUp = ObjectAnimator.ofFloat(logo, "translationY", 0f)
            logoUp.duration = 1500
            logoUp.interpolator = DecelerateInterpolator()

            val colorAnim = ValueAnimator.ofObject(ArgbEvaluator(), Color.parseColor("#1A2142"), Color.WHITE)
            colorAnim.duration = 1500
            colorAnim.addUpdateListener { animator ->
                mainContainer.setBackgroundColor(animator.animatedValue as Int)
            }

            // Animate Logo Tint from White (Transparent tint) to Dark Blue
            // Since the logo resource is White, we need to tint it Dark Blue when background becomes White.
            // When background is Dark Blue, White logo is visible (No tint needed or White tint).
            
            // Note: If resource is White:
            // At start (Dark Blue BG): Tint = White (or null). Result = White.
            // At end (White BG): Tint = Dark Blue. Result = Dark Blue.
            
            val tintAnim = ValueAnimator.ofObject(ArgbEvaluator(), Color.WHITE, Color.parseColor("#1A2142"))
            tintAnim.duration = 1500
            tintAnim.addUpdateListener { animator ->
                logo.setColorFilter(animator.animatedValue as Int)
            }

            val phase1Set = AnimatorSet()
            phase1Set.playTogether(logoUp, colorAnim, tintAnim)

            // Phase 2: Spread (Logo Left, Text Fade In + Slide to natural pos)
            // Move logo LEFT by 'shiftDistance'
            val logoMoveLeft = ObjectAnimator.ofFloat(logo, "translationX", -shiftDistance)
            logoMoveLeft.duration = 800
            logoMoveLeft.interpolator = DecelerateInterpolator()

            val textFade = ObjectAnimator.ofFloat(appNameLayout, "alpha", 0f, 1f)
            textFade.duration = 800
            
            // Text moves from its tucked position to 0 (natural layout pos)
            val textSlide = ObjectAnimator.ofFloat(appNameLayout, "translationX", 0f)
            textSlide.duration = 800
            textSlide.interpolator = DecelerateInterpolator()

            val phase2Set = AnimatorSet()
            phase2Set.playTogether(logoMoveLeft, textFade, textSlide)

            // Master Sequence
            val masterSet = AnimatorSet()
            masterSet.playSequentially(phase1Set, phase2Set)
            masterSet.start()

            // Completion Listener
            masterSet.addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this@SplashActivity, IntroScreen1::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                    }, 500) // Hold for 0.5s
                }
            })

        }, 1000) // 1 second initial delay to ensure solid blue screen is seen
    }
}
