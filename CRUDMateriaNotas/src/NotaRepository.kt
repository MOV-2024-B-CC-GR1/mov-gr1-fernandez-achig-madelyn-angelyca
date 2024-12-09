import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NotaRepository(private val filePath: String) {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    // Leer todas las notas
    fun findAll(): MutableList<Nota> {
        val notas = mutableListOf<Nota>()
        val file = File(filePath)
        if (!file.exists()) return notas
        file.forEachLine { line ->
            if (line.isNotEmpty()) {
                val parts = line.split(";")
                // id;nombre;valor;fechaRegistro;aprobado;porcentaje
                val id = parts[0].toInt()
                val nombre = parts[1]
                val valor = parts[2].toInt()
                val fechaRegistro = LocalDate.parse(parts[3], dateFormatter)
                val aprobado = parts[4].toBoolean()
                val porcentaje = parts[5].toDouble()
                val nota = Nota(id, nombre, valor, fechaRegistro, aprobado, porcentaje)
                notas.add(nota)
            }
        }
        return notas
    }

    private fun saveAll(notas: List<Nota>) {
        val file = File(filePath)
        file.writeText("")
        notas.forEach { nota ->
            val line = "${nota.id};${nota.nombre};${nota.valor};${nota.fechaRegistro.format(dateFormatter)};${nota.aprobado};${nota.porcentaje}"
            file.appendText(line + "\n")
        }
    }

    fun create(nota: Nota) {
        val notas = findAll()
        notas.add(nota)
        saveAll(notas)
    }

    fun findById(id: Int): Nota? {
        return findAll().find { it.id == id }
    }

    fun update(nota: Nota) {
        val notas = findAll()
        val index = notas.indexOfFirst { it.id == nota.id }
        if (index != -1) {
            notas[index] = nota
            saveAll(notas)
        }
    }

    fun delete(id: Int) {
        val notas = findAll()
        val filtered = notas.filter { it.id != id }
        saveAll(filtered)
    }
}
