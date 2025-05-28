package com.example.comet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(
    private var conversations: List<Triple<String, String, String>>,
    private var unreadCounts: List<Int>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvMessage: TextView = itemView.findViewById(R.id.tvLastMessage)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvUnreadCount: TextView = itemView.findViewById(R.id.tvUnreadCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message_conversation, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val conversation = conversations[position]
        holder.tvName.text = conversation.first
        holder.tvMessage.text = conversation.second
        holder.tvTime.text = conversation.third
        val unread = unreadCounts.getOrNull(position) ?: 0
        if (unread > 0) {
            holder.tvUnreadCount.text = unread.toString()
            holder.tvUnreadCount.visibility = View.VISIBLE
        } else {
            holder.tvUnreadCount.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int = conversations.size

    fun updateData(newConversations: List<Triple<String, String, String>>, newUnreadCounts: List<Int>) {
        conversations = newConversations
        unreadCounts = newUnreadCounts
        notifyDataSetChanged() // Consider using DiffUtil for better performance
    }

    fun getCurrentList(): List<Triple<String, String, String>> {
        return conversations
    }
}