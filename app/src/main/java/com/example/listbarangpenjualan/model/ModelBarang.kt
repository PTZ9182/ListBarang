package com.example.listbarangpenjualan.model

import android.os.Parcel
import android.os.Parcelable

data class ModelBarang(
    val nomor: String,
    val jumlah: String,
    val kode: String,
    val harga: String,
    val total: String
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
        parcel.writeString(jumlah)
        parcel.writeString(kode)
        parcel.writeString(harga)
        parcel.writeString(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelBarang> {
        override fun createFromParcel(parcel: Parcel): ModelBarang {
            return ModelBarang(parcel)
        }

        override fun newArray(size: Int): Array<ModelBarang?> {
            return arrayOfNulls(size)
        }
    }
}
