package com.example.mynilu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynilu.CategoryWiseSpendingActivity
import com.example.mynilu.R

class CategoryAdapter(private val categoryList: List<CategoryWiseSpendingActivity.Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryName.text = category.name
        holder.categoryAmount.text = "Rs${category.amount}"
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    // ViewHolder class for the category item
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val categoryAmount: TextView = itemView.findViewById(R.id.categoryAmount)
    }
}
