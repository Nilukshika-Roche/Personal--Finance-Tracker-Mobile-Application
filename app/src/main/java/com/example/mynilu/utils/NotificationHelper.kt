package com.example.mynilu.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.mynilu.R

class NotificationHelper(val context: Context) {

    private val CHANNEL_ID = "budget_notifications"

    init {
        // Create the notification channel for Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Budget Notifications"
            val descriptionText = "Notifications for budget limit and approaching notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Method to send the budget limit notification
    fun sendBudgetLimitNotification(currentAmount: Float, budgetAmount: Float) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Budget Limit Exceeded")
            .setContentText("You have exceeded your budget of $budgetAmount. Current spend: $currentAmount")
            .setSmallIcon(R.drawable.noti)  // Replace with your own icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }

    // Method to send the budget approaching notification
    fun sendBudgetApproachingNotification(currentAmount: Float, budgetAmount: Float) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Budget Approaching Limit")
            .setContentText("You have spent $currentAmount, approaching your budget of $budgetAmount.")
            .setSmallIcon(R.drawable.noti)  // Replace with your own icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(2, notification)
    }
}
