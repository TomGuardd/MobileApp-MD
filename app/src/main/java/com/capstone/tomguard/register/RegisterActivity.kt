@file:Suppress("DEPRECATION")

package com.capstone.tomguard.register

import android.graphics.Color
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.capstone.tomguard.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private val slideInBottomAnimation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)
    }

    private val slideOutTopAnimation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.slide_out_top)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        setupTextInputFocusChangeListener(R.id.nameEditText, R.id.nameEditTextLayout)
        setupTextInputFocusChangeListener(R.id.emailEditText, R.id.emailEditTextLayout)
        setupTextInputFocusChangeListener(R.id.passwordEditText, R.id.passwordEditTextLayout)
        setupTextInputFocusChangeListener(
            R.id.confirmPasswordEditText,
            R.id.confirmPasswordEditTextLayout
        )
    }

    private fun setupTextInputFocusChangeListener(editTextId: Int, textInputLayoutId: Int) {
        val editText = findViewById<TextInputEditText>(editTextId)
        val textInputLayout = findViewById<TextInputLayout>(textInputLayoutId)

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                textInputLayout.boxStrokeColor = Color.parseColor("#FFA500")
            } else {
                textInputLayout.boxStrokeColor = Color.WHITE
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishWithSlideOutTopAnimation()
    }

    private fun finishWithSlideOutTopAnimation() {
        overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_top)
    }
}
