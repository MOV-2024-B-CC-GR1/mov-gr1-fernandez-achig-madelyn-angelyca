package com.example.deber

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deber.models.Nota
import com.example.deber.models.Materia

class VerNotasActivity : AppCompatActivity() {

    private lateinit var db: SQLiteDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var NotaAdapter: NotaAdapter
    private lateinit var MateriaSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_Notas)

        MateriaSpinner = findViewById(R.id.spinnerMateria)
        recyclerView = findViewById(R.id.recyclerViewNotas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DBHelper(this)
        db = dbHelper.readableDatabase

        // Obtener la lista de Materias
        val Materias = obtenerMaterias(db)

        // Crear un adaptador para el Spinner con las Materias
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Materias)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        MateriaSpinner.adapter = spinnerAdapter

        // Configurar el listener del Spinner
        MateriaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val MateriaSeleccionada = Materias[position]
                val Notas = obtenerNotasPorMateria(db, MateriaSeleccionada.id)

                // Configurar el adaptador del RecyclerView con los listeners
                NotaAdapter = NotaAdapter(
                    Notas,
                    onModificarClickListener = { Nota -> modificarNota(Nota) },
                    onEliminarClickListener = { Nota -> eliminarNota(Nota) }
                )
                recyclerView.adapter = NotaAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada
            }
        }
    }

    private fun modificarNota(Nota: Nota) {
        // Lógica para modificar el Nota
        Toast.makeText(this, "Modificar Nota: ${Nota.Estudiante} ${Nota.Profesor}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ModificarNotaActivity::class.java).apply {
            putExtra("Nota_id", Nota.id)
            putExtra("Estudiante", Nota.Estudiante)
            putExtra("Profesor", Nota.Profesor)
            putExtra("anio", Nota.anio)
            putExtra("ValorDeLaNota", Nota.ValorDeLaNota)
            putExtra("disponible", Nota.disponible)
        }
        startActivity(intent)
    }

    private fun eliminarNota(Nota: Nota) {
        // Lógica para eliminar el Nota
        val dbHelper = DBHelper(this)
        val db = dbHelper.writableDatabase

        val rowsDeleted = db.delete(
            NotaTable.TABLE_NAME,
            "${NotaTable.COLUMN_ID} = ?",
            arrayOf(Nota.id.toString())
        )

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Nota eliminado", Toast.LENGTH_SHORT).show()
            // Actualizar la lista de Notas
            val MateriaSeleccionada = (MateriaSpinner.selectedItem as Materia)
            val Notas = obtenerNotasPorMateria(db, MateriaSeleccionada.id)
            NotaAdapter = NotaAdapter(
                Notas,
                onModificarClickListener = { Nota -> modificarNota(Nota) },
                onEliminarClickListener = { Nota -> eliminarNota(Nota) }
            )
            recyclerView.adapter = NotaAdapter
        } else {
            Toast.makeText(this, "Error al eliminar el Nota", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerMaterias(db: SQLiteDatabase): List<Materia> {
        val Materias = mutableListOf<Materia>()
        val cursor = db.query(
            MateriaTable.TABLE_NAME,
            arrayOf(MateriaTable.COLUMN_ID, MateriaTable.COLUMN_NOMBRE),
            null,
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(MateriaTable.COLUMN_ID))
                val nombre = cursor.getString(cursor.getColumnIndex(MateriaTable.COLUMN_NOMBRE))

                val Materia = Materia(
                    id = id,
                    nombre = nombre,
                    direccion = "",
                    numeroNotas = 0,
                    fechaFundacion = "",
                    abierta = false,
                    latitud = 0.0,
                    longitud = 0.0
                )

                Materias.add(Materia)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return Materias
    }

    private fun obtenerNotasPorMateria(db: SQLiteDatabase, MateriaId: Long): List<Nota> {
        val Notas = mutableListOf<Nota>()
        val cursor = db.query(
            NotaTable.TABLE_NAME,
            arrayOf(
                NotaTable.COLUMN_ID,
                NotaTable.COLUMN_Estudiante,
                NotaTable.COLUMN_Profesor,
                NotaTable.COLUMN_ANIO,
                NotaTable.COLUMN_ValorDeLaNota,
                NotaTable.COLUMN_DISPONIBLE
            ),
            "${NotaTable.COLUMN_Materia_ID} = ?",
            arrayOf(MateriaId.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(NotaTable.COLUMN_ID))
                val Estudiante = cursor.getString(cursor.getColumnIndex(NotaTable.COLUMN_Estudiante))
                val Profesor = cursor.getString(cursor.getColumnIndex(NotaTable.COLUMN_Profesor))
                val anio = cursor.getInt(cursor.getColumnIndex(NotaTable.COLUMN_ANIO))
                val ValorDeLaNota = cursor.getDouble(cursor.getColumnIndex(NotaTable.COLUMN_ValorDeLaNota))
                val disponible = cursor.getInt(cursor.getColumnIndex(NotaTable.COLUMN_DISPONIBLE)) == 1

                val Nota = Nota(
                    id = id,
                    Estudiante = Estudiante,
                    Profesor = Profesor,
                    anio = anio,
                    ValorDeLaNota = ValorDeLaNota,
                    disponible = disponible,
                    MateriaId = MateriaId
                )

                Notas.add(Nota)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return Notas
    }
}