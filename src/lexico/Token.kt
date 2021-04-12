package lexico

import lexicon.Categoria


/**
 * Clase que representa la categoria a la que pertence un simbolo
 *
 * @author Esthefania Lemus - Diana Ramirez - Cristian Bonilla
 */
class Token(var categoria: Categoria, var lexema: String, var fila: Int, var columna: Int) {


    override fun toString(): String {
        return """Token [categoria= $categoria , palabra= $lexema , fila= $fila , columna= $columna]
"""
    }

    /**
     * metodo constructor de Token
     *
     * @param categoria, es un ENUM
     * @param lexema
     * @param fila
     * @param columna
     */
    init {
        this.categoria = categoria
        this.lexema = lexema
        this.fila = fila
        this.columna = columna
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


}