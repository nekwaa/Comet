package com.example.comet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.Toast

class CometeerActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cometeer)

        // Initialize bottom navigation
        bottomNavigation = findViewById(R.id.bottomNavigation)
        
        // Set the menu for Cometeers
        bottomNavigation.menu.clear()
        bottomNavigation.inflateMenu(R.menu.bottom_navigation_cometeer)

        // Set up navigation listener
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_cometeer_home -> {
                    // Already on Cometeer home
                    true
                }
                R.id.navigation_cometeer_chat -> {
                    // Navigate to chat
                    Toast.makeText(this, "chat coming soon", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_cometeer_earnings -> {
                    // Navigate to Cometeer earnings
                    startActivity(Intent(this, CometeerEarningsActivity::class.java))
                    true
                }
                R.id.navigation_cometeer_profile -> {
                    // Navigate to Cometeer profile
                    Toast.makeText(this, "Cometeer profile coming soon", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        // Set home as selected
        bottomNavigation.selectedItemId = R.id.navigation_cometeer_home

        // Set up back button
        findViewById<ImageView>(R.id.ivBackButton)?.setOnClickListener {
            // Go back to regular profile
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        // Set up available jobs
        setupAvailableJobs()
    }

    private fun setupAvailableJobs() {
        // This would typically fetch data from a server
        // For now, we'll just set up some example jobs
        
        // Example of setting up a job card - in a real app, this would be done dynamically
        findViewById<TextView>(R.id.tvJobTitle1)?.text = "Deliver Package"
        findViewById<TextView>(R.id.tvJobDescription1)?.text = "Pick up package from location A and deliver to location B"
        findViewById<TextView>(R.id.tvJobPay1)?.text = "$15.00"
        findViewById<Button>(R.id.btnAcceptJob1)?.setOnClickListener {
            Toast.makeText(this, "Job accepted!", Toast.LENGTH_SHORT).show()
        }
        
        // You would add more job cards here or implement a RecyclerView for a dynamic list
    }
}