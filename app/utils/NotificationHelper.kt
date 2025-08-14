package com.example.mynilu.utils



import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.mynilu.R

class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "budget_channel"
        private const val CHANNEL_NAME = "Budget Alerts"
    }

    fun sendBudgetLimitNotification(amountSpent: Float, budgetLimit: Float) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies when spending exceeds the budget limit"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Budget Limit Exceeded")
            .setContentText("You've spent Rs. $amountSpent out of your Rs. $budgetLimit budget.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(1, builder.build())
    }
}
