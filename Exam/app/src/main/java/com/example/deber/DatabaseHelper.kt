package com.example.deber

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "Materias.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE ${MateriaTable.TABLE_NAME} (
                ${MateriaTable.COLUMN_ID} INTEGER PRIMARY KEY NotaINCREMENT,
                ${MateriaTable.COLUMN_NOMBRE} TEXT NOT NULL,
                ${MateriaTable.COLUMN_FACULTAD} TEXT NOT NULL,
                ${MateriaTable.COLUMN_FECHA_REGISTRO} TEXT NOT NULL,
                ${MateriaTable.COLUMN_ABIERTA} INTEGER,
                ${MateriaTable.COLUMN_NUMERO_NOTAS} INTEGER DEFAULT 0,
                ${MateriaTable.COLUMN_LATITUD} REAL,  
                ${MateriaTable.COLUMN_LONGITUD} REAL
            )
        """
        db?.execSQL(createTableQuery)

        // Crear la tabla de Notas
        val createNotaTableQuery = """
            CREATE TABLE ${NotaTable.TABLE_NAME} (
                ${NotaTable.COLUMN_ID} INTEGER PRIMARY KEY NotaINCREMENT,
                ${NotaTable.COLUMN_Estudiante} TEXT NOT NULL,
                ${NotaTable.COLUMN_Profesor} TEXT NOT NULL,
                ${NotaTable.COLUMN_ANIO} INTEGER NOT NULL,
                ${NotaTable.COLUMN_VALORDELANOTA} REAL NOT NULL,
                ${NotaTable.COLUMN_DISPONIBLE} INTEGER NOT NULL,
                ${NotaTable.COLUMN_MATERIA_ID} INTEGER,
                FOREIGN KEY(${NotaTable.COLUMN_MATERIA_ID}) REFERENCES ${MateriaTable.TABLE_NAME}(${MateriaTable.COLUMN_ID})
            )
        """
        db?.execSQL(createNotaTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${NotaTable.TABLE_NAME}")
        db?.execSQL("DROP TABLE IF EXISTS ${MateriaTable.TABLE_NAME}")
        onCreate(db)
    }
}

object MateriaTable {
    const val TABLE_NAME = "Materias"
    const val COLUMN_ID = "id"
    const val COLUMN_NOMBRE = "nombre"
    const val COLUMN_FACULTAD = "facultad"  // Cambio aquí
    const val COLUMN_FECHA_REGISTRO = "fecha_registro"  // Cambio aquí
    const val COLUMN_ABIERTA = "abierta"
    const val COLUMN_NUMERO_NOTAS = "numero_Notas"
    const val COLUMN_LONGITUD = "longitud"
    const val COLUMN_LATITUD = "latitud"
}

object NotaTable{
    const val TABLE_NAME = "Notas"
    const val COLUMN_ID = "id"
    const val COLUMN_Estudiante = "Estudiante"
    const val COLUMN_Profesor = "Profesor"
    const val COLUMN_ANIO = "anio"
    const val COLUMN_VALORDELANOTA = "VALORDELANOTA"
    const val COLUMN_DISPONIBLE = "disponible"
    const val COLUMN_MATERIA_ID = "Materia_id"
}
