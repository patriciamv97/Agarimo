package com.example.agarimo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agarimo.ProfesionalesBd
import com.example.agarimo.R



class ProfesionalAdapter(val listaProfesionales: ArrayList<ProfesionalesBd>) :
    RecyclerView.Adapter<ProfesionalesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfesionalesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return  ProfesionalesViewHolder(layoutInflater.inflate(R.layout.item_profeisonal, parent,false))
    }

    override fun onBindViewHolder(holder: ProfesionalesViewHolder, position: Int) {
        val item = listaProfesionales[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = listaProfesionales.size


}