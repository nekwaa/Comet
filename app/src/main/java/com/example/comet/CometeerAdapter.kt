package com.example.comet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent

class CometeerAdapter(
    private var cometeers: List<CometeerModel>,
    private val onItemClick: (CometeerModel) -> Unit
) : RecyclerView.Adapter<CometeerAdapter.CometeerViewHolder>() {

    class CometeerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CometeerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cometeer, parent, false)
        return CometeerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CometeerViewHolder, position: Int) {
        val cometeer = cometeers[position]

        holder.tvName.text = cometeer.name
        holder.tvCategory.text = cometeer.category
        holder.tvDistance.text = "${cometeer.distance} km away"
        holder.ratingBar.rating = cometeer.rating
        holder.tvRating.text = "${cometeer.rating} (${cometeer.numReviews})"

        holder.itemView.setOnClickListener {
            onItemClick(cometeer)
        }
    }

    override fun getItemCount(): Int = cometeers.size

    fun updateData(newCometeers: List<CometeerModel>) {
        cometeers = newCometeers
        notifyDataSetChanged()
    }
}