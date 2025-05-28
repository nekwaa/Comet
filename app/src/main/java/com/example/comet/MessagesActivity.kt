package com.example.comet

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MessagesActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var rvMessages: RecyclerView
    private lateinit var tvEmptyState: TextView
    private lateinit var etSearchMessages: EditText

    private var allConversations: List<Triple<String, String, String>> = listOf()
    private var allUnreadCounts: List<Int> = listOf()
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        // Initialize views
        bottomNavigation = findViewById(R.id.bottomNavigation)
        rvMessages = findViewById(R.id.rvMessages)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        etSearchMessages = findViewById(R.id.etSearchMessages)

        // Set up listeners
        bottomNavigation.setOnNavigationItemSelectedListener(this)

        // Set messages as selected
        bottomNavigation.selectedItemId = R.id.navigation_messages

        // Load messages data
        loadMessagesData()

        etSearchMessages.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterConversations(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadMessagesData() {
        // In a real app, this would come from a database or API
        val conversations = listOf(
            Triple("John Doe", "Hi there! I'm available to help with your plumbing issue tomorrow.", "2h ago"),
            Triple("Jane Smith", "Thank you for booking the cleaning service. See you on Friday!", "Yesterday"),
            Triple("Mike Johnson", "I've sent you a quote for the painting job.", "2d ago"),
            Triple("Sarah Williams", "Can we reschedule the tutoring session to next week?", "3d ago"),
            Triple("David Brown", "Your lawn looks great! Thanks for the review.", "1w ago")
        )
        allUnreadCounts = listOf(2, 0, 5, 1, 0) // Simulated unread counts
        allConversations = conversations // Store the original list

        if (allConversations.isEmpty()) {
            rvMessages.visibility = View.GONE
            tvEmptyState.visibility = View.VISIBLE
        } else {
            rvMessages.visibility = View.VISIBLE
            tvEmptyState.visibility = View.GONE

            // Set up the RecyclerView with a proper layout manager
            rvMessages.layoutManager = LinearLayoutManager(this)

            // Create and set the adapter
            // Initialize adapter with all conversations initially
            adapter = MessageAdapter(allConversations, allUnreadCounts) { position ->
                // When an item is clicked, we need to get the actual conversation data.
                // If the list is filtered, 'position' refers to the filtered list.
                // We use getCurrentList() from the adapter to get the currently displayed (possibly filtered) list.
                val clickedConversation = adapter.getCurrentList()[position]

                // Then, find this conversation in the original 'allConversations' list
                // to ensure we pass the correct, unfiltered data to ChatActivity.
                val originalIndex = allConversations.indexOfFirst {
                    it.first == clickedConversation.first && 
                    it.second == clickedConversation.second && 
                    it.third == clickedConversation.third
                }

                if (originalIndex != -1) {
                    val intent = Intent(this@MessagesActivity, ChatActivity::class.java)
                    // Pass the name from the original, unfiltered list to ensure consistency
                    intent.putExtra("CONTACT_NAME", allConversations[originalIndex].first)
                    startActivity(intent)
                } else {
                    // Handle case where conversation might not be found (should not happen if logic is correct)
                    Toast.makeText(this, "Error: Could not find conversation", Toast.LENGTH_SHORT).show()
                }
            }
            rvMessages.adapter = adapter

            Toast.makeText(this, "${allConversations.size} conversations loaded", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filterConversations(query: String) {
        val filteredList = if (query.isEmpty()) {
            allConversations
        } else {
            allConversations.filter {
                it.first.contains(query, ignoreCase = true) || // Search by name
                it.second.contains(query, ignoreCase = true)  // Search by last message
            }
        }

        // Create a corresponding list of unread counts for the filtered conversations
        val filteredUnreadCounts = filteredList.map {
            val originalIndex = allConversations.indexOf(it)
            if (originalIndex != -1) allUnreadCounts[originalIndex] else 0
        }

        adapter.updateData(filteredList, filteredUnreadCounts)

        if (filteredList.isEmpty()) {
            rvMessages.visibility = View.GONE
            tvEmptyState.visibility = View.VISIBLE
            tvEmptyState.text = "No conversations found for '$query'"
        } else {
            rvMessages.visibility = View.VISIBLE
            tvEmptyState.visibility = View.GONE
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                return true
            }
            R.id.navigation_activity -> {
                startActivity(Intent(this, ActivityLogActivity::class.java))
                return true
            }
            R.id.navigation_messages -> {
                // Already on messages, do nothing
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
}