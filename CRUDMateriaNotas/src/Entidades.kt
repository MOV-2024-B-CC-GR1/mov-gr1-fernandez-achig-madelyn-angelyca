import java.time.LocalDate

data class Materia(
    val id: Int,
    var nombre: String,
    var codigo: Int,
    var fechaCreacion: LocalDate,
    var activa: Boolean,
    var creditos: Double,
    var notasIds: MutableList<Int> = mutableListOf() // Relación 1 a N con Notas
)

data class Nota(
    val id: Int,
    var nombre: String,
    var valor: Int,
    var fechaRegistro: LocalDate,
    var aprobado: Boolean,
    var porcentaje: Double
)
