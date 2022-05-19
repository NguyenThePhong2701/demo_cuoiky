package com.example.healthapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.R
import com.example.healthapp.model.Diseases

class AdapterDiseases(
    private val list: List<Diseases>,
    val context: Context,
    private val listener: AdapterDiseases.OnItemClickListener1
) : RecyclerView.Adapter<AdapterDiseases.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val nameDiseases: TextView = itemView.findViewById(R.id.tvNameDiseasesAndMedicine)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            with(listener) {
                onItemClicked1(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.form_record_of_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: Diseases = list[position]
        holder.nameDiseases.text = model.name_diseases
    }

    override fun getItemCount(): Int {
        return list.size
    }


    interface OnItemClickListener1 {
        fun onItemClicked1(position: Int)
    }
}