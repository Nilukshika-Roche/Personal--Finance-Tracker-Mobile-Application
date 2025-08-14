package com.example.mynilu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class Login : AppCompatActivity() {

    private lateinit var etLoginUsername: EditText
    private lateinit var etLoginPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvSignup: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etLoginUsername = findViewById(R.id.etLoginUsername)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvSignup = findViewById(R.id.tvSignup)

        tvSignup.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val username = etLoginUsername.text.toString().trim()
            val password = etLoginPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            } else {
                if (isValidLogin(username, password)) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                    // ✅ Passing username to Dashboard
                    val intent = Intent(this, Dashboard::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isValidLogin(username: String, password: String): Boolean {
        try {
            val fileInputStream = openFileInput("users.txt")
            val bufferedReader = BufferedReader(InputStreamReader(fileInputStream))
            var line: String? = bufferedReader.readLine()

            while (line != null) {
                val userData = line.split(":")
                if (userData.size == 4 && userData[2] == username && userData[3] == password) {
                    return true
                }
                line = bufferedReader.readLine()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}
