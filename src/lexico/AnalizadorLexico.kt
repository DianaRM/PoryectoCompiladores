package lexico

import lexico.Categoria

/**
 * Clase que se encarga de analizar el codigo fuente y extraer cada uno de los
 * tokens que este contenga de acuerdo con las reglas establecidas por los
 * automatas de este lenguaje
 *
 * @author Esthefania Lemus - Diana Ramirez - Cristian Bonilla
 */
class AnalizadorLexico(val codigoFuente: String) {

    val listaTokens: ArrayList<Token> = ArrayList<Token>()
    private var caracterActual: Char
    private val finCodigo: Char
    private var posActual = 0
    private var colActual = 0
    private var filaActual = 0
    private val lexemasReservadas = arrayOf("Excepcion", "Entero", "Real", "Bool", "Void", "String", "Char", "Para", "mientrasQue",
            "Privado", "Publico", "Paquete", "importar", "Clase", "Retorno", "Break", "metodo", "True", "False", "invo",
            "null", "leer", "imprimir", "arreglo", "si?", "sino!")
    var listaDeErrores: ArrayList<Error>

    /**
     * metodo constructor de AnalizadorLexico
     *
     *
     */
    init {
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
            var lexema: String = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            lexema += caracterActual
            obtenerSgteCaracter()
            while (Character.isDigit(caracterActual)) {
                lexema += caracterActual
                obtenerSgteCaracter()
            }
            if (caracterActual == '.') {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            } else if (caracterActual == finCodigo || caracterActual == ';') {
                listaTokens.add(Token(Categoria.ENTERO, lexema, fila, columna))
                return true
            } else {
                return false
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
        if (caracterActual == '.' || caracterActual.isDigit()) {
            val aux = posActual
            var lexema: String = ""
            val fila = filaActual
            val columna = colActual
            lexema += caracterActual

            if (caracterActual.isDigit()) {
                obtenerSgteCaracter()

                while (caracterActual.isDigit()) {
                    lexema += caracterActual
                    obtenerSgteCaracter()
                }

                if (caracterActual != '.' && caracterActual != ';') {
                    posActual = aux
                    colActual = columna
                    filaActual = fila
                    caracterActual = codigoFuente[posActual]
                    return false
                }
            } else {
                obtenerSgteCaracter()
                if (!caracterActual.isDigit()) {
                    posActual = aux
                    colActual = columna
                    filaActual = fila
                    caracterActual = codigoFuente[posActual]
                    return false
                }
            }

            lexema += caracterActual
            obtenerSgteCaracter()

            while (caracterActual.isDigit()) {
                lexema += caracterActual
                obtenerSgteCaracter()
            }
            listaTokens.add(Token(Categoria.DECIMAL, lexema, fila, columna))
            return true
        } else {
            return false
        }
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
            var lexema: String = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            lexema += caracterActual
            obtenerSgteCaracter()
            if (!(Character.isDigit(caracterActual) || Character.isLetter(caracterActual) || caracterActual == '_')) {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            } else {
                lexema += caracterActual
                obtenerSgteCaracter()
                while (Character.isDigit(caracterActual) || Character.isLetter(caracterActual)
                        || caracterActual == '_') {
                    lexema += caracterActual
                    obtenerSgteCaracter()
                }

                if (lexema.length <= 10) {
                    listaTokens.add(Token(Categoria.IDENTIFICADOR, lexema, fila, columna))
                    return true
                } else {
                    posActual = aux
                    colActual = columna
                    filaActual = fila
                    caracterActual = codigoFuente[posActual]
                    listaDeErrores.add(lexico.Error(filaActual, colActual, "El identificador tiene mas de 10 letras"))
                    return false
                }
            }
        }
        return false
    }

