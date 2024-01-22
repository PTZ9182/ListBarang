package com.example.listbarangpenjualan.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "MyDatabase.db"
        const val DATABASE_VERSION = 4
    }

    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "tabel_data"
        const val COLUMN_NOMOR = "nomor"
        const val COLUMN_TANGGAL = "tanggal"
        const val COLUMN_KODE = "kode"
        const val COLUMN_NAMA = "nama"
        const val COLUMN_NOTELP = "notelp"
    }

    object BarangEntry : BaseColumns {
        const val TABLE_NAME = "tabel_barang"
        const val COLUMN_NOMOR = "nomor"
        const val COLUMN_JUMLAH = "jumlah"
        const val COLUMN_KODE = "kode"
        const val COLUMN_HARGA = "harga"
        const val COLUMN_TOTAL = "total"
    }

    object UserEntry : BaseColumns {
        const val TABLE_NAME_USERS = "users"
        const val COLUMN_NAMA = "nama"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
    }

    private val SQL_CREATE_ENTRIES_DATA =
        "CREATE TABLE ${FeedEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedEntry.COLUMN_NOMOR} TEXT," +
                "${FeedEntry.COLUMN_TANGGAL} TEXT," +
                "${FeedEntry.COLUMN_KODE} TEXT," +
                "${FeedEntry.COLUMN_NAMA} TEXT," +
                "${FeedEntry.COLUMN_NOTELP} TEXT)"

    private val SQL_CREATE_ENTRIES_BARANG =
        "CREATE TABLE ${BarangEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${BarangEntry.COLUMN_NOMOR} TEXT," +
                "${BarangEntry.COLUMN_JUMLAH} TEXT," +
                "${BarangEntry.COLUMN_KODE} TEXT," +
                "${BarangEntry.COLUMN_HARGA} TEXT," +
                "${BarangEntry.COLUMN_TOTAL} TEXT)"

    private val SQL_CREATE_ENTRIES_USERS =
        "CREATE TABLE ${UserEntry.TABLE_NAME_USERS} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${UserEntry.COLUMN_NAMA} TEXT," +
                "${UserEntry.COLUMN_EMAIL} TEXT UNIQUE," +
                "${UserEntry.COLUMN_PASSWORD} TEXT)"

    private val SQL_DELETE_ENTRIES_DATA = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_NAME}"
    private val SQL_DELETE_ENTRIES_BARANG = "DROP TABLE IF EXISTS ${BarangEntry.TABLE_NAME}"
    private val SQL_DELETE_ENTRIES_USERS = "DROP TABLE IF EXISTS ${UserEntry.TABLE_NAME_USERS}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES_DATA)
        db.execSQL(SQL_CREATE_ENTRIES_BARANG)
        db.execSQL(SQL_CREATE_ENTRIES_USERS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES_DATA)
        db.execSQL(SQL_DELETE_ENTRIES_BARANG)
        db.execSQL(SQL_DELETE_ENTRIES_USERS)
        onCreate(db)
    }
}

