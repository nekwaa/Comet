package com.example.comet

import android.content.Intent // Added import for Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvSignUp: TextView
    private lateinit var ivBackButton: ImageView
    private lateinit var tvForgotPassword: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvSignUp = findViewById(R.id.tvSignUp)
        ivBackButton = findViewById(R.id.ivBackButton)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)

        // Set click listeners
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateInputs(email, password)) {
                performLogin(email, password)
            }
        }

        tvSignUp.setOnClickListener {
            // Navigate to SignupActivity
            startActivity(Intent(this, SignupActivity::class.java))
        }

        ivBackButton.setOnClickListener {
            finish()
        }

        tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
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

        return true
    }

    private fun performLogin(email: String, password: String) {
        btnLogin.isEnabled = false
        btnLogin.text = "LOGGING IN..."

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(
                        this,
                        "Login successful!",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Navigate to main activity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // Finish LoginActivity so user can't go back to it with back button
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        this,
                        "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                btnLogin.isEnabled = true
                btnLogin.text = "LOGIN"
            }
    }

    private fun showForgotPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
        val etResetEmail = dialogView.findViewById<TextInputEditText>(R.id.etResetEmail)

        builder.setView(dialogView)
            .setTitle("Reset Password")
            .setPositiveButton("Send") { dialog, _ ->
                val email = etResetEmail.text.toString().trim()
                if (email.isEmpty()) {
                    etResetEmail.error = "Please enter your email"
                    return@setPositiveButton
                }
                sendPasswordResetEmail(email)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        builder.create().show()
    }

    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task: Task<Void> ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Password reset email sent!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Failed to send reset email: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}