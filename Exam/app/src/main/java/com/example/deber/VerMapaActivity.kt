package com.example.deber

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.deber.models.Materia
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class VerMapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var db: SQLiteDatabase
    private lateinit var MateriaSpinner: Spinner
    private lateinit var Materias: List<Materia>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_mapa)

        val dbHelper = DBHelper(this)
        db = dbHelper.readableDatabase

        MateriaSpinner = findViewById(R.id.spinnerMateria)

        // Obtener la lista de Materias
        Materias = obtenerMaterias(db)

        // Crear un adaptador para el Spinner con los nombres de las Materias
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Materias.map { it.nombre }
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        MateriaSpinner.adapter = spinnerAdapter

        MateriaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val MateriaSeleccionada = Materias[position]
                moverMapa(MateriaSeleccionada.latitud, MateriaSeleccionada.longitud, MateriaSeleccionada.nombre)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Obtener el fragmento del mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Mostrar la primera Materia al abrir el mapa
        if (Materias.isNotEmpty()) {
            val primera = Materias[0]
            moverMapa(primera.latitud, primera.longitud, primera.nombre)
        }
    }

    private fun moverMapa(latitud: Double, longitud: Double, titulo: String) {
        val ubicacion = LatLng(latitud, longitud)
        mMap.clear()  // Limpia Estudiantedores anteriores
        mMap.addMarker(MarkerOptions().position(ubicacion).title(titulo))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f))
    }

    private fun obtenerMaterias(db: SQLiteDatabase): List<Materia> {
        val Materias = mutableListOf<Materia>()
        val cursor = db.query(
            MateriaTable.TABLE_NAME,
            arrayOf(
                MateriaTable.COLUMN_ID,
                MateriaTable.COLUMN_NOMBRE,
                MateriaTable.COLUMN_LATITUD,
                MateriaTable.COLUMN_LONGITUD
            ),
            null, null, null, null, null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MateriaTable.COLUMN_ID))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(MateriaTable.COLUMN_NOMBRE))
                val latitud = cursor.getDouble(cursor.getColumnIndexOrThrow(MateriaTable.COLUMN_LATITUD))
                val longitud = cursor.getDouble(cursor.getColumnIndexOrThrow(MateriaTable.COLUMN_LONGITUD))

                Materias.add(Materia(id, nombre, "", 0, "", false, latitud, longitud))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return Materias
    }
}
