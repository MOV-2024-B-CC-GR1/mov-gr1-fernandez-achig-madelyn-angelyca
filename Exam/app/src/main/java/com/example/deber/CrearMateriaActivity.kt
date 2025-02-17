package com.example.deber

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class CrearMateriaActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_Materia)

        val nombreEditText: EditText = findViewById(R.id.editTextNombre)
        val facultadEditText: EditText = findViewById(R.id.editTextfacultad)
        val fechaRegistroEditText: EditText = findViewById(R.id.editTextfechaRegistro)
        val abiertaCheckBox: CheckBox = findViewById(R.id.checkBoxAbierta)
        val btnCrearMateria: Button = findViewById(R.id.btnCrearMateria)

        // Inicializar el proveedor de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        
        UbicacionActual()

        val dbHelper = DBHelper(this)
        val db = dbHelper.writableDatabase

        btnCrearMateria.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val facultad = facultadEditText.text.toString()
            val fechaRegistro = fechaRegistroEditText.text.toString()
            val abierta = abiertaCheckBox.isChecked

            if (nombre.isNotEmpty() && facultad.isNotEmpty() && fechaRegistro.isNotEmpty()) {
                val contentValues = ContentValues().apply {
                    put(MateriaTable.COLUMN_NOMBRE, nombre)
                    put(MateriaTable.COLUMN_FACULTAD, facultad)
                    put(MateriaTable.COLUMN_FECHA_REGISTRO, fechaRegistro)
                    put(MateriaTable.COLUMN_ABIERTA, if (abierta) 1 else 0)
                    
                    put(MateriaTable.COLUMN_LATITUD, latitud)
                    put(MateriaTable.COLUMN_LONGITUD, longitud)
                }

                val rowId = db.insert(MateriaTable.TABLE_NAME, null, contentValues)
                if (rowId != -1L) {
                    Toast.makeText(this, "Materia creada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al crear Materia", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun UbicacionActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        val locationRequest = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY

        fusedLocationClient.getCurrentLocation(locationRequest, null).addOnSuccessListener { location: Location? ->
            if (location != null) {
                latitud = location.latitude
                longitud = location.longitude
                Toast.makeText(this, "Ubicación obtenida: $latitud, $longitud", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación en tiempo real", Toast.LENGTH_SHORT).show()
            }
        }
    }

}