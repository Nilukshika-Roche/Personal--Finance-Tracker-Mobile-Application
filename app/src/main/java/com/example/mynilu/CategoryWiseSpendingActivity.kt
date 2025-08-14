package com.example.mynilu


import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynilu.adapters.CategoryAdapter
import com.example.mynilu.utils.SharedPrefHelper

class CategoryWiseSpendingActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_wise_spending)

        sharedPrefHelper = SharedPrefHelper(this)
        recyclerView = findViewById(R.id.recyclerView)

        // Set up the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        categoryAdapter = CategoryAdapter(getCategoryList())
        recyclerView.adapter = categoryAdapter
        // Add the logo click to go back to Dashboard
        val logo = findViewById<ImageView>(R.id.imageView51)
        logo.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }
    }

    private fun getCategoryList(): List<Category> {
        // Retrieve the total amounts by category from SharedPrefHelper
        val categoryData = sharedPrefHelper.getTotalAmountByCategory()

        // Convert the map to a list of Category objects
        return categoryData.map { (category, amount) ->
            Category(category, amount.toFloat())
        }
    }

    // Sample Category class to hold category data
    data class Category(val name: String, val amount: Float)
}
