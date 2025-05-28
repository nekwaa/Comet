package com.example.comet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth // Added FirebaseAuth import

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // Added FirebaseAuth instance

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth

        if (auth.currentUser == null) {
            // Not signed in, launch LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Finish MainActivity so user can't go back to it
            return // Return to prevent further execution of onCreate for non-authenticated user
        }

        // User is signed in, proceed to set up the main activity content
        setContentView(R.layout.activity_main)

        // Initialize UI elements (These might need to be removed or changed if R.layout.activity_main is not for authenticated users)
        val loginButton = findViewById<Button>(R.id.btnLogin) // This button might not exist or be relevant if user is logged in
        val signupButton = findViewById<Button>(R.id.btnSignup) // This button might not exist or be relevant if user is logged in

        // Set click listeners (These might need to be removed or changed)
        loginButton.setOnClickListener { // This listener might not be needed
            // Navigate to Login Activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signupButton.setOnClickListener { // This listener might not be needed
            // Navigate to Signup Activity
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}