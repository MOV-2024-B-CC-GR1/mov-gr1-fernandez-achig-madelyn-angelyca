import java.time.LocalDate
import java.util.Scanner

fun main() {
    val materiaRepository = MateriaRepository("C:\\Users\\madelyn.fernandez\\IdeaProjects\\CRUDMateriaNotas\\src\\materias.csv")
    val notaRepository = NotaRepository("C:\\Users\\madelyn.fernandez\\IdeaProjects\\CRUDMateriaNotas\\src\\notas.csv")

    val scanner = Scanner(System.`in`)
    var opcion: Int

    do {
        println("\n----- MENÚ PRINCIPAL -----")
        println("1. CRUD Materias")
        println("2. CRUD Notas de una Materia")
        println("0. Salir")
        print("Seleccione una opción: ")
        opcion = scanner.nextInt()

        when(opcion) {
            1 -> crudMaterias(materiaRepository, notaRepository, scanner)
            2 -> {
                print("Ingrese el ID de la Materia: ")
                val idMateria = scanner.nextInt()
                val materia = materiaRepository.findById(idMateria)
                if (materia != null) {
                    crudNotas(materiaRepository, notaRepository, scanner, materia)
                } else {
                    println("La materia con ID $idMateria no existe.")
                }
            }
            0 -> println("Saliendo...")
            else -> println("Opción no válida.")
        }
    } while(opcion != 0)
}

fun crudMaterias(materiaRepository: MateriaRepository, notaRepository: NotaRepository, scanner: Scanner) {
    var opcion: Int
    do {
        println("\n----- CRUD MATERIAS -----")
        println("1. Crear Materia")
        println("2. Leer Materias")
        println("3. Actualizar Materia")
        println("4. Eliminar Materia")
        println("0. Regresar")
        print("Seleccione una opción: ")
        opcion = scanner.nextInt()

        when(opcion) {
            1 -> {
                // Crear materia
                print("Ingrese ID: ")
                val id = scanner.nextInt()
                print("Ingrese Nombre: ")
                val nombre = scanner.next()
                print("Ingrese Código: ")
                val codigo = scanner.nextInt()
                val fechaCreacion = LocalDate.now()
                print("¿Está activa? (true/false): ")
                val activa = scanner.nextBoolean()
                print("Ingrese Créditos (decimal): ")
                val creditos = scanner.nextDouble()

                val materia = Materia(
                    id = id,
                    nombre = nombre,
                    codigo = codigo,
                    fechaCreacion = fechaCreacion,
                    activa = activa,
                    creditos = creditos
                )
                materiaRepository.create(materia)
                println("Materia creada con éxito.")
            }
            2 -> {
                // Leer materias
                val materias = materiaRepository.findAll()
                if (materias.isEmpty()) {
                    println("No hay materias registradas.")
                } else {
                    println("Listado de Materias:")
                    materias.forEach {
                        println("ID: ${it.id}, Nombre: ${it.nombre}, Código: ${it.codigo}, Activa: ${it.activa}, Créditos: ${it.creditos}, Notas: ${it.notasIds}")
                    }
                }
            }
            3 -> {
                // Actualizar materia
                print("Ingrese el ID de la materia a actualizar: ")
                val id = scanner.nextInt()
                val materia = materiaRepository.findById(id)
                if (materia != null) {
                    print("Nuevo nombre (${materia.nombre}): ")
                    val nombre = scanner.next()
                    print("Nuevo código (${materia.codigo}): ")
                    val codigo = scanner.nextInt()
                    print("¿Activa? (${materia.activa}): ")
                    val activa = scanner.nextBoolean()
                    print("Nuevos créditos (${materia.creditos}): ")
                    val creditos = scanner.nextDouble()

                    materia.nombre = nombre
                    materia.codigo = codigo
                    materia.activa = activa
                    materia.creditos = creditos

                    materiaRepository.update(materia)
                    println("Materia actualizada con éxito.")
                } else {
                    println("La materia con ID $id no existe.")
                }
            }
            4 -> {
                // Eliminar materia
                print("Ingrese el ID de la materia a eliminar: ")
                val id = scanner.nextInt()
                val materia = materiaRepository.findById(id)
                if (materia != null) {
                    // Al eliminar materia, también sería prudente eliminar sus notas asociadas:
                    materia.notasIds.forEach { notaId ->
                        notaRepository.delete(notaId)
                    }
                    materiaRepository.delete(id)
                    println("Materia eliminada con éxito.")
                } else {
                    println("La materia con ID $id no existe.")
                }
            }
            0 -> println("Regresando al menú principal...")
            else -> println("Opción no válida.")
        }
    } while(opcion != 0)
}

