package com.example.listbarangpenjualan.ui

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listbarangpenjualan.MainActivity
import com.example.listbarangpenjualan.databinding.ActivityTambahListBinding
import com.example.listbarangpenjualan.model.DataModel
import com.example.listbarangpenjualan.model.DatabaseHelper
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TambahListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahListBinding
    private lateinit var dbHelper: DatabaseHelper
    private var isEditMode = false
    private lateinit var dataToEdit: DataModel
    private val calendar = Calendar.getInstance()

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        dbHelper = DatabaseHelper(this)

        binding.buttonSimpan.setOnClickListener {
            if (isEditMode) {
                updateDataToDatabase()
            } else {
                saveDataToDatabase()
            }
        }

        val intent = intent
        if (intent.hasExtra(EXTRA_DATA)) {
            isEditMode = true
            dataToEdit = intent.getParcelableExtra(EXTRA_DATA) ?: DataModel("", "", "", "", "")
            setupEditMode()
        }
        binding.formTanggal.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                // Set selected date to EditText
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                binding.formTanggal.setText(
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                        selectedDate.time
                    )
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun setupEditMode() {
        binding.formNomor.setText(dataToEdit.nomor)
        binding.formTanggal.setText(dataToEdit.tanggal)
        binding.formKode.setText(dataToEdit.kode)
        binding.formNama.setText(dataToEdit.nama)
        binding.formNotelp.setText(dataToEdit.noTelp)
    }

    private fun saveDataToDatabase() {
        val nomor = binding.formNomor.text.toString().trim()
        val tanggal = binding.formTanggal.text.toString().trim()
        val kode = binding.formKode.text.toString().trim()
        val nama = binding.formNama.text.toString().trim()
        val noTelp = binding.formNotelp.text.toString().trim()

        // Validasi input
        if (isInputValid(nomor, tanggal, kode, nama, noTelp)) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(DatabaseHelper.FeedEntry.COLUMN_NOMOR, nomor)
                put(DatabaseHelper.FeedEntry.COLUMN_TANGGAL, tanggal)
                put(DatabaseHelper.FeedEntry.COLUMN_KODE, kode)
                put(DatabaseHelper.FeedEntry.COLUMN_NAMA, nama)
                put(DatabaseHelper.FeedEntry.COLUMN_NOTELP, noTelp)
            }

            db.insert(DatabaseHelper.FeedEntry.TABLE_NAME, null, values)
            db.close()

            val intent = Intent().apply {
                putExtra(EXTRA_DATA, DataModel(nomor, tanggal, kode, nama, noTelp))
                putExtra(MainActivity.EXTRA_ACTION, MainActivity.ACTION_ADD)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun updateDataToDatabase() {
        val nomor = binding.formNomor.text.toString().trim()
        val tanggal = binding.formTanggal.text.toString().trim()
        val kode = binding.formKode.text.toString().trim()
        val nama = binding.formNama.text.toString().trim()
        val noTelp = binding.formNotelp.text.toString().trim()

        // Validasi input
        if (isInputValid(nomor, tanggal, kode, nama, noTelp)) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(DatabaseHelper.FeedEntry.COLUMN_NOMOR, nomor)
                put(DatabaseHelper.FeedEntry.COLUMN_TANGGAL, tanggal)
                put(DatabaseHelper.FeedEntry.COLUMN_KODE, kode)
                put(DatabaseHelper.FeedEntry.COLUMN_NAMA, nama)
                put(DatabaseHelper.FeedEntry.COLUMN_NOTELP, noTelp)
            }

            db.update(
                DatabaseHelper.FeedEntry.TABLE_NAME,
                values,
                "${DatabaseHelper.FeedEntry.COLUMN_NOMOR} = ?",
                arrayOf(dataToEdit.nomor)
            )

            db.close()

            val intent = Intent().apply {
                putExtra(EXTRA_DATA, DataModel(nomor, tanggal, kode, nama, noTelp))
                putExtra(MainActivity.EXTRA_ACTION, MainActivity.ACTION_UPDATE)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun isInputValid(
        nomor: String,
        tanggal: String,
        kode: String,
        nama: String,
        noTelp: String
    ): Boolean {
        if (nomor.isBlank() || tanggal.isBlank() || kode.isBlank() || nama.isBlank() || noTelp.isBlank()) {
            showToast("Harap isi semua kolom")
            return false
        }

        // Validasi format tanggal
        if (!isDateValid(tanggal)) {
            showToast("Format tanggal tidak valid")
            return false
        }

        // Tambahan validasi lainnya sesuai kebutuhan

        return true
    }

    private fun isDateValid(date: String): Boolean {
        try {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            dateFormat.isLenient = false
            dateFormat.parse(date)
            return true
        } catch (e: ParseException) {
            return false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
