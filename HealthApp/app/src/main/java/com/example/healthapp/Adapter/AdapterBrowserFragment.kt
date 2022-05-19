package com.example.healthapp.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.R
import com.example.healthapp.model.RecordMedical


class AdapterBrowserFragment(
    private val list: List<RecordMedical>,
    val context: Context,
    private val listener: OnItemClickListener) :
    RecyclerView.Adapter<AdapterBrowserFragment.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val date: TextView = itemView.findViewById(R.id.labelDate)
        val time: TextView = itemView.findViewById(R.id.labelTime)


        init {
            itemView.setOnClickListener (this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            with(listener) {
                onItemClicked(position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.form_list_of_medical_record, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: RecordMedical = list[position]
        holder.date.text = model.date
        holder.time.text = model.time
    }


    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
