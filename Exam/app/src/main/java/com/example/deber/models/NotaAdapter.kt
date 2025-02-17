package com.example.deber

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deber.models.Nota

class NotaAdapter(
    private val Notas: List<Nota>,
    private val onModificarClickListener: (Nota) -> Unit,
    private val onEliminarClickListener: (Nota) -> Unit
) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val EstudianteTextView: TextView = itemView.findViewById(R.id.textViewEstudiante)
        val ProfesorTextView: TextView = itemView.findViewById(R.id.textViewProfesor)
        val anioTextView: TextView = itemView.findViewById(R.id.textViewAnio)
        val ValorDeLaNotaTextView: TextView = itemView.findViewById(R.id.textViewValorDeLaNota)
        val disponibleTextView: TextView = itemView.findViewById(R.id.textViewDisponible)
        val btnModificar: Button = itemView.findViewById(R.id.btnModificar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_Nota, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val Nota = Notas[position]
        holder.EstudianteTextView.text = "Estudiante: ${Nota.Estudiante}"
        holder.ProfesorTextView.text = "Profesor: ${Nota.Profesor}"
        holder.anioTextView.text = "Periodo: ${Nota.anio}"
        holder.ValorDeLaNotaTextView.text = "Valor de la nota: $${Nota.ValorDeLaNota}"
        holder.disponibleTextView.text = if (Nota.disponible) "Activa" else "Eliminada"

        // Configurar el listener para el botón "Modificar"
        holder.btnModificar.setOnClickListener {
            onModificarClickListener(Nota)
        }

        // Configurar el listener para el botón "Eliminar"
        holder.btnEliminar.setOnClickListener {
            onEliminarClickListener(Nota)
        }
    }

    override fun getItemCount(): Int {
        return Notas.size
    }
}