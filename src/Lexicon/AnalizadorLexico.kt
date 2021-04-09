package Lexicon

import lexicon.Categoria
import lexicon.Token
import java.util.ArrayList

/**
 * Clase que se encarga de analizar el codigo fuente y extraer cada uno de los
 * tokens que este contenga de acuerdo con las reglas establecidas por los
 * automatas de este lenguaje
 *
 * @author Esthefania Lemus - Diana Ramirez - Cristian Bonilla
 */
class AnalizadorLexico( val codigoFuente: String)  {

    private val listaTokens: ArrayList<Token>
    private var caracterActual: Char
    private val finCodigo: Char
    private var posActual = 0
    private var colActual = 0
    private var filaActual = 0
    private val palabrasReservadas = arrayOf("Excepcion", "Entero", "Real", "Bool", "Void", "String", "Char", "Para", "mientrasQue",
            "Privado", "Publico", "Paquete", "importar", "Clase", "Retorno", "Break", "metodo", "True", "False", "invo",
            "null", "leer", "imprimir", "arreglo", "si?", "sino!")
    var listaDeErrores: ArrayList<Error>

    /**
     * metodo constructor de AnalizadorLexico
     *
     *
     */
    init {
        listaTokens = ArrayList<Token>()
        caracterActual = codigoFuente[posActual]
        finCodigo = 0.toChar()
        listaDeErrores = ArrayList()
    }

    /**
     * metodo encargado de analizar cada uno de los caracteres del codigo fuente, y
     * de esta manera con ayuda de los automantas programados, lograr clasificar
     * cada uno de los Tokens encontrados
     */
    fun analizar() {
        while (caracterActual != finCodigo) {
            if (caracterActual == ' ' || caracterActual == '\n' || caracterActual == '\t' || caracterActual == '\r') {
                obtenerSgteCaracter()
                continue
            }
            if (esEntero()) continue
            listaTokens.add(Token(Categoria.DESCONOCIDO, "" + caracterActual, filaActual, colActual))
            obtenerSgteCaracter()
        }
    }
    //////////////////// AUTÓMATAS
    //////////////////// ////////////////////////////////////////////////////////
    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Numero Entero, y de ser asi, crea un token con esta categoria y con
     * lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esEntero(): Boolean {
        if (Character.isDigit(caracterActual)) {
            var palabra: String = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            palabra += caracterActual
            obtenerSgteCaracter()
            while (Character.isDigit(caracterActual)) {
                palabra += caracterActual
                obtenerSgteCaracter()
            }
            if (caracterActual == '.') {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            } else {
                listaTokens.add(Token(Categoria.ENTERO, palabra, fila, columna))
                return true
            }
        }
        // RI
        return false
    }

    ///////////////////////////////////// METODOS
    ///////////////////////////////////// AUXILIARES/////////////////////////////////////////////////
    /**
     * metodo que cada vez que se invoque, obtiene el siguiente caracter del codigo
     * fuente y actualiza las posiciones en donde encuentra dicho caracter
     */
    fun obtenerSgteCaracter() {
        posActual++
        if (posActual < codigoFuente.length) {
            if (caracterActual == '\n') {
                filaActual++
                colActual = 0
            } else {
                colActual++
            }
            caracterActual = codigoFuente[posActual]
        } else {
            caracterActual = finCodigo
        }
    }

}