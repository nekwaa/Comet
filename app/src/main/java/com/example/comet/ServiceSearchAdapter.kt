package com.example.comet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ServiceSearchAdapter(
    private var services: MutableList<ServiceSearchModel>,
    private val onItemClick: (ServiceSearchModel) -> Unit
) : RecyclerView.Adapter<ServiceSearchAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.tvServiceName)
        val serviceAvailability: TextView = itemView.findViewById(R.id.tvServiceAvailability)
        val serviceCategory: TextView = itemView.findViewById(R.id.tvServiceCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service_search_result, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.serviceName.text = service.name
        holder.serviceAvailability.text = "${service.availableCount} available"
        holder.serviceCategory.text = "Category: ${service.category}"

        holder.itemView.setOnClickListener {
            onItemClick(service)
        }
    }

    override fun getItemCount() = services.size

    fun updateData(newServices: List<ServiceSearchModel>) {
        services.clear()
        services.addAll(newServices)
        notifyDataSetChanged()
    }
}