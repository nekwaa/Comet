package com.example.comet

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.Toast

class ActivityLogActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var rvActivity: RecyclerView
    private lateinit var tvEmptyState: TextView
    private lateinit var etSearchActivity: EditText

    private lateinit var allTransactions: List<TransactionModel>
    private var activityAdapter: ActivityAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        // Initialize views
        bottomNavigation = findViewById(R.id.bottomNavigation)
        rvActivity = findViewById(R.id.rvActivity)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        etSearchActivity = findViewById(R.id.etSearchActivity)

        // Set up listeners
        bottomNavigation.setOnNavigationItemSelectedListener(this)

        // Set activity as selected
        bottomNavigation.selectedItemId = R.id.navigation_activity

        // Load activity data
        loadActivityData()

        // Setup search
        etSearchActivity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterTransactions(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun loadActivityData() {
        // In a real app, this would come from a database or API
        val unsortedTransactions = listOf(
            TransactionModel(
                id = "1",
                cometeerName = "John Smith",
                cometeerImageUrl = "",
                tags = listOf("plumbing", "pipes", "leak repair"),
                serviceType = "Plumbing Service",
                dateTime = "May 15, 2023 - 2:30 PM",
                cost = 85.00,
                status = TransactionStatus.COMPLETED
            ),
            TransactionModel(
                id = "2",
                cometeerName = "Emily Wilson",
                cometeerImageUrl = "",
                tags = listOf("cleaning", "housekeeping", "deep clean"),
                serviceType = "Cleaning Service",
                dateTime = "May 20, 2023 - 10:00 AM",
                cost = 120.00,
                status = TransactionStatus.ONGOING
            ),
            TransactionModel(
                id = "3",
                cometeerName = "David Brown",
                cometeerImageUrl = "",
                tags = listOf("gardening", "lawn care", "mowing"),
                serviceType = "Lawn Mowing",
                dateTime = "May 10, 2023 - 4:00 PM",
                cost = 45.00,
                status = TransactionStatus.COMPLETED
            ),
            TransactionModel(
                id = "4",
                cometeerName = "Sarah Williams",
                cometeerImageUrl = "",
                tags = listOf("education", "math", "science"),
                serviceType = "Tutoring",
                dateTime = "May 5, 2023 - 6:30 PM",
                cost = 60.00,
                status = TransactionStatus.COMPLETED
            ),
            TransactionModel(
                id = "5",
                cometeerName = "Mike Davis",
                cometeerImageUrl = "",
                tags = listOf("plumbing", "drain cleaning", "emergency"),
                serviceType = "Emergency Plumbing",
                dateTime = "May 22, 2023 - 11:00 AM",
                cost = 150.00,
                status = TransactionStatus.ONGOING
            )
        )

        allTransactions = unsortedTransactions.sortedWith(compareBy {
            when (it.status) {
                TransactionStatus.ONGOING -> 0  // Lower number = higher priority
                TransactionStatus.COMPLETED -> 1
            }
        })

        if (allTransactions.isEmpty()) {
            rvActivity.visibility = View.GONE
            tvEmptyState.visibility = View.VISIBLE
            tvEmptyState.text = "No transactions yet"
        } else {
            rvActivity.visibility = View.VISIBLE
            tvEmptyState.visibility = View.GONE

            // Set up the RecyclerView with a proper layout manager
            rvActivity.layoutManager = LinearLayoutManager(this)

            // Create and set the adapter
            activityAdapter = ActivityAdapter(
                transactions = allTransactions.toMutableList(), // Pass a mutable list
                onCancelClick = { transaction ->
                    // Handle cancel click
                    // Show confirmation dialog
                    Toast.makeText(this, "Cancel service with ${transaction.cometeerName}", Toast.LENGTH_SHORT).show()
                },
                onReviewSubmitted = { review ->
                    // Handle review submission
                    Toast.makeText(this, "Review submitted for ${review.cometeerName}", Toast.LENGTH_SHORT).show()
                },
                onReviewDeleted = { transactionId ->
                    // Handle review deletion
                    Toast.makeText(this, "Review deleted", Toast.LENGTH_SHORT).show()
                }
            )
            rvActivity.adapter = activityAdapter
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                return true
            }
            R.id.navigation_activity -> {
                // Already on activity, do nothing
                return true
            }
            R.id.navigation_messages -> {
                // Navigate to messages
                startActivity(Intent(this, MessagesActivity::class.java))
                return true
            }
            R.id.navigation_profile -> {
                // Navigate to profile
                startActivity(Intent(this, ProfileActivity::class.java))
                return true
            }
        }
        return false
    }

    private fun filterTransactions(query: String) {
        val filteredList = if (query.isEmpty()) {
            allTransactions
        } else {
            allTransactions.filter {
                it.tags.any { tag -> tag.contains(query, ignoreCase = true) } ||
                it.cometeerName.contains(query, ignoreCase = true) ||
                it.serviceType.contains(query, ignoreCase = true)
            }
        }

        activityAdapter?.updateData(filteredList)

        if (filteredList.isEmpty()) {
            rvActivity.visibility = View.GONE
            tvEmptyState.visibility = View.VISIBLE
            tvEmptyState.text = "No results found for \"$query\""
        } else {
            rvActivity.visibility = View.VISIBLE
            tvEmptyState.visibility = View.GONE
        }
    }
}