package com.example.listbarangpenjualan.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listbarangpenjualan.MainActivity
import com.example.listbarangpenjualan.R
import com.example.listbarangpenjualan.model.DatabaseHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        dbHelper = DatabaseHelper(this)
        emailEditText = findViewById(R.id.lk_isiform_email)
        passwordEditText = findViewById(R.id.lk_isiform_password)

        val loginButton: Button = findViewById(R.id.lk_button_login)
        val registerText: TextView = findViewById(R.id.lk_regis)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (isValidCredentials(email, password)) {
                if (checkUser(email, password)) {
                    // Login berhasil, pindah ke MainActivity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showToast("Login gagal. Cek kembali email dan password.")
                }
            } else {
                showToast("Email dan password harus diisi.")
            }
        }

        registerText.setOnClickListener {
            // Pindah ke RegisterActivity
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isValidCredentials(email: String, password: String): Boolean {
        // Validasi email dan password, Anda bisa menambahkan validasi sesuai kebutuhan
        if (email.isBlank() || password.isBlank()) {
            showToast("Email dan password harus diisi.")
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Format email tidak valid.")
            return false
        }

        return true
    }


    private fun checkUser(email: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(DatabaseHelper.UserEntry.COLUMN_EMAIL, DatabaseHelper.UserEntry.COLUMN_PASSWORD)
        val selection = "${DatabaseHelper.UserEntry.COLUMN_EMAIL} = ? AND ${DatabaseHelper.UserEntry.COLUMN_PASSWORD} = ?"
        val selectionArgs = arrayOf(email, password)

        val cursor = db.query(
            DatabaseHelper.UserEntry.TABLE_NAME_USERS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val userExists = cursor.count > 0
        cursor.close()
        db.close()

        return userExists
    }

    private fun showToast(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }
}
