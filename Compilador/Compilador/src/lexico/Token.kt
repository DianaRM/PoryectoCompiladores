package lexico

import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.*

/**
 * Clase que representa la categoria a la que pertence un simbolo
 *
 * @author Esthefania Lemus - Diana Ramirez - Cristian Bonilla
 */
class Token(categoria: Categoria, lexema: String, fila: Int, columna: Int) {
    private var categoria: Categoria
    var lexema: String
    var fila: Int
    var columna: Int

    fun getCategoria(): Categoria {
        return categoria
    }

    fun setCategoria(categoria: Categoria) {
        this.categoria = categoria
    }

    override fun toString(): String {
        return """Token [categoria= $categoria , palabra= $lexema , fila= $fila , columna= $columna]
"""
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<String?>, ambito: Simbolo) {
        if (categoria === Categoria.IDENTIFICADOR) {
            if (tablaSimbolos.buscarSimboloVariable(lexema, ambito, fila, columna) == null) {
                erroresSemanticos.add("La variable $lexema no esta declarada")
            }
        }
    }

    fun traducirReservada(): String {
        if (lexema == "Entero") {
            return "int"
        } else if (lexema == "Real") {
            return "double"
        } else if (lexema == "Bool") {
            return "boolean"
        } else if (lexema == "Void") {
            return "void"
        } else if (lexema == "Char") {
            return "char"
        } else if (lexema == "Para") {
            return "for"
        } else if (lexema == "mientrasQue") {
            return "while"
        } else if (lexema == "Privado") {
            return "private"
        } else if (lexema == "Publico") {
            return "public"
        } else if (lexema == "Retorno") {
            return "return"
        } else if (lexema == "True") {
            return "true"
        } else if (lexema == "False") {
            return "false"
        } else if (lexema == "leer") {
            return "JOptionPane.showInputDialog"
        } else if (lexema == "imprimir") {
            return "JOptionPane.showMessageDialog"
        } else if (lexema == "si?") {
            return "if"
        } else if (lexema == "sino!") {
            return "else"
        }
        return ""
    }

    fun traducirIdentificador(): String {
        return lexema.substring(1, lexema.length)
    }

    init {
        this.categoria = categoria
        this.lexema = lexema
        this.fila = fila
        this.columna = columna
    }
}