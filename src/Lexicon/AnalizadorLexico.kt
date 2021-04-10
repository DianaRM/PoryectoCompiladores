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
            if (esReal()) continue
            if (esIdentificador()) continue
            if (esReservada()) continue
            if (esOperadorAritmetico()) continue
            if (esOperadorRelacional()) continue
            if (esOperadorLogico()) continue
            if (esOperadorDeAsignacion()) continue

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
    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Numero Real, y de ser asi, crea un token con esta categoria y con
     * lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esReal(): Boolean {
        if (caracterActual == '.') {
            if (!Character.isDigit(codigoFuente[posActual + 1])) {
                return false
            }
            var palabra: String = ""
            val fila = filaActual
            val columna = colActual
            palabra += caracterActual
            obtenerSgteCaracter()
            while (Character.isDigit(caracterActual)) {
                palabra += caracterActual
                obtenerSgteCaracter()
            }
            listaTokens.add(Token(Categoria.DECIMAL, palabra, fila, columna))
            return true
        } else if (Character.isDigit(caracterActual)) {
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
            if (caracterActual != '.') {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            } else {
                palabra += caracterActual
                obtenerSgteCaracter()
                while (Character.isDigit(caracterActual)) {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                }
                listaTokens.add(Token(Categoria.DECIMAL, palabra, fila, columna))
                return true
            }
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Identificador, y de ser asi, crea un token con esta categoria y con
     * lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esIdentificador(): Boolean {
        if (caracterActual == '#') {
            var palabra: String = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            palabra += caracterActual
            obtenerSgteCaracter()
            if (!(Character.isDigit(caracterActual) || Character.isLetter(caracterActual) || caracterActual == '_')) {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            } else {
                palabra += caracterActual
                obtenerSgteCaracter()
                while (Character.isDigit(caracterActual) || Character.isLetter(caracterActual)
                        || caracterActual == '_') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                }
                listaTokens.add(Token(Categoria.IDENTIFICADOR, palabra, fila, columna))
                return true
            }
        }
        return false
    }
    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Palabra Reservada, y de ser asi, crea un token con esta categoria y
     * con lo que lleve concatenado hasta el momento
     *
     * Lista de palabras reservadas: Entero, Real, Para, Mientras, Privado, Publico,
     * Paquete, Importar, Clase, Return, Break
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esReservada(): Boolean {
        for (i in palabrasReservadas.indices) {
            if (posActual + palabrasReservadas[i].length <= codigoFuente.length && (palabrasReservadas[i]
                            == codigoFuente.substring(posActual, posActual + palabrasReservadas[i].length))) {
                listaTokens.add(Token(Categoria.RESERVADA, palabrasReservadas[i], filaActual, colActual))
                colActual += palabrasReservadas[i].length
                posActual += palabrasReservadas[i].length - 1
                obtenerSgteCaracter()
                return true
            }
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Operador aritmetico, y de ser asi, crea un token con esta categoria
     * y con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esOperadorAritmetico(): Boolean {
        if (caracterActual == '+') {
            if (posActual + 1 < codigoFuente.length) {
                if (codigoFuente[posActual + 1] == '+' || codigoFuente[posActual + 1] == '=') {
                    return false
                }
            }
            var palabra: String = ""
            val fila = filaActual
            val columna = colActual
            palabra += caracterActual
            obtenerSgteCaracter()
            listaTokens.add(Token(Categoria.OPERADOR_ARITMETICO, palabra, fila, columna))
            return true
        } else if (caracterActual == '-') {
            if (posActual + 1 < codigoFuente.length) {
                if (codigoFuente[posActual + 1] == '-' || codigoFuente[posActual + 1] == '=') {
                    return false
                }
            }
            var palabra: String = ""
            val fila = filaActual
            val columna = colActual
            palabra += caracterActual
            obtenerSgteCaracter()
            listaTokens.add(Token(Categoria.OPERADOR_ARITMETICO, palabra, fila, columna))
            return true
        } else if (caracterActual == '*' || caracterActual == '/' || caracterActual == '%') {
            if (posActual + 1 < codigoFuente.length) {
                if (codigoFuente[posActual + 1] == '=') {
                    return false
                }
            }
            var palabra: String = ""
            val fila = filaActual
            val columna = colActual
            palabra += caracterActual
            obtenerSgteCaracter()
            listaTokens.add(Token(Categoria.OPERADOR_ARITMETICO, palabra, fila, columna))
            return true
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Operador Relacional, y de ser asi, crea un token con esta categoria
     * y con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esOperadorRelacional(): Boolean {
        var palabra: String = ""
        val fila = filaActual
        val columna = colActual
        if (caracterActual == '<' || caracterActual == '>') {
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '=') {
                palabra += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_RELACIONAL, palabra, fila, columna))
            } else {
                listaTokens.add(Token(Categoria.OPERADOR_RELACIONAL, palabra, fila, columna))
            }
            return true
        } else if (caracterActual == '!' || caracterActual == '=') {
            val aux = posActual
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '=') {
                palabra += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_RELACIONAL, palabra, fila, columna))
                return true
            } else {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            }
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Operador logico, y de ser asi, crea un token con esta categoria y
     * con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esOperadorLogico(): Boolean {
        if (caracterActual == '&') {
            var palabra: String = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '&') {
                palabra += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_LOGICO_BINARIO, palabra, fila, columna))
                return true
            } else {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            }
        } else if (caracterActual == '|') {
            var palabra: String = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '|') {
                palabra += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_LOGICO_BINARIO, palabra, fila, columna))
                return true
            } else {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            }
        } else if (caracterActual == '!') {
            var palabra: String = ""
            val fila = filaActual
            val columna = colActual
            palabra += caracterActual
            obtenerSgteCaracter()
            listaTokens.add(Token(Categoria.OPERADOR_LOGICO_UNARIO, palabra, fila, columna))
            return true
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Operador de asignacion, y de ser asi, crea un token con esta
     * categoria y con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esOperadorDeAsignacion(): Boolean {
        var palabra: String = ""
        val fila = filaActual
        val columna = colActual
        val aux = posActual
        if (caracterActual == '+' || caracterActual == '-' || caracterActual == '/' || caracterActual == '*' || caracterActual == '%') {
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '=') {
                palabra += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_ASIGNACION, palabra, fila, columna))
                return true
            } else {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            }
        } else if (caracterActual == '=') {
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '=') {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            } else {
                listaTokens.add(Token(Categoria.OPERADOR_ASIGNACION, palabra, fila, columna))
                return true
            }
        }
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