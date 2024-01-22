package com.example.listbarangpenjualan

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listbarangpenjualan.adapter.DataAdapter
import com.example.listbarangpenjualan.databinding.ActivityMainBinding
import com.example.listbarangpenjualan.model.DataModel
import com.example.listbarangpenjualan.model.DatabaseHelper
import com.example.listbarangpenjualan.ui.BarangActivity
import com.example.listbarangpenjualan.ui.TambahListActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), DataAdapter.ItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper

    companion object {
        const val REQUEST_ADD_EDIT = 123
        const val EXTRA_ACTION = "extra_action"
        const val ACTION_ADD = "action_add"
        const val ACTION_UPDATE = "action_update"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, TambahListActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_EDIT)
        }

        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.activityMain -> {
                    showMessage("List Transaksi")
                    true
                }
                R.id.activityBarang -> {
                    // Navigasi ke BarangActivity
                    val intent = Intent(this, BarangActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        dbHelper = DatabaseHelper(this)

        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            DatabaseHelper.FeedEntry.COLUMN_NOMOR,
            DatabaseHelper.FeedEntry.COLUMN_TANGGAL,
            DatabaseHelper.FeedEntry.COLUMN_KODE,
            DatabaseHelper.FeedEntry.COLUMN_NAMA,
            DatabaseHelper.FeedEntry.COLUMN_NOTELP
        )

        val cursor: Cursor? = db.query(
            DatabaseHelper.FeedEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val recyclerView = findViewById<RecyclerView>(R.id.data_latihan)

        val dataList = ArrayList<DataModel>()

        cursor?.let {
            while (it.moveToNext()) {
                val nomor = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.COLUMN_NOMOR))
                val tanggal = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.COLUMN_TANGGAL))
                val kode = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.COLUMN_KODE))
                val nama = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.COLUMN_NAMA))
                val noTelp = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.COLUMN_NOTELP))

                val data = DataModel(nomor, tanggal, kode, nama, noTelp)
                dataList.add(data)
            }
        }

        cursor?.close()

        val adapter = DataAdapter(dataList, this)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)
    }



    override fun onEditClick(data: DataModel) {
        val intent = Intent(this, TambahListActivity::class.java)
        intent.putExtra(TambahListActivity.EXTRA_DATA, data)
        startActivity(intent)
    }

    override fun onDeleteClick(data: DataModel) {
        deleteDataFromDatabase(data)
        showMessage("Hapus diklik untuk item: ${data.nama}")
    }

    private fun deleteDataFromDatabase(data: DataModel) {
        val db = dbHelper.writableDatabase
        val selection = "${DatabaseHelper.FeedEntry.COLUMN_NOMOR} LIKE ?"
        val selectionArgs = arrayOf(data.nomor)

        db.delete(DatabaseHelper.FeedEntry.TABLE_NAME, selection, selectionArgs)
        db.close()

        refreshDataInView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_EDIT && resultCode == RESULT_OK) {
            when (data?.getStringExtra(EXTRA_ACTION)) {
                ACTION_ADD -> {
                    data?.getParcelableExtra<DataModel>(TambahListActivity.EXTRA_DATA)?.let { addedData ->
                        addDataToRecyclerView(addedData)
                    }
                }
                ACTION_UPDATE -> {
                    data?.getParcelableExtra<DataModel>(TambahListActivity.EXTRA_DATA)?.let { updatedData ->
                        updateDataInRecyclerView(updatedData)
                    }
                }
            }
        }
    }

    private fun addDataToRecyclerView(addedData: DataModel) {
        val recyclerView = findViewById<RecyclerView>(R.id.data_latihan)
        val adapter = recyclerView.adapter as? DataAdapter

        adapter?.dataList?.add(addedData)
        adapter?.notifyItemInserted(adapter.dataList.size - 1)
        checkEmptyState()
    }

    private fun updateDataInRecyclerView(updatedData: DataModel) {
        val recyclerView = findViewById<RecyclerView>(R.id.data_latihan)
        val adapter = recyclerView.adapter as? DataAdapter

        adapter?.updateData(updatedData)
        adapter?.notifyDataSetChanged()
        checkEmptyState()
    }

    private fun checkEmptyState() {
        val recyclerView = findViewById<RecyclerView>(R.id.data_latihan)
        val emptyTextView = findViewById<TextView>(R.id.emptyTextView)
        if (recyclerView.adapter?.itemCount == 0) {
            recyclerView.visibility = View.GONE
            emptyTextView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyTextView.visibility = View.GONE
        }
    }


    private fun refreshDataInView() {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            DatabaseHelper.FeedEntry.COLUMN_NOMOR,
            DatabaseHelper.FeedEntry.COLUMN_TANGGAL,
            DatabaseHelper.FeedEntry.COLUMN_KODE,
            DatabaseHelper.FeedEntry.COLUMN_NAMA,
            DatabaseHelper.FeedEntry.COLUMN_NOTELP
        )

        val cursor: Cursor? = db.query(
            DatabaseHelper.FeedEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val dataList = ArrayList<DataModel>()

        cursor?.let {
            while (it.moveToNext()) {
                val nomor = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.COLUMN_NOMOR))
                val tanggal = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.COLUMN_TANGGAL))
                val kode = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.COLUMN_KODE))
                val nama = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.COLUMN_NAMA))
                val noTelp = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.FeedEntry.COLUMN_NOTELP))

                val data = DataModel(nomor, tanggal, kode, nama, noTelp)
                dataList.add(data)
            }
        }

        cursor?.close()

        val recyclerView = findViewById<RecyclerView>(R.id.data_latihan)
        val adapter = DataAdapter(dataList, this)
        recyclerView.adapter = adapter

        val emptyTextView = findViewById<TextView>(R.id.emptyTextView)
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
