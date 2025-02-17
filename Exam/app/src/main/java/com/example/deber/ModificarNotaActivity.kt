package com.example.deber

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deber.models.Nota

class ModificarNotaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar_Nota)

        val EstudianteEditText: EditText = findViewById(R.id.editTextEstudiante)
        val ProfesorEditText: EditText = findViewById(R.id.editTextProfesor)
        val anioEditText: EditText = findViewById(R.id.editTextAnio)
        val ValorDeLaNotaEditText: EditText = findViewById(R.id.editTextValorDeLaNota)
        val disponibleCheckBox: CheckBox = findViewById(R.id.checkBoxDisponible)
        val btnGuardar: Button = findViewById(R.id.btnGuardar)

        val NotaId = intent.getLongExtra("Nota_id", -1)
        val Estudiante = intent.getStringExtra("Estudiante") ?: ""
        val Profesor = intent.getStringExtra("Profesor") ?: ""
        val anio = intent.getIntExtra("anio", 0)
        val ValorDeLaNota = intent.getDoubleExtra("ValorDeLaNota", 0.0)
        val disponible = intent.getBooleanExtra("disponible", false)

        EstudianteEditText.setText(Estudiante)
        ProfesorEditText.setText(Profesor)
        anioEditText.setText(anio.toString())
        ValorDeLaNotaEditText.setText(ValorDeLaNota.toString())
        disponibleCheckBox.isChecked = disponible

        btnGuardar.setOnClickListener {
            // Lógica para guardar los cambios
            Toast.makeText(this, "Nota modificado", Toast.LENGTH_SHORT).show()
            finish() // Cerrar la actividad
        }
    }
}