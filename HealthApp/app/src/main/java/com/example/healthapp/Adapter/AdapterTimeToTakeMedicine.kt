package com.example.healthapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.R
import com.example.healthapp.model.TimeToTakeMedicine

class AdapterTimeToTakeMedicine(
    private val list: ArrayList<TimeToTakeMedicine>,
    val context: Context,
    private val listener: ClickGetTime
) :
    RecyclerView.Adapter<AdapterTimeToTakeMedicine.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val nameMedicine: TextView = itemView.findViewById(R.id.tvNamMedicine1)
        val time: TextView = itemView.findViewById(R.id.tvAlarmTime)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            with(listener){
                onItemClicked(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.form_list_of_time_to_take_medicine, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: TimeToTakeMedicine = list[position]
        holder.nameMedicine.text = model.name_diseases
        holder.time.text = model.time
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ClickGetTime {
        fun onItemClicked(position: Int)
    }
}