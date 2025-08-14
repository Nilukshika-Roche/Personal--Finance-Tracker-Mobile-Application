package com.example.mynilu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mynilu.utils.SharedPrefHelper
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class Dashboard : AppCompatActivity() {

    private lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        sharedPrefHelper = SharedPrefHelper(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = intent.getStringExtra("username")
        val textView = findViewById<TextView>(R.id.textViewHello)
        textView.text = "Hello $username"

        // Settings icon click - Go to Profile Settings
        val settingsBtn = findViewById<ImageView>(R.id.imageView11)
        settingsBtn.setOnClickListener {
            val intent = Intent(this, ProfileSettings::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        // Set up the Pie chart
        val pieChart: PieChart = findViewById(R.id.pieChart)
        setupPieChart(pieChart)

        val logo1 = findViewById<ImageView>(R.id.imageView8)
        logo1.setOnClickListener {
            startActivity(Intent(this, ViewTransactionsActivity::class.java))
            finish()
        }
        val logo2 = findViewById<ImageView>(R.id.imageView10)
        logo2.setOnClickListener {
            startActivity(Intent(this, CategoryWiseSpendingActivity::class.java))
            finish()
        }
        val logo3 = findViewById<ImageView>(R.id.imageView9)
        logo3.setOnClickListener {
            startActivity(Intent(this, BudgetSettingsActivity::class.java))
            finish()
        }
        val logo4 = findViewById<ImageView>(R.id.imageView12)
        logo4.setOnClickListener {
            startActivity(Intent(this, SummaryActivity::class.java))
            finish()
        }
    }

    private fun setupPieChart(pieChart: PieChart) {
        // Retrieve the total amounts by category from SharedPrefHelper
        val categoryData = sharedPrefHelper.getTotalAmountByCategory()

        // Prepare PieEntries for the chart
        val entries = categoryData.map { (category, amount) ->
            PieEntry(amount.toFloat(), category)
        }

        // Define the colors directly in the code
        val colors = listOf(
            0xFF0000FF.toInt(), // Blue
            0xFFFF8C00.toInt(), // Dark Orange
            0xFFFFD700.toInt(), // Gold
            0xFF98FB98.toInt(), // Pale Green
            0xFF40E0D0.toInt()  // Turquoise
        )

        // Create a PieDataSet and style it
        val dataSet = PieDataSet(entries, "Categories")
        dataSet.setColors(*colors.toIntArray()) // Set colors directly in the code

        dataSet.setValueTextSize(12f) // Keep value text size

        // Apply custom ValueFormatter to PieDataSet directly
        val valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.toInt()}%" // Format the value as an integer percentage
            }
        }
        dataSet.valueFormatter = valueFormatter

        // Set the value text color to white
        dataSet.setValueTextColor(0xFF000000.toInt()) // Set percentage value text color to white

        // Set data to the chart
        val pieData = PieData(dataSet)
        pieChart.data = pieData

        // Customize chart appearance
        pieChart.description.isEnabled = false // Disable chart description
        pieChart.setUsePercentValues(true) // Show percentage values
        pieChart.setDrawEntryLabels(false) // Hide text labels inside pie slices

        pieChart.invalidate() // Refresh chart
    }
}
