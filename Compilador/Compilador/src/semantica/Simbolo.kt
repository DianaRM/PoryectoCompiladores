package semantica

import sintaxis.Expresion
import java.util.*

/**
 * Representa la información de interés de los símbolos del programa, un símbolo puede ser
 * una varible o una función.
 * @author Esthefania Lemus - Diana Ramirez - Cristian Bonilla
 */
class Simbolo {
    var nombre: String?
    var tipo: String
    var fila = 0
    var columna = 0
    var ambito: Simbolo? = null
    private var expresion: Expresion? = null
    var tipoParametros: ArrayList<String?>? = null

    constructor(nombre: String?, tipo: String, fila: Int, columna: Int, ambito: Simbolo?, expresion: Expresion?) {
        this.nombre = nombre
        this.tipo = tipo
        this.fila = fila
        this.columna = columna
        this.ambito = ambito
        this.expresion = expresion
    }

    constructor(nombre: String?, tipo: String, tipoParametros: ArrayList<String?>) : super() {
        this.nombre = nombre
        this.tipo = tipo
        this.tipoParametros = tipoParametros
    }

    fun getExpresion(): Expresion? {
        return expresion
    }

    fun setExpresion(expresion: Expresion?) {
        this.expresion = expresion
    }

    override fun toString(): String {
        return """
             Simbolo [nombre=$nombre, tipo=$tipo, fila=$fila, columna=$columna, ambito=$ambito, expresion=$expresion, tipoParametros=$tipoParametros]
             
             """.trimIndent()
    }
}