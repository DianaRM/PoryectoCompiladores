package lexico

import lexicon.Categoria

/**
 * Clase que se encarga de analizar el codigo fuente y extraer cada uno de los
 * tokens que este contenga de acuerdo con las reglas establecidas por los
 * automatas de este lenguaje
 *
 * @author Esthefania Lemus - Diana Ramirez - Cristian Bonilla
 */
class AnalizadorLexico( val codigoFuente: String)  {

    val listaTokens: ArrayList<Token>
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
            if (esOperadorDeIncrementoDecremento()) continue
            if (esParentesisApertura()) continue
            if (esParentesisCierre()) continue
            if (esLlaveApertura()) continue
            if (esLlaveCierre()) continue
            if (esCorcheteApertura()) continue
            if (esCorcheteCierre()) continue
            if (esTerminal()) continue
            if (esSeparador()) continue
            if (esCadenaDeCaracteres()) continue
            if (esComentarioDeLinea()) continue
            if (esComentarioDeBloque()) continue
            if (esDosPuntos()) continue
            if (esPunto()) continue
            if (esCaracter()) continue


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
            if(posActual == 0){
                if(codigoFuente.length < 2){
                    return false
                }
            }
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

                if(palabra.length <= 10) {
                    listaTokens.add(Token(Categoria.IDENTIFICADOR, palabra, fila, columna))
                    return true
                }else{
                    posActual = aux
                    colActual = columna
                    filaActual = fila
                    caracterActual = codigoFuente[posActual]
                }
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


    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Operador de Incremento y Decremento, y de ser asi, crea un token
     * con esta categoria y con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esOperadorDeIncrementoDecremento(): Boolean {
        var palabra: String = ""
        val fila = filaActual
        val columna = colActual
        val aux = posActual
        if (caracterActual == '+') {
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '+') {
                palabra += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_INCREMENTO_DECREMENTO, palabra, fila, columna))
                return true
            } else {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            }
        } else if (caracterActual == '-') {
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '-') {
                palabra += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_INCREMENTO_DECREMENTO, palabra, fila, columna))
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
     * categoria Parentesis de Apertura, y de ser asi, crea un token con esta
     * categoria y con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esParentesisApertura(): Boolean {
        if (caracterActual == '(') {
            listaTokens.add(Token(Categoria.PARENTESIS_APERTURA, "(", filaActual, colActual))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Parentesis de Cierre, y de ser asi, crea un token con esta
     * categoria y con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esParentesisCierre(): Boolean {
        if (caracterActual == ')') {
            listaTokens.add(Token(Categoria.PARENTESIS_CIERRE, ")", filaActual, colActual))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Llave de Apertura, y de ser asi, crea un token con esta categoria y
     * con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esLlaveApertura(): Boolean {
        if (caracterActual == '{') {
            listaTokens.add(Token(Categoria.LLAVE_APERTURA, "{", filaActual, colActual))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Llave de Cierre, y de ser asi, crea un token con esta categoria y
     * con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esLlaveCierre(): Boolean {
        if (caracterActual == '}') {
            listaTokens.add(Token(Categoria.LLAVE_CIERRE, "}", filaActual, colActual))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Corchete de Apertura, y de ser asi, crea un token con esta
     * categoria y con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esCorcheteApertura(): Boolean {
        if (caracterActual == '[') {
            listaTokens.add(Token(Categoria.CORCHETE_APERTURA, "[", filaActual, colActual))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Corchete de Cierre, y de ser asi, crea un token con esta categoria
     * y con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esCorcheteCierre(): Boolean {
        if (caracterActual == ']') {
            listaTokens.add(Token(Categoria.CORCHETE_CIERRE, "]", filaActual, colActual))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Terminal(Fin de sentencia), y de ser asi, crea un token con esta
     * categoria y con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esTerminal(): Boolean {
        if (caracterActual == ';') {
            listaTokens.add(Token(Categoria.TERMINAL, ";", filaActual, colActual))
            obtenerSgteCaracter()
            return true
        }
        return false
    }
    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Separador, y de ser asi, crea un token con esta categoria y con lo
     * que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esSeparador(): Boolean {
        if (caracterActual == ',') {
            listaTokens.add(Token(Categoria.SEPARADOR, ",", filaActual, colActual))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Hexadecimal, y de ser asi, crea un token con esta categoria y con
     * lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esHexadecimal(): Boolean {
        if (caracterActual == '°') {
            var palabra: String = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == 'A' || caracterActual == 'B' || caracterActual == 'C' || caracterActual == 'D' || caracterActual == 'E' || caracterActual == 'F' || Character.isDigit(caracterActual)) {
                palabra += caracterActual
                obtenerSgteCaracter()
                while (caracterActual == 'A' || caracterActual == 'B' || caracterActual == 'C' || caracterActual == 'D' || caracterActual == 'E' || caracterActual == 'F' || Character.isDigit(caracterActual)) {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                }
                listaTokens.add(Token(Categoria.HEXADECIMAL, palabra, fila, columna))
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
     * categoria Cadena de Caracteres, y de ser asi, crea un token con esta
     * categoria y con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esCadenaDeCaracteres(): Boolean {
        if (caracterActual == '~') {
            var palabra: String = ""
            val fila = filaActual
            val columna = colActual
            palabra += caracterActual
            obtenerSgteCaracter()
            while (caracterActual != '~') {
                if (caracterActual == finCodigo) {
                    listaDeErrores.add(Error(fila, columna,
                            " El simbolo '~' de apertura de la cadena de caracteres esta abierto pero nunca fue cerrado"))
                    return true
                } else if (caracterActual == '\\') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == '~' || caracterActual == 'b' || caracterActual == 't' || caracterActual == 'f' || caracterActual == 'n' || caracterActual == 'r' || caracterActual == '\\') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                    } else {
                        listaDeErrores.add(Error(fila, columna,
                                " Secuencia de escape invalida (las validas son: " + '\\' + "b " + '\\' + "t " + '\\'
                                        + "n " + '\\' + "f " + '\\' + "r " + '\\' + "~ " + ")"))
                        listaTokens.add(Token(
                                Categoria.ERROR, (" Secuencia de escape invalida (las validas son: " + '\\' + "b " + '\\'
                                + "t " + '\\' + "n " + '\\' + "f " + '\\' + "r " + '\\' + "~ " + ")"),
                                fila, columna))
                    }
                } else {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                }
            }
            palabra += caracterActual
            obtenerSgteCaracter()
            listaTokens.add(Token(Categoria.CADENA_CARACTERES, palabra, fila, columna))
            return true
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria Caracter, y de ser asi, crea un token con esta categoria y con lo
     * que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esCaracter(): Boolean {
        if (caracterActual == '$') {
            var palabra = ""
            val fila = filaActual
            val columna = colActual
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == finCodigo) {
                listaTokens.add(Token(Categoria.ERROR,
                        "$palabra El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado", fila,
                        columna))
                listaDeErrores.add(Error(fila, columna,
                        " El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado"))
                return true
            } else if (caracterActual == '\\') {
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == finCodigo) {
                    listaTokens.add(Token(Categoria.ERROR,
                            "$palabra El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado",
                            fila, columna))
                    listaDeErrores.add(Error(fila, columna,
                            " El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado"))
                    return true
                } else if ((caracterActual == 'n') || (caracterActual == '$') || (caracterActual == 't'
                                ) || (caracterActual == 'r') || (caracterActual == 'f') || (caracterActual == 'b'
                                ) || (caracterActual == '\\')) {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == '$') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        listaTokens.add(Token(Categoria.CARACTER, palabra, fila, columna))
                        return true
                    } else {
                        listaTokens.add(Token(Categoria.ERROR, (palabra
                                + " El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado"), fila,
                                columna))
                        listaDeErrores.add(Error(fila, columna,
                                " El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado"))
                        return true
                    }
                } else {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == '$') {
                        listaTokens.add(Token(Categoria.ERROR,
                                (palabra + " Secuencia de escape invalida (las validas son: " + '\\' + "b " + '\\' + "t "
                                        + '\\' + "n " + '\\' + "f " + '\\' + "r " + '\\' + "$ " + ")"),
                                fila, columna))
                        listaDeErrores.add(Error(fila, columna,
                                (" Secuencia de escape invalida (las validas son: " + '\\' + "b " + '\\' + "t " + '\\'
                                        + "n " + '\\' + "f " + '\\' + "r " + '\\' + "$ " + ")")))
                        obtenerSgteCaracter()
                        return true
                    } else {
                        listaTokens.add(Token(Categoria.ERROR, (palabra
                                + " El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado"), fila,
                                columna))
                        listaDeErrores.add(Error(fila, columna,
                                " El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado"))
                        obtenerSgteCaracter()
                        return true
                    }
                }
            } else if (caracterActual == '$') {
                palabra += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.ERROR, "$palabra El caracter esta vacio", fila, columna))
                return true
            } else {
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == finCodigo) {
                    listaTokens.add(Token(Categoria.ERROR,
                            "$palabra El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado",
                            fila, columna))
                    return true
                } else if (caracterActual == '$') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    listaTokens.add(Token(Categoria.CARACTER, palabra, fila, columna))
                    return true
                }
                listaTokens.add(Token(Categoria.ERROR,
                        "$palabra El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado", fila,
                        columna))
                return true
            }
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria comentario de linea, y de ser asi, crea un token con esta categoria
     * y con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esComentarioDeLinea(): Boolean {
        if (caracterActual == '¬') {
            var palabra: String = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '¬') {
                palabra += caracterActual
                obtenerSgteCaracter()
                while (caracterActual != '\n') {
                    if (caracterActual == finCodigo) {
                        listaTokens.add(Token(Categoria.COMENTARIO_LINEA, palabra, fila, columna))
                        return true
                    }
                    palabra += caracterActual
                    obtenerSgteCaracter()
                }
                palabra += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.COMENTARIO_LINEA, palabra, fila, columna))
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
     * categoria Comentario de bloque, y de ser asi, crea un token con esta
     * categoria y con lo que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esComentarioDeBloque(): Boolean {
        if (caracterActual == '¬') {
            var palabra = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '*') {
                palabra += caracterActual
                obtenerSgteCaracter()
                while (caracterActual != finCodigo) {
                    if (caracterActual == '*') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        if (caracterActual == '¬') {
                            palabra += caracterActual
                            obtenerSgteCaracter()
                            listaTokens.add(Token(Categoria.COMENTARIO_BLOQUE, palabra, fila, columna))
                            return true
                        }
                        palabra += caracterActual
                        obtenerSgteCaracter()
                    }
                    palabra += caracterActual
                    obtenerSgteCaracter()
                }
                listaTokens.add(Token(Categoria.ERROR,
                        "$palabra El comentario de bloque fue abierto pero nunca fue cerrado", fila, columna))
                listaDeErrores
                        .add(Error(fila, columna, " El comentario de bloque fue abierto pero nunca fue cerrado"))
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
     * categoria Dos_puntos, y de ser asi, crea un token con esta categoria y con lo
     * que lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esDosPuntos(): Boolean {
        if (caracterActual == ':') {
            listaTokens.add(Token(Categoria.DOS_PUNTOS, ":", filaActual, colActual))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria puntos, y de ser asi, crea un token con esta categoria y con lo que
     * lleve concatenado hasta el momento
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esPunto(): Boolean {
        if (caracterActual == '.') {
            listaTokens.add(Token(Categoria.PUNTO, ".", filaActual, colActual))
            obtenerSgteCaracter()
            return true
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