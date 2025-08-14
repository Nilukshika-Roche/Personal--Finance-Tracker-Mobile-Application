package com.example.mynilu.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefHelper(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("FinancePrefs", Context.MODE_PRIVATE)

    fun saveBudget(budget: Float) {
        with(sharedPref.edit()) {
            putFloat("monthly_budget", budget)
            apply()
        }
    }

    fun getBudget(): Float {
        return sharedPref.getFloat("monthly_budget", 0f)
    }

    fun saveCurrency(currency: String) {
        with(sharedPref.edit()) {
            putString("currency_type", currency)
            apply()
        }
    }

    fun getCurrency(): String? {
        return sharedPref.getString("currency_type", "Rs.")
    }
}
