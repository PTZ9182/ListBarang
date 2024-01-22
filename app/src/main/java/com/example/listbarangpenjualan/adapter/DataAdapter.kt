package com.example.listbarangpenjualan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listbarangpenjualan.R
import com.example.listbarangpenjualan.model.DataModel

class DataAdapter(internal val dataList: MutableList<DataModel>, private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    // Metode lainnya

    fun updateData(updatedData: DataModel) {
        for (index in dataList.indices) {
            if (dataList[index].nomor == updatedData.nomor) {
                dataList[index] = updatedData
                notifyItemChanged(index)
                break
            }
        }
    }

    interface ItemClickListener {
        fun onEditClick(data: DataModel)
        fun onDeleteClick(data: DataModel)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaLatihan: TextView = itemView.findViewById(R.id.kodemain)
        val kesusahan: TextView = itemView.findViewById(R.id.cust)
        val tantangan2: TextView = itemView.findViewById(R.id.cust2)
        val tantangan3: TextView = itemView.findViewById(R.id.cust3)
        val tantangan4: TextView = itemView.findViewById(R.id.cust4)
        val editButton: Button = itemView.findViewById(R.id.edit_button)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.namaLatihan.text = data.kode
        holder.kesusahan.text = data.nama
        holder.tantangan2.text = data.nomor
        holder.tantangan3.text = data.tanggal
        holder.tantangan4.text = data.noTelp

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
