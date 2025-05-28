package com.example.comet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText

class TransactionDetailActivity : AppCompatActivity() {

    private var currentReview: ReviewModel? = null

    private lateinit var ivBack: ImageView
    private lateinit var ivCometeerImage: ImageView
    private lateinit var tvCometeerName: TextView
    private lateinit var tvServiceType: TextView
    private lateinit var tvDateTime: TextView
    private lateinit var tvCost: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnAction: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_detail)

        // Initialize views
        ivBack = findViewById(R.id.ivBack)
        ivCometeerImage = findViewById(R.id.ivCometeerImage)
        tvCometeerName = findViewById(R.id.tvCometeerName)
        tvServiceType = findViewById(R.id.tvServiceType)
        tvDateTime = findViewById(R.id.tvDateTime)
        tvCost = findViewById(R.id.tvCost)
        tvStatus = findViewById(R.id.tvStatus)
        btnAction = findViewById(R.id.btnAction)

        // Set up back button
        ivBack.setOnClickListener {
            finish()
        }

        // Get transaction details from intent
        val transactionId = intent.getStringExtra("TRANSACTION_ID") ?: ""
        val cometeerName = intent.getStringExtra("COMETEER_NAME") ?: ""
        val serviceType = intent.getStringExtra("SERVICE_TYPE") ?: ""
        val dateTime = intent.getStringExtra("DATE_TIME") ?: ""
        val cost = intent.getDoubleExtra("COST", 0.0)
        val statusString = intent.getStringExtra("STATUS") ?: ""
        val status = TransactionStatus.valueOf(statusString)

        // Set transaction details to views
        tvCometeerName.text = cometeerName
        tvServiceType.text = serviceType
        tvDateTime.text = dateTime
        tvCost.text = "$${cost}"

        // Set status text and color
        when (status) {
            TransactionStatus.ONGOING -> {
                tvStatus.text = "Ongoing"
                tvStatus.setTextColor(getColor(R.color.ongoing_color))
                btnAction.text = "Cancel Service"
                btnAction.setOnClickListener {
                    // Handle cancel service
                    Toast.makeText(this, "Cancel service with $cometeerName", Toast.LENGTH_SHORT).show()
                }
            }
            TransactionStatus.COMPLETED -> {
                tvStatus.text = "Completed"
                tvStatus.setTextColor(getColor(R.color.completed_color))
                btnAction.text = "Leave Review"
                btnAction.setOnClickListener {
                    // Show review dialog
                    showReviewDialog(cometeerName)
                }
            }
        }

        // Load cometeer image
        ivCometeerImage.setImageResource(R.drawable.profile_placeholder)
    }

    private fun showReviewDialog(cometeerName: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_leave_review, null)

        // Initialize dialog views
        val tvDialogTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)
        val tvCometeerName = dialogView.findViewById<TextView>(R.id.tvCometeerName)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val etReviewComment = dialogView.findViewById<TextInputEditText>(R.id.etReviewComment)
        val btnSubmitReview = dialogView.findViewById<Button>(R.id.btnSubmitReview)
        val btnCancelReview = dialogView.findViewById<Button>(R.id.btnCancelReview)

        // Check if review exists
        if (currentReview != null) {
            tvDialogTitle.text = "Edit Review"
            ratingBar.rating = currentReview!!.rating
            etReviewComment.setText(currentReview!!.comment)
            btnSubmitReview.text = "Update Review"
        } else {
            tvDialogTitle.text = "Leave a Review"
            btnSubmitReview.text = "Submit Review"
        }

        // Set cometeer name
        tvCometeerName.text = "How was your experience with $cometeerName?"

        // Create dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.setCancelable(true)

        // Add delete button if review exists
        if (currentReview != null) {
            val btnDeleteReview = Button(this).apply {
                text = "Delete Review"
                setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
                setBackgroundResource(android.R.color.transparent)
                setOnClickListener {
                    AlertDialog.Builder(this@TransactionDetailActivity)
                        .setTitle("Delete Review")
                        .setMessage("Are you sure you want to delete this review?")
                        .setPositiveButton("Delete") { _, _ ->
                            currentReview = null
                            updateReviewUI()
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            }
            dialogView.findViewById<ViewGroup>(R.id.dialogContainer).addView(btnDeleteReview)
        }

        // Set up buttons
        btnSubmitReview.setOnClickListener {
            val rating = ratingBar.rating
            if (rating == 0f) {
                Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val transactionId = intent.getStringExtra("TRANSACTION_ID") ?: return@setOnClickListener
            
            currentReview = ReviewModel(
                id = currentReview?.id ?: System.currentTimeMillis().toString(),
                transactionId = transactionId,
                cometeerName = cometeerName,
                rating = rating,
                comment = etReviewComment.text?.toString(),
                dateTime = System.currentTimeMillis().toString(),
                isEdited = currentReview != null
            )

            updateReviewUI()
            dialog.dismiss()
        }

        btnCancelReview.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateReviewUI() {
        // Update UI based on current review status
        if (currentReview != null) {
            btnAction.text = "Edit Review"
        } else {
            btnAction.text = "Leave Review"
        }
    }
}