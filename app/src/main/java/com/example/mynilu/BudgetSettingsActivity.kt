package com.example.mynilu

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mynilu.utils.SharedPrefHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BudgetSettingsActivity : AppCompatActivity() {

    private lateinit var textDate: TextView  // TextView to display the selected date
    private lateinit var buttonPickDate: Button  // Button to open DatePickerDialog
    private lateinit var spinnerCurrency: Spinner  // Spinner for selecting currency
    private lateinit var editBudget: EditText  // EditText to enter budget
    private lateinit var sharedPrefHelper: SharedPrefHelper // To save and retrieve data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_settings)

        // Initialize Views
        textDate = findViewById(R.id.textDate)
        buttonPickDate = findViewById(R.id.buttonPickDate)
        spinnerCurrency = findViewById(R.id.spinnerCurrency)
        editBudget = findViewById(R.id.editBudget)
        sharedPrefHelper = SharedPrefHelper(this)

        val logo = findViewById<ImageView>(R.id.imageView50)
        logo.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

        // Set up Spinner for Currency
        val currencyList = arrayOf("Rs.", "$", "€", "£", "¥")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCurrency.adapter = adapter

        // Load existing currency and budget if available
        val savedCurrency = sharedPrefHelper.getCurrency() ?: "Rs."
        val savedBudget = sharedPrefHelper.getBudget()
        val savedDate = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("budgetDate", "Not Set")

        // Set current values in the UI
        val currencyIndex = currencyList.indexOf(savedCurrency)
        spinnerCurrency.setSelection(currencyIndex)
        editBudget.setText(savedBudget.toString())
        textDate.text = "Selected Date: $savedDate"

        // Set up Date Picker
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = Calendar.getInstance()
                date.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(date.time)

                textDate.text = "Selected Date: $formattedDate"
                // Save selected date
                val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                sharedPref.edit().putString("budgetDate", formattedDate).apply()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Open the DatePickerDialog when the button is clicked
        buttonPickDate.setOnClickListener {
            datePickerDialog.show()
        }

        // Save the new or edited budget and currency when the Save button is clicked
        val buttonSaveSettings = findViewById<Button>(R.id.buttonSave)
        buttonSaveSettings.setOnClickListener {
            val budgetInput = editBudget.text.toString()
            if (budgetInput.isNotEmpty()) {
                val budget = budgetInput.toFloat()

                // Get the selected currency
                val selectedCurrency = spinnerCurrency.selectedItem.toString()

                // Save the budget and currency
                sharedPrefHelper.saveCurrency(selectedCurrency)
                sharedPrefHelper.saveBudget(budget)

                // Provide feedback to the user
                Toast.makeText(this, "Budget and Currency Saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a budget value.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
