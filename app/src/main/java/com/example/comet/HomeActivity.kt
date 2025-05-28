package com.example.comet

import android.widget.TextView
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher


class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavigation: BottomNavigationView
    // private lateinit var cardSearch: CardView // Replaced by EditText and RecyclerView
    private lateinit var etServiceSearch: EditText
    private lateinit var rvServiceSearchResults: RecyclerView
    private lateinit var tvNoServiceResults: TextView
    private lateinit var serviceSearchAdapter: ServiceSearchAdapter
    private val allServices = mutableListOf<ServiceSearchModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize views
        bottomNavigation = findViewById(R.id.bottomNavigation)
        etServiceSearch = findViewById(R.id.etServiceSearch)
        rvServiceSearchResults = findViewById(R.id.rvServiceSearchResults)
        tvNoServiceResults = findViewById(R.id.tvNoServiceResults)

        // Set up listeners
        bottomNavigation.setOnNavigationItemSelectedListener(this)

        // Set home as selected
        bottomNavigation.selectedItemId = R.id.navigation_home

        // Prepare mock services data
        prepareMockServices()

        // Setup search RecyclerView
        setupServiceSearchRecyclerView()

        // Set up search EditText
        etServiceSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterServices(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Old search card click listener - can be removed or repurposed if needed
        // findViewById<CardView>(R.id.cardSearch).setOnClickListener {
            // Navigate to search screen
            // Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
        // } // This closing brace was causing issues

        findViewById<TextView>(R.id.tvViewAllCategories).setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.tvViewAllTransactions).setOnClickListener {
            val intent = Intent(this, ActivityLogActivity::class.java)
            startActivity(intent)
        }

        // Load data for recycler views
        setupCategoriesRecyclerView()
        // setupRecentTransactionsRecyclerView() // This was a duplicate call, original is further down
        setupRecommendedRecyclerView()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                // Already on home, do nothing
                return true
            }
            R.id.navigation_activity -> {
                startActivity(Intent(this, ActivityLogActivity::class.java))
                finish()
                return true
            }
            R.id.navigation_messages -> {
                // Navigate to messages
                startActivity(Intent(this, MessagesActivity::class.java))
                finish()
                return true
            }
            R.id.navigation_profile -> {
                // Navigate to profile
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
                return true
            }
        }
        return false
    }

    private fun prepareMockServices() {
        allServices.add(ServiceSearchModel("1", "Plumbing Repairs", 5, "Home Repairs"))
        allServices.add(ServiceSearchModel("2", "House Cleaning", 10, "Cleaning"))
        allServices.add(ServiceSearchModel("3", "Electrical Wiring", 3, "Home Repairs"))
        allServices.add(ServiceSearchModel("4", "Lawn Mowing", 7, "Gardening"))
        allServices.add(ServiceSearchModel("5", "Interior Painting", 4, "Home Improvement"))
        allServices.add(ServiceSearchModel("6", "Appliance Installation", 2, "Home Repairs"))
        allServices.add(ServiceSearchModel("7", "Deep Cleaning Services", 8, "Cleaning"))
        allServices.add(ServiceSearchModel("8", "Garden Weeding", 6, "Gardening"))
        allServices.add(ServiceSearchModel("9", "Plumbing Installation", 3, "Home Repairs"))
    }

    private fun setupServiceSearchRecyclerView() {
        serviceSearchAdapter = ServiceSearchAdapter(mutableListOf()) { service ->
            val intent = Intent(this, RadiusMatchingActivity::class.java)
            intent.putExtra("SERVICE_NAME", service.name) // Pass service name or ID
            intent.putExtra("CATEGORY_NAME", service.category)
            startActivity(intent)
            // Optionally clear search and hide results after click
            etServiceSearch.text.clear()
            rvServiceSearchResults.visibility = View.GONE
        }
        rvServiceSearchResults.layoutManager = LinearLayoutManager(this)
        rvServiceSearchResults.adapter = serviceSearchAdapter
    }

    private fun filterServices(query: String) {
        if (query.isEmpty()) {
            rvServiceSearchResults.visibility = View.GONE
            tvNoServiceResults.visibility = View.GONE
            // Restore visibility of other sections if they were hidden
            findViewById<View>(R.id.tvCategoriesTitle).visibility = View.VISIBLE
            findViewById<View>(R.id.rvCategories).visibility = View.VISIBLE
            // ... and so on for other sections if they were hidden
            return
        }

        val filteredServices = allServices.filter {
            it.name.contains(query, ignoreCase = true) || 
            it.category.contains(query, ignoreCase = true)
        }

        serviceSearchAdapter.updateData(filteredServices)

        if (filteredServices.isEmpty()) {
            rvServiceSearchResults.visibility = View.GONE
            tvNoServiceResults.visibility = View.VISIBLE
        } else {
            rvServiceSearchResults.visibility = View.VISIBLE
            tvNoServiceResults.visibility = View.GONE
        }
        // Hide other sections when search results are shown
        findViewById<View>(R.id.tvCategoriesTitle).visibility = if (query.isNotEmpty()) View.GONE else View.VISIBLE
        findViewById<View>(R.id.rvCategories).visibility = if (query.isNotEmpty()) View.GONE else View.VISIBLE
        findViewById<View>(R.id.tvRecentTransactionsTitle).visibility = if (query.isNotEmpty()) View.GONE else View.VISIBLE
        findViewById<View>(R.id.rvRecentTransactions).visibility = if (query.isNotEmpty()) View.GONE else View.VISIBLE
        findViewById<View>(R.id.tvRecommendedTitle).visibility = if (query.isNotEmpty()) View.GONE else View.VISIBLE
        findViewById<View>(R.id.rvRecommended).visibility = if (query.isNotEmpty()) View.GONE else View.VISIBLE
        findViewById<View>(R.id.tvViewAllCategories).visibility = if (query.isNotEmpty()) View.GONE else View.VISIBLE
        findViewById<View>(R.id.tvViewAllTransactions).visibility = if (query.isNotEmpty()) View.GONE else View.VISIBLE

    }

    // This function was duplicated. The correct implementation is below.
    // private fun setupRecentTransactionsRecyclerView() { // Changed from setupPopularRecyclerView
    //     setupRecommendedRecyclerView()
    // }

    private fun setupCategoriesRecyclerView() {
        val rvCategories = findViewById<RecyclerView>(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Sample categories data
        val categories = listOf(
            CategoryModel("1", "Plumbing", R.drawable.ic_plumbing, "Fix leaks, installations & repairs"),
            CategoryModel("2", "Electrical", R.drawable.ic_electrical, "Wiring, fixtures & electrical repairs"),
            CategoryModel("3", "Cleaning", R.drawable.ic_cleaning, "Home & office cleaning services"),
            CategoryModel("4", "Gardening", R.drawable.ic_gardening, "Lawn care & landscaping"),
            CategoryModel("5", "Painting", R.drawable.ic_painting, "Interior & exterior painting"),
            CategoryModel("6", "Moving", R.drawable.ic_moving, "Relocation & furniture moving"),
        )

        // Use the new HomeCategoryAdapter instead of CategoryAdapter
        val adapter = HomeCategoryAdapter(this, categories)
        rvCategories.adapter = adapter

        // Show toast with loaded categories count
        Toast.makeText(this, "${categories.size} categories loaded", Toast.LENGTH_SHORT).show()
    }

    // Correct implementation of setupRecentTransactionsRecyclerView
    private fun setupRecentTransactionsRecyclerView() {
        val rvRecentTransactions = findViewById<RecyclerView>(R.id.rvRecentTransactions)
        rvRecentTransactions.layoutManager = LinearLayoutManager(this)

        // In a real app, this would come from a database or API
        val transactions = listOf(
            TransactionModel(
                id = "1",
                cometeerName = "Emily Wilson",
                cometeerImageUrl = "",
                serviceType = "Cleaning Service",
                dateTime = "May 20, 2023 - 10:00 AM",
                cost = 120.00,
                status = TransactionStatus.ONGOING
            ),
            TransactionModel(
                id = "2",
                cometeerName = "John Smith",
                cometeerImageUrl = "",
                serviceType = "Plumbing Service",
                dateTime = "May 15, 2023 - 2:30 PM",
                cost = 85.00,
                status = TransactionStatus.COMPLETED
            ),
            TransactionModel(
                id = "3",
                cometeerName = "David Brown",
                cometeerImageUrl = "",
                serviceType = "Lawn Mowing",
                dateTime = "May 10, 2023 - 4:00 PM",
                cost = 45.00,
                status = TransactionStatus.COMPLETED
            ),
            TransactionModel(
                id = "4",
                cometeerName = "Sarah Williams",
                cometeerImageUrl = "",
                serviceType = "Tutoring",
                dateTime = "May 5, 2023 - 6:30 PM",
                cost = 60.00,
                status = TransactionStatus.COMPLETED
            )
        )

        // Sort transactions: ONGOING first, then COMPLETED
        val sortedTransactions = transactions.sortedWith(compareBy {
            when (it.status) {
                TransactionStatus.ONGOING -> 0  // Lower number = higher priority
                TransactionStatus.COMPLETED -> 1
            }
        }).take(4) // Only take the top 4 transactions

        // Create a simplified adapter for home screen transactions
        val adapter = HomeTransactionAdapter(sortedTransactions) { transaction ->
            // Navigate to transaction details or chat
            val intent = Intent(this, ActivityLogActivity::class.java)
            startActivity(intent)
        }

        rvRecentTransactions.adapter = adapter
    }

    private fun setupRecommendedRecyclerView() {
        val rvRecommended = findViewById<RecyclerView>(R.id.rvRecommended)
        rvRecommended.layoutManager = LinearLayoutManager(this)

        // Sample recommended categories with descriptions
        val recommendedCategories = listOf(
            Pair("Cleaning", "Professional home and office cleaning services"),
            Pair("Plumbing", "Expert plumbers for repairs and installations"),
            Pair("Electrical", "Licensed electricians for all electrical needs"),
            Pair("Tutoring", "Qualified tutors for various subjects")
        )

        val adapter = RecommendedAdapter(recommendedCategories) { categoryName ->
            val intent = Intent(this, RadiusMatchingActivity::class.java)
            intent.putExtra("CATEGORY_NAME", categoryName)
            startActivity(intent)
        }

        rvRecommended.adapter = adapter
    }

    // onNavigationItemSelected was moved up to be with other overridden methods for clarity
}