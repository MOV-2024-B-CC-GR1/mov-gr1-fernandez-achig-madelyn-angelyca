package com.example.deber


import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnVerMaterias: Button = findViewById(R.id.btnVerMaterias)
        val btnVerNotas: Button = findViewById(R.id.btnVerNotas)
        val btnCrearMateria: Button = findViewById(R.id.btnCrearMateria)
        val btnCrearNota: Button = findViewById(R.id.btnCrearNota)
        val btnVerMapa: Button = findViewById(R.id.btnVerMapa)

        val dbHelper = DBHelper(this)
        val db = dbHelper.writableDatabase

        btnVerMaterias.setOnClickListener {
            val intent = Intent(this, VerMateriasActivity::class.java)
            startActivity(intent)
        }

        btnVerNotas.setOnClickListener {
            val intent = Intent(this, VerNotasActivity::class.java)
            startActivity(intent)
        }

        btnCrearMateria.setOnClickListener {
            val intent = Intent(this, CrearMateriaActivity::class.java)
            startActivity(intent)
        }

        btnCrearNota.setOnClickListener {
            val intent = Intent(this, CrearNotaActivity::class.java)
            startActivity(intent)
        }
        btnVerMapa.setOnClickListener {
            val intent = Intent(this, VerMapaActivity::class.java)

            startActivity(intent)
        }
        // Inicializar el proveedor de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

    }
}
