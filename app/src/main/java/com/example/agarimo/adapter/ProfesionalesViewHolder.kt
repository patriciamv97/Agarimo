package com.example.agarimo.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agarimo.ProfesionalesBd
import com.example.agarimo.R

class ProfesionalesViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    val nombre = view.findViewById<TextView>(R.id.tvnombre)
    val lt = view.findViewById<TextView>(R.id.tvlt)
    val lg = view.findViewById<TextView>(R.id.tvlg)

    fun render(profesionalModel: ProfesionalesBd) {
        nombre.text="Nombre Profesional :"+profesionalModel.nombre
        lt.text= "Latitud: "+profesionalModel.lt.toString()
        lg.text= "Longitud: "+profesionalModel.lg.toString()
        //Glide.with(photo.context)


    }

}