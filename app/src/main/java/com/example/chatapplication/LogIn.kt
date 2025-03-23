package com.example.chatapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapplication.utils.FirebaseHelper

class LogIn : AppCompatActivity() {

    private lateinit var emailEditText: EditText // Input field for email
    private lateinit var passwordEditText: EditText // Input field for password
    private lateinit var loginButton: Button // Button to log in
    private lateinit var signupButton: Button // Button to navigate to SignUp page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide()

        emailEditText = findViewById(R.id.edt_Email)
        passwordEditText = findViewById(R.id.edt_Password)
        loginButton = findViewById(R.id.btn_login)
        signupButton = findViewById(R.id.btn_signup)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Authenticate the user with Firebase
                FirebaseHelper.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to SignUp activity when signup button is clicked
        signupButton.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }
}