package com.example.listbarangpenjualan.model

import android.os.Parcel
import android.os.Parcelable

data class DataModel(
    val nomor: String,
    val tanggal: String,
    val kode: String,
    val nama: String,
    val noTelp: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nomor)
        parcel.writeString(tanggal)
        parcel.writeString(kode)
        parcel.writeString(nama)
        parcel.writeString(noTelp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataModel> {
        override fun createFromParcel(parcel: Parcel): DataModel {
            return DataModel(parcel)
        }

        override fun newArray(size: Int): Array<DataModel?> {
            return arrayOfNulls(size)
        }
    }
}

