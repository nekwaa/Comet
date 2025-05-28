package com.example.comet

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeCategoryAdapter(
    private val context: Context,
    private val categories: List<CategoryModel>
) : RecyclerView.Adapter<HomeCategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCategoryIcon: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
        val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_home, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        holder.ivCategoryIcon.setImageResource(category.iconResId)
        holder.tvCategoryName.text = category.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, RadiusMatchingActivity::class.java).apply {
                putExtra("CATEGORY_NAME", category.name)
                putExtra("CATEGORY_DESCRIPTION", category.description)
                putExtra("CATEGORY_ICON", category.iconResId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = categories.size
}