package com.example.deber.models

data class Materia(
    val id: Long = 0,
    val nombre: String,
    val factultas: String,
    var numeroNotas: Int,
    val fechaRegistro: String,
    val abierta: Boolean,
    val latitud: Double,
    val longitud: Double, 
    val Notas: MutableList<Nota> = mutableListOf()

){
    override fun toString(): String {
        return nombre
    }
}