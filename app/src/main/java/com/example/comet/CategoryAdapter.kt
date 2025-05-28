package com.example.comet

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class CategoryAdapter(
    private val context: Context,
    private val categories: List<CategoryModel>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCategoryIcon: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
        val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        holder.ivCategoryIcon.setImageResource(category.iconResId)
        holder.tvCategoryName.text = category.name

        // Apply a slight animation effect on click
        holder.itemView.setOnClickListener {
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(100).start()

                // Navigate to the radius matching activity with category info
                val intent = Intent(context, RadiusMatchingActivity::class.java).apply {
                    putExtra("CATEGORY_NAME", category.name)
                    putExtra("CATEGORY_DESCRIPTION", category.description)
                    putExtra("CATEGORY_ICON", category.iconResId)
                }
                context.startActivity(intent)
            }.start()
        }
    }

    override fun getItemCount(): Int = categories.size
}