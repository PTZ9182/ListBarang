package com.example.listbarangpenjualan.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listbarangpenjualan.databinding.ActivityTambahBarangBinding
import com.example.listbarangpenjualan.model.DatabaseHelper
import com.example.listbarangpenjualan.model.ModelBarang


class TambahBarangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahBarangBinding
    private lateinit var dbHelper: DatabaseHelper
    private var isEditMode = false
    private lateinit var dataToEdit: ModelBarang

    companion object {
        const val EXTRA_DATA_BARANG = "extra_data_barang"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        dbHelper = DatabaseHelper(this)

        binding.buttonSimpanBarang.setOnClickListener {
            if (isEditMode) {
                updateDataToDatabase()
            } else {
                saveDataToDatabase()
            }
        }

        val intent = intent
        if (intent.hasExtra(TambahBarangActivity.EXTRA_DATA_BARANG)) {
            isEditMode = true
            dataToEdit = intent.getParcelableExtra(TambahBarangActivity.EXTRA_DATA_BARANG) ?: ModelBarang("", "", "", "", "")
            setupEditMode()
        }
    }

    private fun setupEditMode() {
        binding.formBarangNomor.setText(dataToEdit.nomor)
        binding.formBarangKode.setText(dataToEdit.kode)
        binding.formBarangHarga.setText(dataToEdit.harga)
        binding.formBarangJumlah.setText(dataToEdit.jumlah)
        binding.formBarangTotal.setText(dataToEdit.total)
    }

    private fun saveDataToDatabase() {
        val nomor = binding.formBarangNomor.text.toString().trim()
        val jumlah = binding.formBarangJumlah.text.toString().trim()
        val kode = binding.formBarangKode.text.toString().trim()
        val harga = binding.formBarangHarga.text.toString().trim()
        val total = binding.formBarangTotal.text.toString().trim()

        // Validasi input
        if (isInputValid(nomor, jumlah, kode, harga, total)) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(DatabaseHelper.BarangEntry.COLUMN_NOMOR, nomor)
                put(DatabaseHelper.BarangEntry.COLUMN_JUMLAH, jumlah)
                put(DatabaseHelper.BarangEntry.COLUMN_KODE, kode)
                put(DatabaseHelper.BarangEntry.COLUMN_HARGA, harga)
                put(DatabaseHelper.BarangEntry.COLUMN_TOTAL, total)
            }

            db.insert(DatabaseHelper.BarangEntry.TABLE_NAME, null, values)
            db.close()

            val intent = Intent().apply {
                putExtra(TambahListActivity.EXTRA_DATA, ModelBarang(nomor, jumlah, kode, harga, total))
                putExtra(BarangActivity.EXTRA_ACTION, BarangActivity.ACTION_ADD)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun updateDataToDatabase() {
        val nomor = binding.formBarangNomor.text.toString().trim()
        val jumlah = binding.formBarangJumlah.text.toString().trim()
        val kode = binding.formBarangKode.text.toString().trim()
        val harga = binding.formBarangHarga.text.toString().trim()
        val total = binding.formBarangTotal.text.toString().trim()

        // Validasi input
        if (isInputValid(nomor, jumlah, kode, harga, total)) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(DatabaseHelper.BarangEntry.COLUMN_NOMOR, nomor)
                put(DatabaseHelper.BarangEntry.COLUMN_JUMLAH, jumlah)
                put(DatabaseHelper.BarangEntry.COLUMN_KODE, kode)
                put(DatabaseHelper.BarangEntry.COLUMN_HARGA, harga)
                put(DatabaseHelper.BarangEntry.COLUMN_TOTAL, total)
            }

            db.update(
                DatabaseHelper.BarangEntry.TABLE_NAME,
                values,
                "${DatabaseHelper.BarangEntry.COLUMN_NOMOR} = ?",
                arrayOf(dataToEdit.nomor)
            )

            db.close()

            val intent = Intent().apply {
                putExtra(TambahListActivity.EXTRA_DATA, ModelBarang(nomor, jumlah, kode,harga, total))
                putExtra(BarangActivity.EXTRA_ACTION, BarangActivity.ACTION_UPDATE)
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


        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
