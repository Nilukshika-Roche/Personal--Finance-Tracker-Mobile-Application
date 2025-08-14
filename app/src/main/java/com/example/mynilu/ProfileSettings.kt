package com.example.mynilu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileSettings : AppCompatActivity() {

    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var updateBtn: Button
    private var currentUsername: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        usernameField = findViewById(R.id.editUsername)
        passwordField = findViewById(R.id.editPassword)
        updateBtn = findViewById(R.id.updateBtn)

        // Get the current username passed from previous activity
        currentUsername = intent.getStringExtra("username")

        if (currentUsername != null) {
            loadUserDetails(currentUsername!!)
        }

        // Update user details when "Update" button is clicked
        updateBtn.setOnClickListener {
            updateUserDetails()
        }
    }

    // Load user details
    private fun loadUserDetails(username: String) {
        try {
            val lines = openFileInput("users.txt").bufferedReader().readLines()
            for (line in lines) {
                val parts = line.split(" ")
                if (parts.size >= 2) {
                    val user = parts[0]
                    val pass = parts[1]
                    if (user == username) {
                        usernameField.setText(user)
                        passwordField.setText(pass)
                        break
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load user details", Toast.LENGTH_SHORT).show()
        }
    }

    // Update user details in the file
    private fun updateUserDetails() {
        val newUsername = usernameField.text.toString().trim()
        val newPassword = passwordField.text.toString().trim()

        if (newUsername.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val lines = openFileInput("users.txt").bufferedReader().readLines()

            // Update the user details
            val updatedLines = lines.map { line ->
                val parts = line.split(" ")
                if (parts.size >= 2) {
                    val user = parts[0]
                    if (user == currentUsername) {
                        "$newUsername $newPassword"
                    } else {
                        line
                    }
                } else {
                    line
                }
            }

            // Write the updated lines back to the file
            openFileOutput("users.txt", Context.MODE_PRIVATE).bufferedWriter().use { writer ->
                updatedLines.forEach { updatedLine ->
                    writer.write("$updatedLine\n")
                }
            }

            Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()

            // After update, clear session and force login again
            val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to update details", Toast.LENGTH_SHORT).show()
        }
    }
}
