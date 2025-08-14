package com.example.mynilu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mynilu.utils.NotificationHelper
import com.example.mynilu.utils.SharedPrefHelper

class SummaryActivity : AppCompatActivity() {

    private lateinit var textSpent: TextView
    private lateinit var textBudget: TextView
    private lateinit var textDate: TextView
    private lateinit var textRemainingBudget: TextView
    private lateinit var buttonEditBudget: Button
    private lateinit var buttonDeleteBudget: Button
    private lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        // Initialize Views
        textSpent = findViewById(R.id.textSpent)
        textBudget = findViewById(R.id.textBudget)
        textDate = findViewById(R.id.textDate)
        textRemainingBudget = findViewById(R.id.textRemainingBudget)
        buttonEditBudget = findViewById(R.id.buttonEditBudget)
        buttonDeleteBudget = findViewById(R.id.buttonDeleteBudget)

        // Initialize SharedPrefHelper
        sharedPrefHelper = SharedPrefHelper(this)

        // Load current budget and other values from SharedPreferences
        val currentBudget = sharedPrefHelper.getBudget()
        val currency = sharedPrefHelper.getCurrency() ?: "Rs."
        val spent = sharedPrefHelper.getTotalSpent()

        // Edit Budget Button Click Listener
        buttonEditBudget.setOnClickListener {
            startActivity(Intent(this, BudgetSettingsActivity::class.java))
        }

        // Delete Budget Button Click Listener
        buttonDeleteBudget.setOnClickListener {
            sharedPrefHelper.deleteBudget()
            // Reset the UI after deleting the budget
            textBudget.text = "Monthly Budget: $currency 0.00"
            textSpent.text = "Total Spent: $currency 0.00"
            textRemainingBudget.text = "Remaining Budget: $currency 0.00"
            Toast.makeText(this, "Budget Deleted", Toast.LENGTH_SHORT).show()
        }

        // Set the current logo click listener to go back to Dashboard
        val logo = findViewById<ImageView>(R.id.imageView14)
        logo.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

        // Log the current budget and spent amount for debugging
        Log.d("SummaryActivity", "Budget: $currentBudget, Total Spent: $spent")

        // Update the UI with the current values
        textBudget.text = "Monthly Budget: $currency %.2f".format(currentBudget)
        textSpent.text = "Total Spent: $currency %.2f".format(spent)

        // Calculate remaining budget
        val remainingBudget = currentBudget - spent
        textRemainingBudget.text = "Remaining Budget: $currency %.2f".format(remainingBudget)

        // Load the selected date from SharedPreferences
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val date = sharedPref.getString("budgetDate", "Not Set")
        textDate.text = "Month: $date"

        // Initialize NotificationHelper
        val notificationHelper = NotificationHelper(this)

        // Send notifications if needed
        if (spent > currentBudget) {
            notificationHelper.sendBudgetLimitNotification(spent.toFloat(), currentBudget)
        } else if (spent >= currentBudget * 0.8) {
            notificationHelper.sendBudgetApproachingNotification(spent.toFloat(), currentBudget)
        }
    }
}
