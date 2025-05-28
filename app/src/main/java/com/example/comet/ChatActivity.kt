package com.example.comet

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var tvName: TextView
    private lateinit var ivCall: ImageView
    private lateinit var rvChat: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var ivSend: ImageView
    private lateinit var ivAttachment: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Get contact name from intent
        val contactName = intent.getStringExtra("CONTACT_NAME") ?: "Chat"

        // Initialize views
        ivBack = findViewById(R.id.ivBack)
        tvName = findViewById(R.id.tvName)
        ivCall = findViewById(R.id.ivCall)
        rvChat = findViewById(R.id.rvChat)
        etMessage = findViewById(R.id.etMessage)
        ivSend = findViewById(R.id.ivSend)
        ivAttachment = findViewById(R.id.ivAttachment)

        // Set contact name
        tvName.text = contactName

        // Set up listeners
        ivBack.setOnClickListener {
            finish()

        }

        ivCall.setOnClickListener {
            Toast.makeText(this, "Call feature coming soon", Toast.LENGTH_SHORT).show()
        }

        ivAttachment.setOnClickListener {
            Toast.makeText(this, "Attachment feature coming soon", Toast.LENGTH_SHORT).show()
        }

        ivSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                etMessage.text.clear()
            }
        }

        // Load chat messages
        loadChatMessages()
    }

    // Update the loadChatMessages method
    private fun loadChatMessages() {
        // Sample messages for demonstration
        val messages = mutableListOf(
            Message("Hi, I need help with my plumbing issue.", "10:30 AM", true),
            Message("Hello! I'd be happy to help. Can you describe the problem?", "10:32 AM", false),
            Message("My kitchen sink is leaking from the pipe underneath.", "10:33 AM", true),
            Message("I see. Is it a slow drip or a larger leak?", "10:35 AM", false),
            Message("It's a slow but steady drip. It's been going on for a couple of days.", "10:36 AM", true),
            Message("I understand. I can come by tomorrow morning around 9 AM to take a look. Does that work for you?", "10:38 AM", false),
            Message("Yes, that works perfectly. Thank you!", "10:39 AM", true, false, false),
            Message("Great! Your booking for plumbing service tomorrow at 9 AM is confirmed. See you then!", "10:45 AM", false, true, false) // Booking confirmation
        )

        // Set up adapter
        val adapter = ChatAdapter(messages)
        rvChat.adapter = adapter
    }

    // Update the sendMessage method
    private fun sendMessage(message: String) {
        val timeStamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        val newMessage = Message(message, timeStamp, true, false, true) // New message is sent and read by sender

        // Add message to chat
        (rvChat.adapter as? ChatAdapter)?.addMessage(newMessage)

        // Scroll to the bottom
        rvChat.smoothScrollToPosition((rvChat.adapter?.itemCount ?: 0) - 1)

        // Simulate a reply after a delay (for demo purposes)
        rvChat.postDelayed({
            val reply = when {
                message.contains("hello", ignoreCase = true) ||
                        message.contains("hi", ignoreCase = true) -> "Hello! How can I help you today?"
                message.contains("thanks", ignoreCase = true) -> "You're welcome! Is there anything else you need help with?"
                message.contains("time", ignoreCase = true) -> "I'm available tomorrow from 9 AM to 5 PM."
                message.contains("price", ignoreCase = true) ||
                        message.contains("cost", ignoreCase = true) -> "My standard rate is $85/hour with a one-hour minimum."
                message.contains("?") -> "That's a good question. Let me check and get back to you shortly."
                else -> "Thanks for your message. I'll respond as soon as possible."
            }

            val replyMessage = Message(reply, timeStamp, false, false, false) // Received message, initially unread
            (rvChat.adapter as? ChatAdapter)?.addMessage(replyMessage)
            rvChat.smoothScrollToPosition((rvChat.adapter?.itemCount ?: 0) - 1)

            // Simulate marking received message as read after a short delay
            rvChat.postDelayed({
                (rvChat.adapter as? ChatAdapter)?.markMessageAsRead(replyMessage)
            }, 500)
        }, 1000) // 1-second delay for the "reply"
    }
}