    /**
     * metodo que determina si los caracteres que se analizan pertenecen a la
     * categoria lexema Reservada, y de ser asi, crea un token con esta categoria y
     * con lo que lleve concatenado hasta el momento
     *
     * Lista de lexemas reservadas: Entero, Real, Para, Mientras, Privado, Publico,
     * Paquete, Importar, Clase, Return, Break
     *
     * @return true si pertenece a esta categoria, false en caso contrario
     */
    fun esReservada(): Boolean {

        val aux = posActual
        val columna = colActual
        val fila = filaActual

        var lexema = ""
        if (caracterActual.isLetter()) {
            lexema += caracterActual
            obtenerSgteCaracter()
            while (caracterActual.isLetter()) {
                lexema += caracterActual
                obtenerSgteCaracter()
            }
        }
        if(lexemasReservadas.contains(lexema)){
            listaTokens.add(Token(Categoria.RESERVADA,lexema,fila,columna))
            return true
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
            var lexema: String = ""
            val fila = filaActual
            val columna = colActual
            lexema += caracterActual
            obtenerSgteCaracter()
            listaTokens.add(Token(Categoria.OPERADOR_ARITMETICO, lexema, fila, columna))
            return true
        } else if (caracterActual == '-') {
            if (posActual + 1 < codigoFuente.length) {
                if (codigoFuente[posActual + 1] == '-' || codigoFuente[posActual + 1] == '=') {
                    return false
                }
            }
            var lexema: String = ""
            val fila = filaActual
            val columna = colActual
            lexema += caracterActual
            obtenerSgteCaracter()
            listaTokens.add(Token(Categoria.OPERADOR_ARITMETICO, lexema, fila, columna))
            return true
        } else if (caracterActual == '*' || caracterActual == '/' || caracterActual == '%') {
            if (posActual + 1 < codigoFuente.length) {
                if (codigoFuente[posActual + 1] == '=') {
                    return false
                }
            }
            var lexema: String = ""
            val fila = filaActual
            val columna = colActual
            lexema += caracterActual
            obtenerSgteCaracter()
            listaTokens.add(Token(Categoria.OPERADOR_ARITMETICO, lexema, fila, columna))
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
        var lexema: String = ""
        val fila = filaActual
        val columna = colActual
        if (caracterActual == '<' || caracterActual == '>') {
            lexema += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '=') {
                lexema += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_RELACIONAL, lexema, fila, columna))
            } else {
                listaTokens.add(Token(Categoria.OPERADOR_RELACIONAL, lexema, fila, columna))
            }
            return true
        } else if (caracterActual == '!' || caracterActual == '=') {
            val aux = posActual
            lexema += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '=') {
                lexema += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_RELACIONAL, lexema, fila, columna))
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
            var lexema: String = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            lexema += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '&') {
                lexema += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_LOGICO_BINARIO, lexema, fila, columna))
                return true
            } else {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            }
        } else if (caracterActual == '|') {
            var lexema: String = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            lexema += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '|') {
                lexema += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_LOGICO_BINARIO, lexema, fila, columna))
                return true
            } else {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            }
        } else if (caracterActual == '!') {
            var lexema: String = ""
            val fila = filaActual
            val columna = colActual
            lexema += caracterActual
            obtenerSgteCaracter()
            listaTokens.add(Token(Categoria.OPERADOR_LOGICO_UNARIO, lexema, fila, columna))
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
        var lexema: String = ""
        val fila = filaActual
        val columna = colActual
        val aux = posActual
        if (caracterActual == '+' || caracterActual == '-' || caracterActual == '/' || caracterActual == '*' || caracterActual == '%') {
            lexema += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '=') {
                lexema += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_ASIGNACION, lexema, fila, columna))
                return true
            } else {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            }
        } else if (caracterActual == '=') {
            lexema += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '=') {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            } else {
                listaTokens.add(Token(Categoria.OPERADOR_ASIGNACION, lexema, fila, columna))
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
        var lexema: String = ""
        val fila = filaActual
        val columna = colActual
        val aux = posActual
        if (caracterActual == '+') {
            lexema += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '+') {
                lexema += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_INCREMENTO_DECREMENTO, lexema, fila, columna))
                return true
            } else {
                posActual = aux
                colActual = columna
                filaActual = fila
                caracterActual = codigoFuente[posActual]
                return false
            }
        } else if (caracterActual == '-') {
            lexema += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '-') {
                lexema += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.OPERADOR_INCREMENTO_DECREMENTO, lexema, fila, columna))
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
            var lexema: String = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            lexema += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == 'A' || caracterActual == 'B' || caracterActual == 'C' || caracterActual == 'D' || caracterActual == 'E' || caracterActual == 'F' || Character.isDigit(caracterActual)) {
                lexema += caracterActual
                obtenerSgteCaracter()
                while (caracterActual == 'A' || caracterActual == 'B' || caracterActual == 'C' || caracterActual == 'D' || caracterActual == 'E' || caracterActual == 'F' || Character.isDigit(caracterActual)) {
                    lexema += caracterActual
                    obtenerSgteCaracter()
                }
                listaTokens.add(Token(Categoria.HEXADECIMAL, lexema, fila, columna))
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
            var lexema: String = ""
            val fila = filaActual
            val columna = colActual
            lexema += caracterActual
            obtenerSgteCaracter()
            while (caracterActual != '~') {
                if (caracterActual == finCodigo) {
                    listaDeErrores.add(Error(fila, columna,
                            " El simbolo '~' de apertura de la cadena de caracteres\n esta abierto pero nunca fue cerrado"))
                    return true
                } else if (caracterActual == '\\') {
                    lexema += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == '~' || caracterActual == 'b' || caracterActual == 't' || caracterActual == 'f' || caracterActual == 'n' || caracterActual == 'r' || caracterActual == '\\') {
                        lexema += caracterActual
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
                    lexema += caracterActual
                    obtenerSgteCaracter()
                }
            }
            lexema += caracterActual
            obtenerSgteCaracter()
            listaTokens.add(Token(Categoria.CADENA_CARACTERES, lexema, fila, columna))
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
            var lexema = ""
            val fila = filaActual
            val columna = colActual
            lexema += caracterActual
            obtenerSgteCaracter()
            while (caracterActual != finCodigo) {
                if (caracterActual == finCodigo) {
                    listaTokens.add(Token(Categoria.ERROR,
                            "$lexema El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado", fila,
                            columna))
                    listaDeErrores.add(Error(fila, columna,
                            " El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado"))
                    return true
                } else if (caracterActual == '\\') {
                    lexema += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == finCodigo) {
                        listaTokens.add(Token(Categoria.ERROR,
                                "$lexema El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado",
                                fila, columna))
                        listaDeErrores.add(Error(fila, columna,
                                " El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado"))
                        return true
                    } else if ((caracterActual == 'n') || (caracterActual == '$') || (caracterActual == 't'
                                    ) || (caracterActual == 'r') || (caracterActual == 'f') || (caracterActual == 'b'
                                    ) || (caracterActual == '\\')) {
                        lexema += caracterActual
                        obtenerSgteCaracter()
                        if (caracterActual == '$') {
                            lexema += caracterActual
                            obtenerSgteCaracter()
                            listaTokens.add(Token(Categoria.CARACTER, lexema, fila, columna))
                            return true
                        } else {
                            listaTokens.add(Token(Categoria.ERROR, (lexema
                                    + " El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado"), fila,
                                    columna))
                            listaDeErrores.add(Error(fila, columna,
                                    " El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado"))
                            return true
                        }
                    } else {
                        lexema += caracterActual
                        obtenerSgteCaracter()
                        if (caracterActual == '$') {
                            listaTokens.add(Token(Categoria.ERROR,
                                    (lexema + " Secuencia de escape invalida (las validas son: " + '\\' + "b " + '\\' + "t "
                                            + '\\' + "n " + '\\' + "f " + '\\' + "r " + '\\' + "$ " + ")"),
                                    fila, columna))
                            listaDeErrores.add(Error(fila, columna,
                                    (" Secuencia de escape invalida (las validas son: " + '\\' + "b " + '\\' + "t " + '\\'
                                            + "n " + '\\' + "f " + '\\' + "r " + '\\' + "$ " + ")")))
                            obtenerSgteCaracter()
                            return true
                        } else {
                            listaTokens.add(Token(Categoria.ERROR, (lexema
                                    + " El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado"), fila,
                                    columna))
                            listaDeErrores.add(Error(fila, columna,
                                    " El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado"))
                            obtenerSgteCaracter()
                            return true
                        }
                    }
                } else if (caracterActual == '$') {
                    lexema += caracterActual
                    obtenerSgteCaracter()
                    listaTokens.add(Token(Categoria.ERROR, "$lexema El caracter esta vacio", fila, columna))
                    return true
                } else {
                    lexema += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == finCodigo) {
                        listaTokens.add(Token(Categoria.ERROR,
                                "$lexema El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado",
                                fila, columna))
                        return true
                    } else if (caracterActual == '$') {
                        lexema += caracterActual
                        obtenerSgteCaracter()
                        listaTokens.add(Token(Categoria.CARACTER, lexema, fila, columna))
                        return true
                    }
                }
                listaTokens.add(Token(Categoria.ERROR,
                        "$lexema El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado", fila,
                        columna))
                listaDeErrores.add(Error(fila, columna,
                        " El simbolo '$' de apertura del caracter esta abierto pero nunca fue cerrado"))
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
            var lexema: String = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            lexema += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '¬') {
                lexema += caracterActual
                obtenerSgteCaracter()
                while (caracterActual != '\n') {
                    if (caracterActual == finCodigo) {
                        listaTokens.add(Token(Categoria.COMENTARIO_LINEA, lexema, fila, columna))
                        return true
                    }
                    lexema += caracterActual
                    obtenerSgteCaracter()
                }
                lexema += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(Categoria.COMENTARIO_LINEA, lexema, fila, columna))
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
            var lexema = ""
            val fila = filaActual
            val columna = colActual
            val aux = posActual
            lexema += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '*') {
                lexema += caracterActual
                obtenerSgteCaracter()
                while (caracterActual != finCodigo) {
                    if (caracterActual == '*') {
                        lexema += caracterActual
                        obtenerSgteCaracter()
                        if (caracterActual == '¬') {
                            lexema += caracterActual
                            obtenerSgteCaracter()
                            listaTokens.add(Token(Categoria.COMENTARIO_BLOQUE, lexema, fila, columna))
                            return true
                        }
                        lexema += caracterActual
                        obtenerSgteCaracter()
                    }
                    lexema += caracterActual
                    obtenerSgteCaracter()
                }
                listaTokens.add(Token(Categoria.ERROR,
                        "$lexema El comentario de bloque fue abierto pero nunca fue cerrado", fila, columna))
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