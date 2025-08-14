package com.example.mynilu


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynilu.adapters.TransactionAdapter
import com.example.mynilu.utils.SharedPrefHelper

class item_transaction : AppCompatActivity() {

    private lateinit var transactionRecyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_transaction)

        transactionRecyclerView = findViewById(R.id.recyclerView)
        sharedPrefHelper = SharedPrefHelper(this)

        // Fetch the saved transactions
        val transactions = sharedPrefHelper.getTransactions()

        // Set up the adapter
        transactionAdapter = TransactionAdapter(
            context = this,
            transactions = transactions.toMutableList(),
            onEdit = { transaction ->
                // Handle edit (open another activity to edit the transaction)
                Toast.makeText(this, "Edit transaction: ${transaction.title}", Toast.LENGTH_SHORT).show()
            },
            onDelete = { transactionId ->
                sharedPrefHelper.deleteTransaction(transactionId)
                // Refresh the list after deletion
                transactionAdapter.updateList(sharedPrefHelper.getTransactions())
            }
        )

        // Set the adapter to RecyclerView
        transactionRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionRecyclerView.adapter = transactionAdapter
    }
}
