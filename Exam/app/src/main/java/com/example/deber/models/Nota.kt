package com.example.deber.models

data class Nota(
    val id: Long,
    val Estudiante: String,
    val Profesor: String,
    val anio: Int,
    val ValorDeLaNota: Double,
    val disponible: Boolean,
    val MateriaId: Long
)