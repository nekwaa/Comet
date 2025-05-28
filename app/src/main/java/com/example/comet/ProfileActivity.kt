package com.example.comet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText

class ProfileActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize bottom navigation
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Set up navigation listener using the new approach
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.navigation_activity -> {
                    startActivity(Intent(this, ActivityLogActivity::class.java))
                    true
                }
                R.id.navigation_messages -> {
                    startActivity(Intent(this, MessagesActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    // Already on profile
                    true
                }
                else -> false
            }
        }

        // Set profile as selected
        bottomNavigation.selectedItemId = R.id.navigation_profile

        // Set up profile data
        setupProfileData()

        // Set up click listeners
        setupClickListeners()
    }

    private fun setupProfileData() {
        // Set profile name and email
        findViewById<TextView>(R.id.tvName)?.text = "John Doe"
        findViewById<TextView>(R.id.tvEmail)?.text = "john.doe@example.com"
    }

    private fun setupClickListeners() {
        // Settings button
        findViewById<ImageView>(R.id.ivSettings)?.setOnClickListener {
            Toast.makeText(this, "Settings coming soon", Toast.LENGTH_SHORT).show()
        }

        // Edit profile button
        findViewById<Button>(R.id.btnEditProfile)?.setOnClickListener {
            showEditProfileDialog()
        }

        // Payment methods option
        findViewById<LinearLayout>(R.id.layoutPaymentMethods)?.setOnClickListener {
            Toast.makeText(this, "Payment methods coming soon", Toast.LENGTH_SHORT).show()
        }

        // Addresses option
        findViewById<LinearLayout>(R.id.layoutAddresses)?.setOnClickListener {
            Toast.makeText(this, "Addresses coming soon", Toast.LENGTH_SHORT).show()
        }

        // Notifications option
        findViewById<LinearLayout>(R.id.layoutNotifications)?.setOnClickListener {
            Toast.makeText(this, "Notifications coming soon", Toast.LENGTH_SHORT).show()
        }

        // Help center option
        findViewById<LinearLayout>(R.id.layoutHelp)?.setOnClickListener {
            Toast.makeText(this, "Help center coming soon", Toast.LENGTH_SHORT).show()
        }

        // Feedback option
        findViewById<LinearLayout>(R.id.layoutFeedback)?.setOnClickListener {
            Toast.makeText(this, "Feedback coming soon", Toast.LENGTH_SHORT).show()
        }

        // Logout button
        findViewById<Button>(R.id.btnLogout)?.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            // Go back to home screen
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.btnBecomeCometeer)?.setOnClickListener {
            // Navigate to CometeerActivity
            val intent = Intent(this, CometeerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile, null)
        val etEditName = dialogView.findViewById<EditText>(R.id.etEditName)
        val etEditEmail = dialogView.findViewById<EditText>(R.id.etEditEmail)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSaveProfile)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancelEditProfile)
        val ivEditProfilePic = dialogView.findViewById<ImageView>(R.id.ivEditProfilePic)

        // Pre-fill with current values
        etEditName.setText(findViewById<TextView>(R.id.tvName)?.text)
        etEditEmail.setText(findViewById<TextView>(R.id.tvEmail)?.text)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnSave.setOnClickListener {
            val newName = etEditName.text.toString().trim()
            val newEmail = etEditEmail.text.toString().trim()
            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Name and email cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                findViewById<TextView>(R.id.tvName)?.text = newName
                findViewById<TextView>(R.id.tvEmail)?.text = newEmail
                dialog.dismiss()
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
            }
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        // (Optional) Add logic for changing profile picture here
        dialog.show()
    }
}