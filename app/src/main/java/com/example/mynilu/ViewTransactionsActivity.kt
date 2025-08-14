package com.example.mynilu

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynilu.adapters.TransactionAdapter
import com.example.mynilu.data.Transaction
import com.example.mynilu.utils.SharedPrefHelper

class ViewTransactionsActivity : AppCompatActivity() {

    private lateinit var helper: SharedPrefHelper
    private lateinit var adapter: TransactionAdapter
    private lateinit var recyclerView: RecyclerView
    private var transactions = mutableListOf<Transaction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_transactions)

        helper = SharedPrefHelper(this)
        recyclerView = findViewById(R.id.recyclerView)

        // Load transactions (most recent first)
        transactions = helper.getTransactions().reversed().toMutableList()

        adapter = TransactionAdapter(
            this, transactions,
            onEdit = { showEditDialog(it) },
            onDelete = { id ->
                helper.deleteTransaction(id)
                refreshList()
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivityForResult(intent, 1)
        }

        val logo = findViewById<ImageView>(R.id.imageView4)
        logo.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

        // Backup and Restore buttons
        val btnBackup = findViewById<Button>(R.id.btnBackup)
        val btnRestore = findViewById<Button>(R.id.btnRestore)

        btnBackup.setOnClickListener {
            val successExport = helper.exportTransactionsToFile(this)
            if (successExport) {
                Toast.makeText(this, "Backup successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Backup failed!", Toast.LENGTH_SHORT).show()
            }
        }

        btnRestore.setOnClickListener {
            val successImport = helper.importTransactionsFromFile(this)
            if (successImport) {
                Toast.makeText(this, "Restore successful!", Toast.LENGTH_SHORT).show()
                refreshList()
            } else {
                Toast.makeText(this, "Restore failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEditDialog(transaction: Transaction) {
        val dialogView = layoutInflater.inflate(R.layout.activity_dialog_edit_transaction, null)
        val titleSpinner = dialogView.findViewById<Spinner>(R.id.editTitle)
        val amount = dialogView.findViewById<EditText>(R.id.editAmount)
        val categorySpinner = dialogView.findViewById<Spinner>(R.id.editCategory)

        // Set current transaction values
        amount.setText(transaction.amount.toString())

        // Title Spinner (Income or Expense)
        val titleOptions = arrayOf("Income", "Expense")
        val titleAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, titleOptions)
        titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        titleSpinner.adapter = titleAdapter
        val titlePosition = titleOptions.indexOf(transaction.title)
        titleSpinner.setSelection(if (titlePosition >= 0) titlePosition else 0)

        // Category Spinner
        val categoryOptions = arrayOf(
            "Household", "Education", "Transportation", "Food", "Clothing", "Bills",
            "Entertainment", "Health", "Salary", "bonus", "OtherIncome", "OtherExpense"
        )
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryOptions)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter
        val categoryPosition = categoryOptions.indexOf(transaction.category)
        categorySpinner.setSelection(if (categoryPosition >= 0) categoryPosition else 0)

        AlertDialog.Builder(this)
            .setTitle("Edit Transaction")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updated = transaction.copy(
                    title = titleSpinner.selectedItem.toString(),
                    amount = amount.text.toString().toDoubleOrNull() ?: 0.0,
                    category = categorySpinner.selectedItem.toString()
                )
                helper.updateTransaction(updated)
                refreshList()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun refreshList() {
        val reversedList = helper.getTransactions().reversed()
        adapter.updateList(reversedList)
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }
}
