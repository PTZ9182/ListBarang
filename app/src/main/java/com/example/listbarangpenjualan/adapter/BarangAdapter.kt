package com.example.listbarangpenjualan.adapter

import com.example.listbarangpenjualan.model.ModelBarang
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listbarangpenjualan.R

class BarangAdapter(internal val dataList: MutableList<ModelBarang>, private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<BarangAdapter.ViewHolder>() {

    // Metode lainnya

    fun updateData(updatedData: ModelBarang) {
        for (index in dataList.indices) {
            if (dataList[index].nomor == updatedData.nomor) {
                dataList[index] = updatedData
                notifyItemChanged(index)
                break
            }
        }
    }

    interface ItemClickListener {
        fun onEditClick(data: ModelBarang)
        fun onDeleteClick(data: ModelBarang)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaBarang: TextView = itemView.findViewById(R.id.idbarang)
        val namaBarang2: TextView = itemView.findViewById(R.id.barang)
        val kodeBarang: TextView = itemView.findViewById(R.id.kode_barang)
        val jumlahBarang: TextView = itemView.findViewById(R.id.jumlah_barang)
        val totalBarang: TextView = itemView.findViewById(R.id.total_barang)
        val editButton: Button = itemView.findViewById(R.id.edit_button_barang)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button_barang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_barang, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.namaBarang.text = data.nomor
        holder.namaBarang2.text = data.kode
        holder.kodeBarang.text = data.harga
        holder.jumlahBarang.text = data.jumlah
        holder.totalBarang.text = data.total

        holder.editButton.setOnClickListener {
            itemClickListener.onEditClick(data)
        }

        holder.deleteButton.setOnClickListener {
            itemClickListener.onDeleteClick(data)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
