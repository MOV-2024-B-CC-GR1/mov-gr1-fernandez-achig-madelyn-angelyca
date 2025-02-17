package com.example.deber

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deber.models.Materia

class CrearNotaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_Nota)

        val EstudianteEditText: EditText = findViewById(R.id.editTextEstudiante)
        val ProfesorEditText: EditText = findViewById(R.id.editTextProfesor)
        val anioEditText: EditText = findViewById(R.id.editTextAnio)
        val ValorDeLaNotaEditText: EditText = findViewById(R.id.editTextValorDeLaNota)
        val disponibleCheckBox: CheckBox = findViewById(R.id.checkBoxDisponible)
        val MateriaSpinner: Spinner = findViewById(R.id.spinnerMaterias)
        val btnCrearNota: Button = findViewById(R.id.btnCrearNota)

        val dbHelper = DBHelper(this)
        val db = dbHelper.readableDatabase

        // Obtener la lista de Materias
        val Materias = obtenerMaterias(db)

        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Materias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        MateriaSpinner.adapter = adapter

        btnCrearNota.setOnClickListener {
            val Estudiante = EstudianteEditText.text.toString()
            val Profesor = ProfesorEditText.text.toString()
            val anioStr = anioEditText.text.toString()
            val ValorDeLaNotaStr = ValorDeLaNotaEditText.text.toString()
            val MateriaSeleccionada = Materias[MateriaSpinner.selectedItemPosition]
            val disponible = disponibleCheckBox.isChecked

            if (Estudiante.isNotEmpty() && Profesor.isNotEmpty() && anioStr.isNotEmpty() && ValorDeLaNotaStr.isNotEmpty()) {
                val anio = anioStr.toIntOrNull()  // Convertir a Int de forma segura
                val ValorDeLaNota = ValorDeLaNotaStr.toDoubleOrNull()  // Convertir a Double de forma segura

                if (anio != null && ValorDeLaNota != null) {
                    val contentValues = ContentValues().apply {
                        put(NotaTable.COLUMN_Estudiante, Estudiante)
                        put(NotaTable.COLUMN_Profesor, Profesor)
                        put(NotaTable.COLUMN_ANIO, anio)
                        put(NotaTable.COLUMN_ValorDeLaNota, ValorDeLaNota)
                        put(NotaTable.COLUMN_DISPONIBLE, if (disponible) 1 else 0)
                        put(NotaTable.COLUMN_Materia_ID, MateriaSeleccionada.id)
                    }

                    val rowId = db.insert(NotaTable.TABLE_NAME, null, contentValues)
                    if (rowId != -1L) {
                        actualizarNumeroNotas(MateriaSeleccionada.id, db)
                        Toast.makeText(this, "Nota creado exitosamente", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error al crear el Nota", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Por favor ingresa valores válidos para el Periodo y Valor De La Nota", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para obtener la lista de Materias
    private fun obtenerMaterias(db: SQLiteDatabase): List<Materia> {
        val Materias = mutableListOf<Materia>()
        val cursor = db.query(
            MateriaTable.TABLE_NAME,
            arrayOf(MateriaTable.COLUMN_ID, MateriaTable.COLUMN_NOMBRE), // Selecciona solo los campos necesarios
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
    private fun actualizarNumeroNotas(MateriaId: Long, db: SQLiteDatabase) {
        val sqlUpdate = """
        UPDATE ${MateriaTable.TABLE_NAME}
        SET ${MateriaTable.COLUMN_NUMERO_NotaS} = 
            (SELECT COUNT(*) FROM ${NotaTable.TABLE_NAME} WHERE ${NotaTable.COLUMN_Materia_ID} = ?)
        WHERE ${MateriaTable.COLUMN_ID} = ?
    """
        val stmt = db.compileStatement(sqlUpdate)
        stmt.bindLong(1, MateriaId)
        stmt.bindLong(2, MateriaId)
        stmt.executeUpdateDelete()
    }


}