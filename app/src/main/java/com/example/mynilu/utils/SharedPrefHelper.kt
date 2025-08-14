package com.example.mynilu.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.mynilu.data.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter


class SharedPrefHelper(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("FinancePrefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    fun saveBudget(budget: Float) {
        with(sharedPref.edit()) {
            putFloat("monthly_budget", budget)
            apply()
        }
    }
    // Export transactions to internal storage
    fun exportTransactionsToFile(context: Context): Boolean {
        return try {
            val transactions = getTransactions()
            val json = gson.toJson(transactions)

            val file = File(context.filesDir, "transactions_backup.json")
            val writer = FileWriter(file)
            writer.use {
                it.write(json)
            }

            Log.d("SharedPrefHelper", "Backup created at: ${file.absolutePath}")
            true
        } catch (e: Exception) {
            Log.e("SharedPrefHelper", "Error exporting transactions", e)
            false
        }
    }

    // Import transactions from internal storage
    fun importTransactionsFromFile(context: Context): Boolean {
        return try {
            val file = File(context.filesDir, "transactions_backup.json")
            if (!file.exists()) return false

            val reader = FileReader(file)
            val json = reader.readText()
            reader.close()

            val type = object : TypeToken<List<Transaction>>() {}.type
            val transactions: List<Transaction> = gson.fromJson(json, type)

            saveTransactionList(transactions)

            Log.d("SharedPrefHelper", "Transactions restored from backup")
            true
        } catch (e: Exception) {
            Log.e("SharedPrefHelper", "Error importing transactions", e)
            false
        }
    }
    fun getBudget(): Float {
        return sharedPref.getFloat("monthly_budget", 0f)
    }




    fun addTransaction(transaction: Transaction) {
        val transactions = getTransactions().toMutableList()
        transactions.add(transaction)
        saveTransactionList(transactions)
    }

    fun getTransactions(): List<Transaction> {
        val json = sharedPref.getString("transactions", null)
        if (json.isNullOrEmpty()) return emptyList()

        val type = object : TypeToken<List<Transaction>>() {}.type
        return gson.fromJson(json, type)
    }

    fun updateTransaction(updatedTransaction: Transaction) {
        val transactions = getTransactions().toMutableList()
        val index = transactions.indexOfFirst { it.id == updatedTransaction.id }
        if (index != -1) {
            transactions[index] = updatedTransaction
            saveTransactionList(transactions)
        }
    }

    fun deleteTransaction(id: Long) {
        val transactions = getTransactions().toMutableList()
        val filtered = transactions.filter { it.id != id }
        saveTransactionList(filtered)
    }

    private fun saveTransactionList(transactions: List<Transaction>) {
        val json = gson.toJson(transactions)
        with(sharedPref.edit()) {
            putString("transactions", json)
            apply()
        }
    }
    fun isLimitNotificationSent(): Boolean {
        return sharedPref.getBoolean("limit_notification_sent", false)
    }

    fun setLimitNotificationSent(sent: Boolean) {
        sharedPref.edit().putBoolean("limit_notification_sent", sent).apply()
    }

    fun isApproachingNotificationSent(): Boolean {
        return sharedPref.getBoolean("approaching_notification_sent", false)
    }

    fun setApproachingNotificationSent(sent: Boolean) {
        sharedPref.edit().putBoolean("approaching_notification_sent", sent).apply()
    }

    // ✅ Expense only
    fun getTotalSpent(): Double {
        val transactions = getTransactions()
        val totalSpent = transactions
            .filter { it.title.equals("expense", ignoreCase = true) }
            .sumOf { it.amount }

        Log.d("SharedPrefHelper", "Total spent: $totalSpent")
        return totalSpent
    }

    // Save Currency
    fun saveCurrency(currency: String) {
        sharedPref.edit().putString("currency", currency).apply()  // Correct approach
    }

    fun getCurrency(): String? {
        return sharedPref.getString("currency", "Rs.")
    }
    fun deleteBudget() {
        with(sharedPref.edit()) {
            remove("monthly_budget")
            apply()
        }
    }
    // New method to get total spending by category
    fun getTotalAmountByCategory(): Map<String, Double> {
        val transactions = getTransactions()

        // Calculate the total spending per category
        val categoryMap = mutableMapOf<String, Double>()
        for (transaction in transactions) {
            val currentAmount = categoryMap.getOrDefault(transaction.category, 0.0)
            categoryMap[transaction.category] = currentAmount + transaction.amount
        }

        return categoryMap
    }
    // New method to update the budget
    fun updateBudget(budget: Float) {
        saveBudget(budget) // Save the new budget
    }
}
