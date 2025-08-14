package com.example.mynilu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynilu.R
import com.example.mynilu.data.Transaction

class TransactionAdapter(
    private val context: Context,
    private var transactions: MutableList<Transaction>,
    private val onEdit: (Transaction) -> Unit,
    private val onDelete: (Long) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView       = itemView.findViewById(R.id.textTitle)
        val amount: TextView      = itemView.findViewById(R.id.textAmount)
        val category: TextView    = itemView.findViewById(R.id.textCategory)
        val date: TextView        = itemView.findViewById(R.id.textDate)
        val menuButton: ImageView = itemView.findViewById(R.id.menuButton)

        init {
            menuButton.setOnClickListener {
                val popup = PopupMenu(context, menuButton)
                popup.inflate(R.menu.transaction_menu)
                popup.setOnMenuItemClickListener { item ->
                    val tx = transactions.getOrNull(adapterPosition)
                    if (tx != null) {
                        when (item.itemId) {
                            R.id.menu_edit -> onEdit(tx)
                            R.id.menu_delete -> onDelete(tx.id)
                        }
                        true
                    } else false
                }
                popup.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.activity_item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tx = transactions[position]
        holder.title.text    = tx.title
        holder.amount.text   = "Rs. ${tx.amount}"
        holder.category.text = tx.category
        holder.date.text     = tx.date
    }

    override fun getItemCount() = transactions.size

    fun updateList(newList: List<Transaction>) {
        transactions = newList.toMutableList()
        notifyDataSetChanged()
    }
}
