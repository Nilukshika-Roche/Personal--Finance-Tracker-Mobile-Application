package com.example.mynilu

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mynilu.data.Transaction
import com.example.mynilu.utils.SharedPrefHelper
import java.util.Calendar

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var spinnerTitle: Spinner
    private lateinit var spinnerCategory: Spinner
    private lateinit var editAmount: EditText
    private lateinit var textDate: TextView
    private lateinit var buttonPickDate: Button
    private lateinit var buttonSave: Button

    private val incomeCategories = arrayOf("Salary", "Bonus", "OtherIncome")
    private val expenseCategories = arrayOf(
        "Household", "Education", "Transportation", "Food", "Clothing",
        "Bills", "Entertainment", "Health", "OtherExpense"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        // Initialize views
        spinnerTitle = findViewById(R.id.spinnerTitle)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        editAmount = findViewById(R.id.editAmount)
        textDate = findViewById(R.id.textDate)
        buttonPickDate = findViewById(R.id.buttonPickDate)
        buttonSave = findViewById(R.id.btnSave)

        // Set default date text
        textDate.text = "Select Date"

        // Set up Title Spinner
        val titleOptions = arrayOf("Income", "Expense")
        val titleAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, titleOptions)
        titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTitle.adapter = titleAdapter

        // Set category list based on initial title
        updateCategorySpinner("Income")

        spinnerTitle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long
            ) {
                val selectedTitle = titleOptions[position]
                updateCategorySpinner(selectedTitle)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val formattedDate = "$day/${month + 1}/$year"
            textDate.text = formattedDate
        }

        buttonPickDate.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        buttonSave.setOnClickListener {
            val title = spinnerTitle.selectedItem.toString()
            val amount = editAmount.text.toString().toDoubleOrNull()
            val category = spinnerCategory.selectedItem?.toString() ?: ""
            val date = textDate.text.toString()

            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (category.isBlank()) {
                Toast.makeText(this, "Select a category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (date.isBlank() || date == "Select Date") {
                Toast.makeText(this, "Pick a date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val transaction = Transaction(
                id = System.currentTimeMillis(),
                title = title,
                amount = amount,
                category = category,
                date = date
            )

            val sharedPrefHelper = SharedPrefHelper(this)
            sharedPrefHelper.addTransaction(transaction)

            Toast.makeText(this, "Transaction Saved", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ViewTransactionsActivity::class.java))
            finish()
        }
    }

    private fun updateCategorySpinner(title: String) {
        val categories = if (title == "Income") incomeCategories else expenseCategories
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter
    }
}
