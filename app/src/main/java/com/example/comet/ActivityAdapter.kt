package com.example.comet

import android.app.Dialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import androidx.core.content.ContextCompat

class ActivityAdapter(
    private var transactions: MutableList<TransactionModel>,
    private val onCancelClick: (TransactionModel) -> Unit,
    private val onReviewSubmitted: (ReviewModel) -> Unit,
    private val onReviewDeleted: (String) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.TransactionViewHolder>() {

    private val reviews = mutableMapOf<String, ReviewModel>() // Store reviews by transactionId

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCometeerImage: ImageView = itemView.findViewById(R.id.ivActivityIcon)
        val tvCometeerName: TextView = itemView.findViewById(R.id.tvActivityTitle)
        val tvServiceDetails: TextView = itemView.findViewById(R.id.tvActivityDescription)
        val tvDateTime: TextView = itemView.findViewById(R.id.tvActivityTime)
        val tvCost: TextView = itemView.findViewById(R.id.tvCost)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnAction: Button = itemView.findViewById(R.id.btnAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.tvCometeerName.text = transaction.cometeerName
        holder.tvServiceDetails.text = transaction.serviceType
        holder.tvDateTime.text = transaction.dateTime
        holder.tvCost.text = "$${transaction.cost}"

        // Set status text and color
        when (transaction.status) {
            TransactionStatus.ONGOING -> {
                holder.tvStatus.text = "Ongoing"
                holder.tvStatus.setTextColor(holder.itemView.context.getColor(R.color.ongoing_color))
                holder.btnAction.text = "Cancel Service"
                holder.btnAction.setOnClickListener { onCancelClick(transaction) }
            }
            TransactionStatus.COMPLETED -> {
                holder.tvStatus.text = "Completed"
                holder.tvStatus.setTextColor(holder.itemView.context.getColor(R.color.completed_color))
                holder.btnAction.text = "Leave Review"
                holder.btnAction.setOnClickListener {
                    // Show review dialog instead of just calling onReviewClick
                    showReviewDialog(holder.itemView, transaction)
                }
            }
        }

        // Load cometeer image
        holder.ivCometeerImage.setImageResource(R.drawable.profile_placeholder)

        // Add click listener to the entire item view
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, TransactionDetailActivity::class.java).apply {
                putExtra("TRANSACTION_ID", transaction.id)
                putExtra("COMETEER_NAME", transaction.cometeerName)
                putExtra("SERVICE_TYPE", transaction.serviceType)
                putExtra("DATE_TIME", transaction.dateTime)
                putExtra("COST", transaction.cost)
                putExtra("STATUS", transaction.status.name)
            }
            context.startActivity(intent)
        }
    }

    private fun showReviewDialog(view: View, transaction: TransactionModel) {
        val context = view.context
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_leave_review, null)

        // Initialize dialog views
        val tvDialogTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)
        val tvCometeerName = dialogView.findViewById<TextView>(R.id.tvCometeerName)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val etReviewComment = dialogView.findViewById<TextInputEditText>(R.id.etReviewComment)
        val btnSubmitReview = dialogView.findViewById<Button>(R.id.btnSubmitReview)
        val btnCancelReview = dialogView.findViewById<Button>(R.id.btnCancelReview)

        // Check if review exists
        val existingReview = reviews[transaction.id]
        if (existingReview != null) {
            tvDialogTitle.text = "Edit Review"
            ratingBar.rating = existingReview.rating
            etReviewComment.setText(existingReview.comment)
            btnSubmitReview.text = "Update Review"
        } else {
            tvDialogTitle.text = "Leave a Review"
            btnSubmitReview.text = "Submit Review"
        }

        // Set cometeer name
        tvCometeerName.text = "How was your experience with ${transaction.cometeerName}?"

        // Create dialog
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialog.setCancelable(true)

        // Add delete button if review exists
        if (existingReview != null) {
            val btnDeleteReview = Button(context).apply {
                text = "Delete Review"
                setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
                setBackgroundResource(android.R.color.transparent)
                setOnClickListener {
                    AlertDialog.Builder(context)
                        .setTitle("Delete Review")
                        .setMessage("Are you sure you want to delete this review?")
                        .setPositiveButton("Delete") { _, _ ->
                            reviews.remove(transaction.id)
                            onReviewDeleted(transaction.id)
                            dialog.dismiss()
                            notifyDataSetChanged()
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
                Toast.makeText(context, "Please select a rating", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val review = ReviewModel(
                id = existingReview?.id ?: System.currentTimeMillis().toString(),
                transactionId = transaction.id,
                cometeerName = transaction.cometeerName,
                rating = rating,
                comment = etReviewComment.text?.toString(),
                dateTime = System.currentTimeMillis().toString(),
                isEdited = existingReview != null
            )

            reviews[transaction.id] = review
            onReviewSubmitted(review)
            dialog.dismiss()
            notifyDataSetChanged()
        }

        btnCancelReview.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun getItemCount() = transactions.size

    fun updateData(newTransactions: List<TransactionModel>) {
        transactions.clear()
        transactions.addAll(newTransactions)
        notifyDataSetChanged()
    }
}