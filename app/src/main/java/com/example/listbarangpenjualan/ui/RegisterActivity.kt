package com.example.listbarangpenjualan.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listbarangpenjualan.R
import com.example.listbarangpenjualan.model.DatabaseHelper

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var namaPerusahaanEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        dbHelper = DatabaseHelper(this)
        namaPerusahaanEditText = findViewById(R.id.rg_isiform_nama_perusahaan)
        emailEditText = findViewById(R.id.rg_isiform_email)
        passwordEditText = findViewById(R.id.rg_isiform_password)

        val registerButton: Button = findViewById(R.id.rg_button_daftar)
        val loginText: TextView = findViewById(R.id.rg_login)

        registerButton.setOnClickListener {
            val namaPerusahaan = namaPerusahaanEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (isValidCredentials(namaPerusahaan, email, password)) {
                if (registerUser(namaPerusahaan, email, password)) {
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showToast("Registrasi gagal. Email sudah digunakan.")
                }
            }
        }

        loginText.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isValidCredentials(namaPerusahaan: String, email: String, password: String): Boolean {
        if (namaPerusahaan.isBlank() || email.isBlank() || password.isBlank()) {
            showToast("Semua kolom harus diisi.")
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Format email tidak valid.")
            return false
        }

        return true
    }


    private fun registerUser(namaPerusahaan: String, email: String, password: String): Boolean {
        if (!checkEmailExist(email)) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(DatabaseHelper.UserEntry.COLUMN_NAMA, namaPerusahaan)
                put(DatabaseHelper.UserEntry.COLUMN_EMAIL, email)
                put(DatabaseHelper.UserEntry.COLUMN_PASSWORD, password)
            }

            val newRowId = db.insert(DatabaseHelper.UserEntry.TABLE_NAME_USERS, null, values)
            db.close()

            if (newRowId != -1L) {
                showToast("Registrasi berhasil. Silakan login.")
                return true
            } else {
                showToast("Registrasi gagal. Terjadi kesalahan.")
                return false
            }
        } else {
            return false
        }
    }

    private fun checkEmailExist(email: String): Boolean {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(DatabaseHelper.UserEntry.COLUMN_EMAIL)
        val selection = "${DatabaseHelper.UserEntry.COLUMN_EMAIL} = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            DatabaseHelper.UserEntry.TABLE_NAME_USERS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val emailExists = cursor.count > 0
        cursor.close()
        db.close()

        return emailExists
    }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }
}