fun crudNotas(
    materiaRepository: MateriaRepository,
    notaRepository: NotaRepository,
    scanner: Scanner,
    materia: Materia
) {
    var opcion: Int
    do {
        println("\n----- CRUD NOTAS DE LA MATERIA: ${materia.nombre} -----")
        println("1. Crear Nota")
        println("2. Leer Notas")
        println("3. Actualizar Nota")
        println("4. Eliminar Nota")
        println("0. Regresar")
        print("Seleccione una opción: ")
        opcion = scanner.nextInt()

        when(opcion) {
            1 -> {
                // Crear Nota
                print("Ingrese ID de la Nota: ")
                val id = scanner.nextInt()
                print("Ingrese Nombre de la Nota: ")
                val nombre = scanner.next()
                print("Ingrese Valor (entero): ")
                val valor = scanner.nextInt()
                val fechaRegistro = LocalDate.now()
                print("¿Aprobada? (true/false): ")
                val aprobado = scanner.nextBoolean()
                print("Ingrese Porcentaje (decimal): ")
                val porcentaje = scanner.nextDouble()

                val nota = Nota(
                    id = id,
                    nombre = nombre,
                    valor = valor,
                    fechaRegistro = fechaRegistro,
                    aprobado = aprobado,
                    porcentaje = porcentaje
                )
                notaRepository.create(nota)
                materia.notasIds.add(id)
                materiaRepository.update(materia)
                println("Nota creada y asociada a la materia.")
            }
            2 -> {
                // Leer Notas de la materia
                val notas = notaRepository.findAll().filter { materia.notasIds.contains(it.id) }
                if (notas.isEmpty()) {
                    println("No hay notas para esta materia.")
                } else {
                    println("Listado de Notas:")
                    notas.forEach {
                        println("ID: ${it.id}, Nombre: ${it.nombre}, Valor: ${it.valor}, Aprobado: ${it.aprobado}, Porcentaje: ${it.porcentaje}")
                    }
                }
            }
            3 -> {
                // Actualizar Nota
                print("Ingrese el ID de la Nota a actualizar: ")
                val id = scanner.nextInt()
                val nota = notaRepository.findById(id)
                if (nota != null && materia.notasIds.contains(id)) {
                    print("Nuevo nombre (${nota.nombre}): ")
                    val nombre = scanner.next()
                    print("Nuevo valor (${nota.valor}): ")
                    val valor = scanner.nextInt()
                    print("¿Aprobada? (${nota.aprobado}): ")
                    val aprobado = scanner.nextBoolean()
                    print("Nuevo porcentaje (${nota.porcentaje}): ")
                    val porcentaje = scanner.nextDouble()

                    nota.nombre = nombre
                    nota.valor = valor
                    nota.aprobado = aprobado
                    nota.porcentaje = porcentaje

                    notaRepository.update(nota)
                    println("Nota actualizada con éxito.")
                } else {
                    println("La nota con ID $id no existe o no está asociada a esta materia.")
                }
            }
            4 -> {
                // Eliminar Nota
                print("Ingrese el ID de la Nota a eliminar: ")
                val id = scanner.nextInt()
                val nota = notaRepository.findById(id)
                if (nota != null && materia.notasIds.contains(id)) {
                    notaRepository.delete(id)
                    materia.notasIds.remove(id)
                    materiaRepository.update(materia)
                    println("Nota eliminada con éxito.")
                } else {
                    println("La nota con ID $id no existe o no está asociada a esta materia.")
                }
            }
            0 -> println("Regresando...")
            else -> println("Opción no válida.")
        }
    } while(opcion != 0)
}
