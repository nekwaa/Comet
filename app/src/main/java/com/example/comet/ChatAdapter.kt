package com.example.comet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Color // Added for booking confirmation style
import android.graphics.Typeface // Added for booking confirmation style
import android.widget.ImageView // Added for read receipts
import android.widget.TextView
import androidx.core.content.ContextCompat // Added for booking confirmation style
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_sent, parent, false)
            SentMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_received, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSent) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun markMessageAsRead(messageToMark: Message) {
        val index = messages.indexOfFirst { it == messageToMark }
        if (index != -1) {
            messages[index].isRead = true
            notifyItemChanged(index)
        }
    }

    inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val ivReadReceipt: ImageView? = itemView.findViewById(R.id.ivReadReceipt) // Optional, layout needs this ID

        fun bind(message: Message) {
            tvMessage.text = message.text
            tvTime.text = message.time

            if (message.isBookingConfirmation) {
                tvMessage.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.booking_confirmation_background)) // Define this color
                tvMessage.setTextColor(ContextCompat.getColor(itemView.context, R.color.booking_confirmation_text)) // Define this color
                tvMessage.setTypeface(null, Typeface.BOLD_ITALIC)
            } else {
                // Reset to default if not a booking confirmation
                tvMessage.setBackgroundResource(R.drawable.message_sent_background) 
                tvMessage.setTextColor(Color.WHITE) // Or your default sent message text color
                tvMessage.setTypeface(null, Typeface.NORMAL)
            }

            ivReadReceipt?.visibility = View.VISIBLE
            if (message.isRead) {
                ivReadReceipt?.setImageResource(R.drawable.ic_read_receipt) // Double tick, blue or seen
            } else {
                ivReadReceipt?.setImageResource(R.drawable.ic_sent_receipt) // Single tick or delivered
            }
        }
    }

    inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)

        fun bind(message: Message) {
            tvMessage.text = message.text
            tvTime.text = message.time

            if (message.isBookingConfirmation) {
                tvMessage.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.booking_confirmation_background)) // Define this color
                tvMessage.setTextColor(ContextCompat.getColor(itemView.context, R.color.booking_confirmation_text)) // Define this color
                tvMessage.setTypeface(null, Typeface.BOLD_ITALIC)
            } else {
                // Reset to default if not a booking confirmation
                tvMessage.setBackgroundResource(R.drawable.message_received_background) 
                tvMessage.setTextColor(Color.BLACK) // Or your default received message text color
                tvMessage.setTypeface(null, Typeface.NORMAL)
            }
        }
    }
}