package com.example.comet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class RecommendedAdapter(
    private val recommendations: List<Pair<String, String>>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<RecommendedAdapter.RecommendationViewHolder>() {

    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardRecommendation: CardView = itemView.findViewById(R.id.cardRecommendation)
        val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val tvCategoryDescription: TextView = itemView.findViewById(R.id.tvCategoryDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val recommendation = recommendations[position]

        holder.tvCategoryName.text = recommendation.first
        holder.tvCategoryDescription.text = recommendation.second

        holder.cardRecommendation.setOnClickListener {
            onItemClick(recommendation.first)
        }
    }

    override fun getItemCount(): Int = recommendations.size
}