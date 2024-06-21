package com.capstone.tomguard.register

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.capstone.tomguard.R
import com.capstone.tomguard.data.UserRepository
import com.capstone.tomguard.data.network.ApiConfig
import com.capstone.tomguard.data.Result
import com.capstone.tomguard.data.datastore.UserPreference
import com.capstone.tomguard.data.datastore.dataStore
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val apiService = ApiConfig.getApiService()
        val userPreference = UserPreference.getInstance(applicationContext.dataStore)
        val repository = UserRepository(apiService, userPreference)
        val viewModelFactory = RegisterViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)

        setupTextInputFocusChangeListener(R.id.nameEditText, R.id.nameEditTextLayout)
        setupTextInputFocusChangeListener(R.id.emailEditText, R.id.emailEditTextLayout)
        setupTextInputFocusChangeListener(R.id.passwordEditText, R.id.passwordEditTextLayout)
        setupTextInputFocusChangeListener(
            R.id.confirmPasswordEditText,
            R.id.confirmPasswordEditTextLayout
        )

        findViewById<Button>(R.id.registerButton).setOnClickListener {
            val name = findViewById<TextInputEditText>(R.id.nameEditText).text.toString()
            val email = findViewById<TextInputEditText>(R.id.emailEditText).text.toString()
            val password = findViewById<TextInputEditText>(R.id.passwordEditText).text.toString()
            val confirmPassword = findViewById<TextInputEditText>(R.id.confirmPasswordEditText).text.toString()

            if (password == confirmPassword) {
                if (isValidEmail(email)) {
                    viewModel.register(name, email, password).observe(this, Observer { response ->
                        when (response) {
                            is Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                showLoading(false)
                                showSuccessDialog(response.data.message)
                            }
                            is Result.Error -> {
                                showLoading(false)
                                showToast(response.error)
                            }
                        }
                    })
                } else {
                    showToast("Invalid email format")
                }
            } else {
                showToast("Passwords do not match")
            }
        }
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

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(?:\\.\\p{L}{2,})?\$"
        return email.matches(emailPattern.toRegex())
    }

    private fun showLoading(isLoading: Boolean) {
        findViewById<View>(R.id.progressBar).visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.success))
            setMessage(message)
            setPositiveButton(getString(R.string.continue_login)) { _, _ ->
                finish()
            }
            create()
            show()
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
