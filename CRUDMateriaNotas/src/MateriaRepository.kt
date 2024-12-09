import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MateriaRepository(private val filePath: String) {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    // Leer todas las materias desde el archivo
    fun findAll(): MutableList<Materia> {
        val materias = mutableListOf<Materia>()
        val file = File(filePath)
        if (!file.exists()) return materias

        file.forEachLine { line ->
            if (line.isNotEmpty()) {
                val parts = line.split(";")
                // Estructura esperada: id;nombre;codigo;fechaCreacion;activa;creditos;notasIds separados por ,
                val id = parts[0].toInt()
                val nombre = parts[1]
                val codigo = parts[2].toInt()
                val fechaCreacion = LocalDate.parse(parts[3], dateFormatter)
                val activa = parts[4].toBoolean()
                val creditos = parts[5].toDouble()
                val notasIds = if (parts.size > 6 && parts[6].isNotEmpty()) {
                    parts[6].split(",").map { it.toInt() }.toMutableList()
                } else {
                    mutableListOf()
                }

                val materia = Materia(id, nombre, codigo, fechaCreacion, activa, creditos, notasIds)
                materias.add(materia)
            }
        }
        return materias
    }

    // Guardar todas las materias en el archivo
    private fun saveAll(materias: List<Materia>) {
        val file = File(filePath)
        file.writeText("") // Limpia el archivo
        materias.forEach { materia ->
            val notasIdsStr = materia.notasIds.joinToString(",")
            val line = "${materia.id};${materia.nombre};${materia.codigo};${materia.fechaCreacion.format(dateFormatter)};${materia.activa};${materia.creditos};$notasIdsStr"
            file.appendText(line + "\n")
        }
    }

    fun create(materia: Materia) {
        val materias = findAll()
        materias.add(materia)
        saveAll(materias)
    }

    fun findById(id: Int): Materia? {
        return findAll().find { it.id == id }
    }

    fun update(materia: Materia) {
        val materias = findAll()
        val index = materias.indexOfFirst { it.id == materia.id }
        if (index != -1) {
            materias[index] = materia
            saveAll(materias)
        }
    }

    fun delete(id: Int) {
        val materias = findAll()
        val filtered = materias.filter { it.id != id }
        saveAll(filtered)
    }
}
