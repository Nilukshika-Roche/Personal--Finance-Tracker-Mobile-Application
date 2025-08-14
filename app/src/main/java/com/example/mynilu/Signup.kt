package com.example.mynilu



import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Signup : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnSignup = findViewById(R.id.btnSignup)

        btnSignup.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            when {
                fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() ->
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()

                password.length < 6 ->
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()

                else -> {
                    val userData = "$fullName:$email:$username:$password\n"
                    openFileOutput("users.txt", MODE_APPEND).use {
                        it.write(userData.toByteArray())
                    }
                    Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()

                    // Navigate to Login activity
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
