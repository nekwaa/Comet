package com.example.comet

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class HomeTransactionAdapter(
    private val transactions: List<TransactionModel>,
    private val onItemClick: (TransactionModel) -> Unit
) : RecyclerView.Adapter<HomeTransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTransaction: CardView = itemView.findViewById(R.id.cardTransaction)
        val tvCometeerName: TextView = itemView.findViewById(R.id.tvCometeerName)
        val tvServiceType: TextView = itemView.findViewById(R.id.tvServiceType)
        val tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        val tvCost: TextView = itemView.findViewById(R.id.tvCost)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.tvCometeerName.text = transaction.cometeerName
        holder.tvServiceType.text = transaction.serviceType
        holder.tvDateTime.text = transaction.dateTime
        holder.tvCost.text = "$${transaction.cost}"

        // Set status text and color
        when (transaction.status) {
            TransactionStatus.ONGOING -> {
                holder.tvStatus.text = "Ongoing"
                holder.tvStatus.setTextColor(holder.itemView.context.getColor(R.color.ongoing_color))
            }
            TransactionStatus.COMPLETED -> {
                holder.tvStatus.text = "Completed"
                holder.tvStatus.setTextColor(holder.itemView.context.getColor(R.color.completed_color))
            }
        }

        holder.cardTransaction.setOnClickListener {
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

    override fun getItemCount(): Int = transactions.size
}