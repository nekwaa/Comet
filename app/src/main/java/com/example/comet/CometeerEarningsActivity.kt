package com.example.comet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CometeerEarningsActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cometeer_earnings)

        // Initialize bottom navigation
        bottomNavigation = findViewById(R.id.bottomNavigation)
        
        // Set the menu for Cometeers
        bottomNavigation.menu.clear()
        bottomNavigation.inflateMenu(R.menu.bottom_navigation_cometeer)

        // Set up navigation listener
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_cometeer_home -> {
                    // Navigate to Cometeer home
                    startActivity(Intent(this, CometeerActivity::class.java))
                    finish()
                    true
                }
                R.id.navigation_cometeer_chat -> {
                    // Navigate to chat
                    // startActivity(Intent(this, ChatActivity::class.java))
                    // finish()
                    true
                }
                R.id.navigation_cometeer_earnings -> {
                    // Already on earnings page
                    true
                }
                R.id.navigation_cometeer_profile -> {
                    // Navigate to Cometeer profile
                    // startActivity(Intent(this, CometeerProfileActivity::class.java))
                    // finish()
                    true
                }
                else -> false
            }
        }

        // Set earnings as selected
        bottomNavigation.selectedItemId = R.id.navigation_cometeer_earnings

        // Set up withdraw button
        findViewById<Button>(R.id.btnWithdraw).setOnClickListener {
            // Show withdraw dialog or navigate to withdraw screen
            // For now, just show a toast
            android.widget.Toast.makeText(this, "Withdraw functionality coming soon", android.widget.Toast.LENGTH_SHORT).show()
        }

        // Set up view all transactions
        findViewById<TextView>(R.id.tvViewAllTransactions).setOnClickListener {
            // Navigate to all transactions screen
            android.widget.Toast.makeText(this, "All transactions view coming soon", android.widget.Toast.LENGTH_SHORT).show()
        }

        // Set up view all withdrawals
        findViewById<TextView>(R.id.tvViewAllWithdrawals).setOnClickListener {
            // Navigate to all withdrawals screen
            android.widget.Toast.makeText(this, "All withdrawals view coming soon", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}