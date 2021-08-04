package semantica

import sintaxis.Expresion
import java.util.*

/**
 * Estructura de datos que almacena la información de los simbolos usados en el programa
 * @author Esthefania Lemus - Diana Ramirez - Cristian Bonilla
 */
class TablaSimbolos(listaErrores: ArrayList<String?>?) {
    var listaSimbolos: ArrayList<Simbolo>
    private val listaErrores: ArrayList<String?>?

    /**
     * Permite guardar un símbolo de tipo variable en la tabla de símbolos
     */
    fun guardarSimboloVariable(nombre: String, tipo: String?, fila: Int, columna: Int, ambito: Simbolo, expresion: Expresion?): Simbolo? {
        val s = buscarSimboloVariable(nombre, ambito, fila, columna)
        if (s == null) {
            val nuevo = Simbolo(nombre, tipo!!, fila, columna, ambito, expresion)
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            if (listaErrores != null) {
                listaErrores.add("La variable $nombre ya existe en el ámbito $ambito")
            }
        }
        return null
    }

    /**
     * Permite guardar un símbolo de tipo función en la tabla de símbolos
     */
    fun guardarSimboloFuncion(nombre: String, tipo: String?, tipoParametros: ArrayList<String?>): Simbolo? {
        val s = buscarSimboloFuncion(nombre, tipoParametros)
        if (s == null) {
            val nuevo = Simbolo(nombre, tipo!!, tipoParametros)
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            if (listaErrores != null) {
                listaErrores.add("La función $nombre ya existe")
            }
        }
        return null
    }

    fun buscarSimboloVariable(nombre: String, ambito: Simbolo?, fila: Int, columna: Int): Simbolo? {
        for (simbolo in listaSimbolos) {
            if (simbolo.ambito != null) {
                if (nombre == simbolo.nombre && ambito!!.equals(simbolo.ambito)) {
                    return simbolo
                }
            }
        }
        return null
    }

    fun buscarSimboloFuncion(nombre: String, tiposParametros: ArrayList<String?>): Simbolo? {
        for (simbolo in listaSimbolos) {
            if (simbolo.tipoParametros != null) {
                if (nombre == simbolo.nombre && tiposParametros == simbolo.tipoParametros) {
                    return simbolo
                }
            }
        }
        return null
    }

    override fun toString(): String {
        return "TablaSimbolos [listaSimbolos=$listaSimbolos]"
    }

    init {
        listaSimbolos = ArrayList()
        this.listaErrores = listaErrores
    }
}