package com.example.healthapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.R
import com.example.healthapp.model.DetailMedicine

class AdapterDetailMedicine(
    private val list: List<DetailMedicine>,
    private val context: Context,
    private val listener: AdapterDetailMedicine.OnItemClickListener4
) : RecyclerView.Adapter<AdapterDetailMedicine.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val nameMedicine: TextView = itemView.findViewById(R.id.tvNameMedicine)
        val numberMedicine: TextView = itemView.findViewById(R.id.tvNumberMedicine)
        val timeMedicine: TextView = itemView.findViewById(R.id.tvTimeMedicine)
        val noticeMedicine: TextView = itemView.findViewById(R.id.tvNoticeMedicine)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            with(listener) {
                onItemClicked4(position)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.form_list_of_detail_medicine, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: DetailMedicine = list[position]
        holder.nameMedicine.text = model.name_medicine
        holder.numberMedicine.text = model.number_medicine
        holder.timeMedicine.text = model.time_medicine
        holder.noticeMedicine.text = model.notice_medicine

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener4 {
        fun onItemClicked4(position: Int)
    }
}