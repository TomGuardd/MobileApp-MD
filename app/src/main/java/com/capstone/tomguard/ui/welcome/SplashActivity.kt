@file:Suppress("DEPRECATION")

package com.capstone.tomguard.ui.welcome

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.capstone.tomguard.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
    }

    private fun playAnimation() {
        val startY = binding.images.translationY
        val endY = startY + 100f

        val animator =
            ObjectAnimator.ofFloat(binding.images, View.TRANSLATION_Y, startY, endY).apply {
                duration = 3000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
                interpolator = AccelerateDecelerateInterpolator()
            }


        animator.start()

        val splashScreenTime = 3000L
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }, splashScreenTime)
    }
}
