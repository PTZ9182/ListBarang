package com.example.listbarangpenjualan.ui

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listbarangpenjualan.MainActivity
import com.example.listbarangpenjualan.R
import com.example.listbarangpenjualan.adapter.BarangAdapter
import com.example.listbarangpenjualan.databinding.ActivityBarangBinding
import com.example.listbarangpenjualan.model.DatabaseHelper
import com.example.listbarangpenjualan.model.ModelBarang
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class BarangActivity : AppCompatActivity(), BarangAdapter.ItemClickListener {
    private lateinit var binding: ActivityBarangBinding
    private lateinit var dbHelper: DatabaseHelper

    companion object {
        const val REQUEST_ADD_EDIT = 123
        const val EXTRA_ACTION = "extra_action"
        const val ACTION_ADD = "action_add"
        const val ACTION_UPDATE = "action_update"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, TambahBarangActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_EDIT)
        }

        val bottomNavigationView = binding.bottomNavigationViewBarang
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.activityMain -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.activityBarang -> {
                    // Navigasi ke BarangActivity
                    showMessage("List Barang")
                    true
                }
                else -> false
            }
        }
        dbHelper = DatabaseHelper(this)

        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            DatabaseHelper.BarangEntry.COLUMN_NOMOR,
            DatabaseHelper.BarangEntry.COLUMN_JUMLAH,
            DatabaseHelper.BarangEntry.COLUMN_KODE,
            DatabaseHelper.BarangEntry.COLUMN_HARGA,
            DatabaseHelper.BarangEntry.COLUMN_TOTAL
        )

        val cursor: Cursor? = db.query(
            DatabaseHelper.BarangEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val recyclerView = findViewById<RecyclerView>(R.id.data_latihan2)

        val dataList = ArrayList<ModelBarang>()

        cursor?.let {
            while (it.moveToNext()) {
                val nomor = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.BarangEntry.COLUMN_NOMOR))
                val jumlah= it.getString(it.getColumnIndexOrThrow(DatabaseHelper.BarangEntry.COLUMN_JUMLAH))
                val kode = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.BarangEntry.COLUMN_KODE))
                val harga = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.BarangEntry.COLUMN_HARGA))
                val total = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.BarangEntry.COLUMN_TOTAL))

                val data = ModelBarang(nomor, jumlah, kode, harga, total)
                dataList.add(data)
            }
        }

        cursor?.close()

        val adapter = BarangAdapter(dataList, this)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)
    }



    override fun onEditClick(data: ModelBarang) {
        // Implementasi aksi ketika tombol Edit diklik
        // Buka TambahListActivity untuk mode edit dengan data yang dipilih
        val intent = Intent(this, TambahBarangActivity::class.java)
        intent.putExtra(TambahBarangActivity.EXTRA_DATA_BARANG, data)
        startActivity(intent)
    }

    override fun onDeleteClick(data: ModelBarang) {
        // Implementasikan logika hapus di sini
        deleteDataFromDatabase(data)
        showMessage("Hapus diklik untuk item: ${data.kode}")
    }

    private fun deleteDataFromDatabase(data: ModelBarang) {
        val db = dbHelper.writableDatabase
        val selection = "${DatabaseHelper.BarangEntry.COLUMN_NOMOR} LIKE ?"
        val selectionArgs = arrayOf(data.nomor)

        db.delete(DatabaseHelper.BarangEntry.TABLE_NAME, selection, selectionArgs)
        db.close()

        // Perbarui tampilan RecyclerView setelah menghapus data
        refreshDataInView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_EDIT && resultCode == RESULT_OK) {
            when (data?.getStringExtra(EXTRA_ACTION)) {
                ACTION_ADD -> {
                    data?.getParcelableExtra<ModelBarang>(TambahBarangActivity.EXTRA_DATA_BARANG)?.let { addedData ->
                        // Tambahkan data ke adapter
                        addDataToRecyclerView(addedData)
                    }
                }
                ACTION_UPDATE -> {
                    data?.getParcelableExtra<ModelBarang>(TambahBarangActivity.EXTRA_DATA_BARANG)?.let { updatedData ->
                        // Perbarui data di adapter
                        updateDataInRecyclerView(updatedData)
                    }
                }
            }
        }
    }

    private fun addDataToRecyclerView(addedData: ModelBarang) {
        val recyclerView = findViewById<RecyclerView>(R.id.data_latihan2)
        val adapter = recyclerView.adapter as? BarangAdapter

        adapter?.dataList?.add(addedData)
        adapter?.notifyItemInserted(adapter.dataList.size - 1)
        checkEmptyState()
    }

    private fun updateDataInRecyclerView(updatedData: ModelBarang) {
        val recyclerView = findViewById<RecyclerView>(R.id.data_latihan2)
        val adapter = recyclerView.adapter as? BarangAdapter

        adapter?.updateData(updatedData)
        adapter?.notifyDataSetChanged()
        checkEmptyState()
    }

    private fun checkEmptyState() {
        val recyclerView = findViewById<RecyclerView>(R.id.data_latihan2)
        val emptyTextView = findViewById<TextView>(R.id.emptyTextView2)
        if (recyclerView.adapter?.itemCount == 0) {
            recyclerView.visibility = View.GONE
            emptyTextView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyTextView.visibility = View.GONE
        }
    }


    private fun refreshDataInView() {
        // Ambil data dari SQLite dan perbarui tampilan RecyclerView
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            DatabaseHelper.BarangEntry.COLUMN_NOMOR,
            DatabaseHelper.BarangEntry.COLUMN_JUMLAH,
            DatabaseHelper.BarangEntry.COLUMN_KODE,
            DatabaseHelper.BarangEntry.COLUMN_HARGA,
            DatabaseHelper.BarangEntry.COLUMN_TOTAL
        )

        val cursor: Cursor? = db.query(
            DatabaseHelper.BarangEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val dataList = ArrayList<ModelBarang>()

        cursor?.let {
            while (it.moveToNext()) {
                val nomor = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.BarangEntry.COLUMN_NOMOR))
                val jumlah = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.BarangEntry.COLUMN_JUMLAH))
                val kode = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.BarangEntry.COLUMN_KODE))
                val harga = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.BarangEntry.COLUMN_HARGA))
                val total = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.BarangEntry.COLUMN_TOTAL))

                val data = ModelBarang(nomor, jumlah, kode, harga, total)
                dataList.add(data)
            }
        }

        cursor?.close()

        // Perbarui adapter RecyclerView dengan data terbaru
        val recyclerView = findViewById<RecyclerView>(R.id.data_latihan2)
        val adapter = BarangAdapter(dataList, this)
        recyclerView.adapter = adapter

        val emptyTextView = findViewById<TextView>(R.id.emptyTextView2)
        if (dataList.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyTextView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyTextView.visibility = View.GONE
        }

    }

    override fun onResume() {
        super.onResume()
        refreshDataInView()
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
