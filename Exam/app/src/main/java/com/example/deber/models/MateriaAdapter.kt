package com.example.deber.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deber.R

class MateriaAdapter(private val Materias: List<Materia>) :
    RecyclerView.Adapter<MateriaAdapter.MateriaViewHolder>() {

    class MateriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombre)
        val facultadTextView: TextView = itemView.findViewById(R.id.textViewDireccion)
        val fechaRegistroTextView: TextView = itemView.findViewById(R.id.textViewFechaFundacion)
        val abiertaTextView: TextView = itemView.findViewById(R.id.textViewAbierta)
        val numeroNotasTextView: TextView = itemView.findViewById(R.id.textViewNumeroNotas)
        val latitudTextView: TextView = itemView.findViewById(R.id.textViewLatitud)
        val longitudTextView: TextView = itemView.findViewById(R.id.textViewLongitud)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_Materia, parent, false)
        return MateriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MateriaViewHolder, position: Int) {
        val Materia = Materias[position]
        holder.nombreTextView.text = Materia.nombre
        holder.facultadTextView.text = "Facultad: " + Materia.direccion
        holder.fechaRegistroTextView.text = "Fecha de registro: " + Materia.fechaFundacion
        holder.abiertaTextView.text = if (Materia.abierta) "Abierta" else "Cerrada"
        holder.numeroNotasTextView.text = "Número de calificaciones registradas: ${Materia.numeroNotas}"
        holder.latitudTextView.text = "Latitud de la Facultad: ${Materia.latitud}"
        holder.longitudTextView.text = "Longitud de la Facultad: ${Materia.longitud}"

    }

    override fun getItemCount(): Int {
        return Materias.size
    }
}