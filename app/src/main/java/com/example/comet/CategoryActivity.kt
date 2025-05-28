package com.example.comet

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryActivity : AppCompatActivity() {

    private lateinit var rvCategories: RecyclerView
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // Set up back button
        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            val intent = Intent(this, MessagesActivity::class.java)
            startActivity(intent)
        }

        rvCategories = findViewById(R.id.rvCategories)

        // Set up RecyclerView with 3 columns for better display
        rvCategories.layoutManager = GridLayoutManager(this, 3)

        // Create expanded list of categories
        val categories = listOf(
            CategoryModel("1", "Plumbing", R.drawable.ic_plumbing, "Fix leaks, installations & repairs"),
            CategoryModel("2", "Electrical", R.drawable.ic_electrical, "Wiring, fixtures & electrical repairs"),
            CategoryModel("3", "Cleaning", R.drawable.ic_cleaning, "Home & office cleaning services"),
            CategoryModel("4", "Gardening", R.drawable.ic_gardening, "Lawn care & landscaping"),
            CategoryModel("5", "Painting", R.drawable.ic_painting, "Interior & exterior painting"),
            CategoryModel("6", "Moving", R.drawable.ic_moving, "Relocation & furniture moving"),
            CategoryModel("7", "Tutoring", R.drawable.ic_category_placeholder, "Academic & skill-based tutoring"),
            CategoryModel("8", "Pet Care", R.drawable.ic_category_placeholder, "Pet sitting & dog walking"),
            CategoryModel("9", "Beauty", R.drawable.ic_category_placeholder, "Hair, makeup & nail services"),
            CategoryModel("10", "Fitness", R.drawable.ic_category_placeholder, "Personal training & fitness classes"),
            CategoryModel("11", "Tech Help", R.drawable.ic_category_placeholder, "Computer & device support"),
            CategoryModel("12", "Cooking", R.drawable.ic_category_placeholder, "Personal chef & meal prep")
        )

        // Set up adapter
        adapter = CategoryAdapter(this, categories)
        rvCategories.adapter = adapter
    }
}