package com.example.comet

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.SignInMethodQueryResult

class SignupActivity : AppCompatActivity() {

    private lateinit var etFullName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnSignup: Button
    private lateinit var tvLogin: TextView
    private lateinit var ivBackButton: ImageView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSignup = findViewById(R.id.btnSignup)
        tvLogin = findViewById(R.id.tvLogin)
        ivBackButton = findViewById(R.id.ivBackButton)

        // Set click listeners
        btnSignup.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (validateInputs(fullName, email, password, confirmPassword)) {
                performSignup(fullName, email, password)
            }
        }

        tvLogin.setOnClickListener {
            finish()
        }

        ivBackButton.setOnClickListener {
            finish()
        }
    }

    private fun validateInputs(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (fullName.isEmpty()) {
            etFullName.error = "Name cannot be empty"
            return false
        }

        if (email.isEmpty()) {
            etEmail.error = "Email cannot be empty"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Please enter a valid email"
            return false
        }

        if (password.isEmpty()) {
            etPassword.error = "Password cannot be empty"
            return false
        }

        if (!isPasswordStrong(password)) {
            etPassword.error = "Password must contain at least 8 characters, including uppercase, lowercase, number, and special character"
            return false
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.error = "Please confirm your password"
            return false
        }

        if (password != confirmPassword) {
            etConfirmPassword.error = "Passwords do not match"
            return false
        }

        return true
    }

    private fun isPasswordStrong(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$"
        return password.matches(passwordPattern.toRegex())
    }

    private fun performSignup(fullName: String, email: String, password: String) {
        btnSignup.isEnabled = false
        btnSignup.text = "SIGNING UP..."

        // Check if email already exists
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task: Task<SignInMethodQueryResult> ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods ?: emptyList<String>()
                    if (signInMethods.isNotEmpty()) {
                        // Email already exists
                        etEmail.error = "This email is already registered"
                        btnSignup.isEnabled = true
                        btnSignup.text = "SIGN UP"
                        return@addOnCompleteListener
                    }

                    // Create user with email and password
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { createTask: Task<AuthResult> ->
                            if (createTask.isSuccessful) {
                                // Update user profile with full name
                                val user = auth.currentUser
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName)
                                    .build()

                                user?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener { profileTask: Task<Void> ->
                                        if (profileTask.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "Account created successfully! Please login.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            finish()
                                        }
                                    }
                            } else {
                                // If sign up fails, display a message to the user.
                                Toast.makeText(
                                    this,
                                    "Registration failed: ${createTask.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                btnSignup.isEnabled = true
                                btnSignup.text = "SIGN UP"
                            }
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Error checking email: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnSignup.isEnabled = true
                    btnSignup.text = "SIGN UP"
                }
            }
    }
}