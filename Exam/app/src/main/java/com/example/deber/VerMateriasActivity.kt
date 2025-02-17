package com.example.deber

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deber.models.Materia
import com.example.deber.models.MateriaAdapter

class VerMateriasActivity : AppCompatActivity() {

    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_Materias)

        val dbHelper = DBHelper(this)
        db = dbHelper.readableDatabase

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewMaterias)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val Materias = obtenerMaterias(db)
        val adapter = MateriaAdapter(Materias)
        recyclerView.adapter = adapter
    }

    private fun obtenerMaterias(db: SQLiteDatabase): List<Materia> {
        val Materias = mutableListOf<Materia>()
        val cursor = db.query(
            MateriaTable.TABLE_NAME,
            arrayOf(
                MateriaTable.COLUMN_ID,
                MateriaTable.COLUMN_NOMBRE,
                MateriaTable.COLUMN_DIRECCION,
                MateriaTable.COLUMN_FECHA_FUNDACION,
                MateriaTable.COLUMN_ABIERTA,
                MateriaTable.COLUMN_NUMERO_NotaS,
                MateriaTable.COLUMN_LATITUD,
                MateriaTable.COLUMN_LONGITUD
            ),
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
                val direccion = cursor.getString(cursor.getColumnIndex(MateriaTable.COLUMN_DIRECCION))
                val fechaFundacion = cursor.getString(cursor.getColumnIndex(MateriaTable.COLUMN_FECHA_FUNDACION))
                val abierta = cursor.getInt(cursor.getColumnIndex(MateriaTable.COLUMN_ABIERTA)) == 1
                val numeroNotas = cursor.getInt(cursor.getColumnIndex(MateriaTable.COLUMN_NUMERO_NotaS))
                val latitud = cursor.getDouble(cursor.getColumnIndex(MateriaTable.COLUMN_LATITUD))
                val longitud = cursor.getDouble(cursor.getColumnIndex(MateriaTable.COLUMN_LONGITUD))

                val Materia = Materia(
                    id = id,
                    nombre = nombre,
                    direccion = direccion,
                    numeroNotas = numeroNotas,
                    fechaFundacion = fechaFundacion,
                    abierta = abierta,
                    latitud = latitud,
                    longitud = longitud
                )

                Materias.add(Materia)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return Materias
    }
}