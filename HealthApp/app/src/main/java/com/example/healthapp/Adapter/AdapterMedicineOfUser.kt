package com.example.healthapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.R
import com.example.healthapp.model.Medicine

class AdapterMedicineOfUser(
    private val list: List<Medicine>,
    private val context: Context,
    private val listener: AdapterMedicineOfUser.OnItemClickListener3
) :    RecyclerView.Adapter<AdapterMedicineOfUser.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val nameMedicine: TextView = itemView.findViewById(R.id.tvNameDiseasesAndMedicine)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            with(listener) {
                onItemClicked3(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.form_record_of_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: Medicine = list[position]
        holder.nameMedicine.text = model.name_medicine
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener3 {
        fun onItemClicked3(position: Int)
    }
}