package sintaxis

import lexico.Categoria
import lexico.Error
import lexico.Token
import java.util.*

/**
 * Analizador Sintactico del compilador
 *
 * @author Esthefania Lemus - Diana Ramirez - Cristian Bonilla
 */
class AnalizadorSintactico(private val listaTokens: ArrayList<Token>) {
    private var posActual = 0
    private var tokenActual: Token
    val listaErrores: ArrayList<Error>
    fun obtenerSiguienteToken() {
        posActual++
        if (posActual < listaTokens.size) {
            tokenActual = listaTokens[posActual]
        }
    }

    fun obtenerAnteriorToken() {
        posActual--
        if (posActual >= 0) {
            tokenActual = listaTokens[posActual]
        }
    }

    fun reportarError(mensaje: String?) {
        listaErrores.add(Error(tokenActual.fila, tokenActual.columna, mensaje!!))
    }

    /**
     * <UnidadDeCompilacion> ::= <ListaFunciones>
    </ListaFunciones></UnidadDeCompilacion> */
    fun esUnidadDeCompilacion(): UnidadDeCompilacion? {
        val f: ArrayList<Funcion> = esListaFunciones()
        return if (f != null) {
            UnidadDeCompilacion(f)
        } else null
    }

    /**
     * <ListaFunciones> ::= <Funcion>[<ListaFunciones>]
    </ListaFunciones></Funcion></ListaFunciones> */
    private fun esListaFunciones(): ArrayList<Funcion> {
        val lista: ArrayList<Funcion> = ArrayList<Funcion>()
        var f: Funcion? = esFuncion()
        while (f != null) {
            lista.add(f)
            f = esFuncion()
        }
        return lista
    }

    /**
     * <Funcion> ::= metodo <TipoRetorno> identificador "("[<ListaParametros>]")"
     * "{"[<ListaSentencias>]"}"
    </ListaSentencias></ListaParametros></TipoRetorno></Funcion> */
    private fun esFuncion(): Funcion? {
        if (tokenActual.getCategoria() === Categoria.RESERVADA && tokenActual.lexema == "metodo") {
            obtenerSiguienteToken()
            val tipoRetorno = esTipoRetorno()
            if (tipoRetorno != null) {
                obtenerSiguienteToken()
                if (tokenActual.getCategoria() === Categoria.IDENTIFICADOR) {
                    val nombre = tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.getCategoria() === Categoria.PARENTESIS_APERTURA) {
                        obtenerSiguienteToken()
                        val parametros: ArrayList<Parametro> = esListaParametros()
                        if (tokenActual.getCategoria() === Categoria.PARENTESIS_CIERRE) {
                            obtenerSiguienteToken()
                            if (tokenActual.getCategoria() === Categoria.LLAVE_APERTURA) {
                                obtenerSiguienteToken()
                                val listaSentencias: ArrayList<Sentencia> = esListaSentencias()
                                if (tokenActual.getCategoria() === Categoria.LLAVE_CIERRE) {
                                    obtenerSiguienteToken()
                                    return Funcion(nombre, parametros, tipoRetorno, listaSentencias)
                                } else {
                                    reportarError("Falta la llave de cierre")
                                }
                            } else {
                                reportarError("Falta la llave de apertura")
                            }
                        } else {
                            reportarError("Falta el parentesis de cierre")
                        }
                    } else {
                        reportarError("Falta el parentesis de apertura")
                    }
                } else {
                    reportarError("Falta el nombre de la funcion")
                }
            } else {
                reportarError("Falta retorno")
            }
        }
        return null
    }

    /**
     * <ListaSentencias> ::= <Sentencia>[<ListaSentencias>]
    </ListaSentencias></Sentencia></ListaSentencias> */
    private fun esListaSentencias(): ArrayList<Sentencia> {
        val lista: ArrayList<Sentencia> = ArrayList<Sentencia>()
        var s: Sentencia? = esSentencia()
        while (s != null) {
            lista.add(s)
            s = esSentencia()
        }
        return lista
    }

    /**
     * <Sentencia> ::= <Decision> | <DeclaracionVariable> | <Asignacion> |
     * <Impresion> | <Ciclo> | <Retorno> | <LecturaDatos> | <InvocacionFuncion>
    </InvocacionFuncion></LecturaDatos></Retorno></Ciclo></Impresion></Asignacion></DeclaracionVariable></Decision></Sentencia> */
    private fun esSentencia(): Sentencia? {
        var s: Sentencia? = null
        s = esDecision()
        if (s != null) {
            return s
        }
        s = esDeclaracionVariable()
        if (s != null) {
            return s
        }
        s = esAsignacion()
        if (s != null) {
            return s
        }
        s = esImpresion()
        if (s != null) {
            return s
        }
        s = esCiclo()
        if (s != null) {
            return s
        }
        s = esRetorno()
        if (s != null) {
            return s
        }
        s = esLecturaDatos()
        if (s != null) {
            return s
        }
        s = esInvocacionFuncion()
        if (s != null) {
            return s
        }
        s = esArreglo()
        if (s != null) {
            return s
        }
        s = esIncrementoDecremento()
        if (s != null) {
            return s
        }
        s = esExcepcion()
        return if (s != null) {
            s
        } else null
    }

    /**
     * <Decision> ::= si? "(" <ExpresionLogica> ")" "{" [<ListaSentencias>] "}" [
     * sino! "{" [<ListaSentencias>] "}"]
    </ListaSentencias></ListaSentencias></ExpresionLogica></Decision> */
    private fun esDecision(): Sentencia? {
        if (tokenActual.getCategoria() === Categoria.RESERVADA && tokenActual.lexema == "si?") {
            obtenerSiguienteToken()
            if (tokenActual.getCategoria() === Categoria.PARENTESIS_APERTURA) {
                obtenerSiguienteToken()
                val ex: ExpresionLogica? = esExpresionLogica()
                if (ex != null) {
                    if (tokenActual.getCategoria() === Categoria.PARENTESIS_CIERRE) {
                        obtenerSiguienteToken()
                        if (tokenActual.getCategoria() === Categoria.LLAVE_APERTURA) {
                            obtenerSiguienteToken()
                            val lista1: ArrayList<Sentencia> = esListaSentencias()
                            if (tokenActual.getCategoria() === Categoria.LLAVE_CIERRE) {
                                obtenerSiguienteToken()
                                if (tokenActual.getCategoria() === Categoria.RESERVADA
                                        && tokenActual.lexema == "sino!") {
                                    obtenerSiguienteToken()
                                    if (tokenActual.getCategoria() === Categoria.LLAVE_APERTURA) {
                                        obtenerSiguienteToken()
                                        val lista2: ArrayList<Sentencia> = esListaSentencias()
                                        if (tokenActual.getCategoria() === Categoria.LLAVE_CIERRE) {
                                            obtenerSiguienteToken()
                                            return Decision(ex, lista1, lista2)
                                        } else {
                                            reportarError("Falta la llave de cierre")
                                        }
                                    } else {
                                        reportarError("Falta lalve de apertura")
                                    }
                                } else {
                                    return Decision(ex, lista1)
                                }
                            } else {
                                reportarError("Falta la llave de cierre")
                            }
                        } else {
                            reportarError("Falta la llave de apertura")
                        }
                    } else {
                        reportarError("Falta parentesis de cierre")
                    }
                } else {
                    reportarError("Falta la expresion logica")
                }
            } else {
                reportarError("Falta la llave de apertura")
            }
        }
        return null
    }

    /**
     * <DeclaracionVariable> ::= <TipoDato> Identificador ";"
    </TipoDato></DeclaracionVariable> */
    private fun esDeclaracionVariable(): Sentencia? {
        val tipoDato = esTipoDato()
        if (tipoDato != null) {
            obtenerSiguienteToken()
            if (tokenActual.getCategoria() === Categoria.IDENTIFICADOR) {
                val iden = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.getCategoria() === Categoria.TERMINAL) {
                    obtenerSiguienteToken()
                    return DeclaracionVariable(tipoDato, iden)
                } else {
                    reportarError("Falta el terminal o fin de sentencia")
                }
            } else {
                reportarError("Falta el identificador")
            }
        }
        return null
    }

    /**
     * <Asignacion> ::= Identificador OpAsiganacion <Expresion> ";"
    </Expresion></Asignacion> */
    private fun esAsignacion(): Sentencia? {
        if (tokenActual.getCategoria() === Categoria.IDENTIFICADOR) {
            val iden = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.getCategoria() === Categoria.OPERADOR_ASIGNACION) {
                val opAsigna = tokenActual
                obtenerSiguienteToken()
                val ex: Expresion? = esExpresion()
                if (ex != null) {
                    if (tokenActual.getCategoria() === Categoria.TERMINAL) {
                        obtenerSiguienteToken()
                        return Asignacion(iden, opAsigna, ex)
                    }
                } else {
                    reportarError("Falta especificar el valor a asignar")
                }
            } else {
                reportarError("Falta el operador de asignacion")
            }
        }
        return null
    }

    fun obtenerTokenActual() {
        tokenActual = listaTokens[posActual]
    }

    /**
     * <Expresion> ::= <ExpresionAritmetica> | <ExpresionLogica> |
     * <ExpresionRelacional> | <ExpresionCadena>
    </ExpresionCadena></ExpresionRelacional></ExpresionLogica></ExpresionAritmetica></Expresion> */
    private fun esExpresion(): Expresion? {
        val posBack = posActual
        val expresionLogica: ExpresionLogica? = esExpresionLogica()
        if (expresionLogica != null) {
            return Expresion(expresionLogica)
        }
        posActual = posBack
        obtenerTokenActual()
        val expresionAritmetica: ExpresionAritmetica? = esExpresionAritmetica()
        if (expresionAritmetica != null) {
            return Expresion(expresionAritmetica)
        }
        val expresionCadena: ExpresionCadena? = esExpresionCadena()
        return if (expresionCadena != null) {
            Expresion(expresionCadena)
        } else null
    }

    /**
     * <Impresion> ::= imprimir ":" <Expresion>
    </Expresion></Impresion> */
    private fun esImpresion(): Sentencia? {
        if (tokenActual.getCategoria() === Categoria.RESERVADA && tokenActual.lexema == "imprimir") {
            obtenerSiguienteToken()
            if (tokenActual.getCategoria() === Categoria.DOS_PUNTOS) {
                obtenerSiguienteToken()
                val ex: Expresion? = esExpresion()
                if (ex != null) {
                    return Impresion(ex)
                } else {
                    reportarError("Falta ingresar el valor a imprimir")
                }
            } else {
                reportarError("faltan los \":\"")
            }
        }
        return null
    }

    /**
     * <MientrasQue> ::= mientrasQue "(" <ExpresionLogica> ")" "{"
     * [<ListaSentencias>] "}"
    </ListaSentencias></ExpresionLogica></MientrasQue> */
    private fun esCiclo(): Sentencia? {
        if (tokenActual.getCategoria() === Categoria.RESERVADA && tokenActual.lexema == "mientrasQue") {
            obtenerSiguienteToken()
            if (tokenActual.getCategoria() === Categoria.PARENTESIS_APERTURA) {
                obtenerSiguienteToken()
                val ex: ExpresionLogica? = esExpresionLogica()
                if (ex != null) {
                    if (tokenActual.getCategoria() === Categoria.PARENTESIS_CIERRE) {
                        obtenerSiguienteToken()
                        if (tokenActual.getCategoria() === Categoria.LLAVE_APERTURA) {
                            obtenerSiguienteToken()
                            val listaSentencias: ArrayList<Sentencia> = esListaSentencias()
                            if (tokenActual.getCategoria() === Categoria.LLAVE_CIERRE) {
                                obtenerSiguienteToken()
                                return Ciclo(ex, listaSentencias)
                            } else {
                                reportarError("Falta la llave de cierre")
                            }
                        } else {
                            reportarError("Falta llave de apertura")
                        }
                    } else {
                        reportarError("Falta parentecis de cierre")
                    }
                } else {
                    reportarError("Falta exprecion logica")
                }
            } else {
                reportarError("Falta parentecis de apertura")
            }
        }
        return null
    }

    /**
     * <Retorno> ::= Retorno [ <CategoriaRetorno> ] ";"
    </CategoriaRetorno></Retorno> */
    private fun esRetorno(): Sentencia? {
        if (tokenActual.getCategoria() === Categoria.RESERVADA && tokenActual.lexema == "Retorno") {
            obtenerSiguienteToken()
            val categoriaRetorno: CategoriaRetorno? = esCategoriaRetorno()
            if (tokenActual.getCategoria() === Categoria.TERMINAL) {
                obtenerSiguienteToken()
                return Retorno(categoriaRetorno)
            } else {
                reportarError("Falta el terminal")
            }
        }
        return null
    }

    /**
     * <CategoriaRetorno> ::= <Expresion> | null | <InvocacionFuncion>
    </InvocacionFuncion></Expresion></CategoriaRetorno> */
    private fun esCategoriaRetorno(): CategoriaRetorno? {
        val ex: Expresion? = esExpresion()
        if (ex != null) {
            return CategoriaRetorno(ex)
        }
        if (tokenActual.getCategoria() === Categoria.RESERVADA && tokenActual.lexema == "null") {
            val aux = tokenActual
            obtenerSiguienteToken()
            return CategoriaRetorno(aux)
        }
        val invoFun: InvocacionFuncion? = esInvocacionFuncion()
        return if (invoFun != null) {
            CategoriaRetorno(invoFun)
        } else null
    }

    /**
     * <LecturaDatos> ::= leer ":" <Expresion>
    </Expresion></LecturaDatos> */
    private fun esLecturaDatos(): Sentencia? {
        if (tokenActual.getCategoria() === Categoria.RESERVADA && tokenActual.lexema == "leer") {
            obtenerSiguienteToken()
            if (tokenActual.getCategoria() === Categoria.DOS_PUNTOS) {
                obtenerSiguienteToken()
                val ex: Expresion? = esExpresion()
                if (ex != null) {
                    return LecturaDatos(ex)
                } else {
                    reportarError("Falta agregar el valor a leer")
                }
            } else {
                reportarError("Faltan los \":\"")
            }
        }
        return null
    }

    /**
     * <InvocacionFuncion> ::= invo Identificador "(" [<ListaArgumentos>] ")" ";"
    </ListaArgumentos></InvocacionFuncion> */
    private fun esInvocacionFuncion(): InvocacionFuncion? {
        if (tokenActual.getCategoria() === Categoria.RESERVADA && tokenActual.lexema == "invo") {
            obtenerSiguienteToken()
            if (tokenActual.getCategoria() === Categoria.IDENTIFICADOR) {
                val iden = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.getCategoria() === Categoria.PARENTESIS_APERTURA) {
                    obtenerSiguienteToken()
                    val listaArgumentos: ArrayList<Argumento> = esListaArgumentos()
                    if (tokenActual.getCategoria() === Categoria.PARENTESIS_CIERRE) {
                        obtenerSiguienteToken()
                        if (tokenActual.getCategoria() === Categoria.TERMINAL) {
                            obtenerSiguienteToken()
                            return InvocacionFuncion(iden, listaArgumentos)
                        } else {
                            reportarError("Falta el operador de finalizacion")
                        }
                    } else {
                        reportarError("Falta el parentecis de cierre")
                    }
                } else {
                    reportarError("Falta el parentecis de apertura")
                }
            } else {
                reportarError("Falta el identificador del metodo")
            }
        }
        return null
    }

    /**
     * <ListaArgumento> ::= <Argumento> | <Argumento>","[<ListaArgumento>]
    </ListaArgumento></Argumento></Argumento></ListaArgumento> */
    private fun esListaArgumentos(): ArrayList<Argumento> {
        val listaArgumentos: ArrayList<Argumento> = ArrayList<Argumento>()
        var argumento: Argumento? = esArgumento()
        while (argumento != null) {
            listaArgumentos.add(argumento)
            argumento = if (tokenActual.getCategoria() === Categoria.SEPARADOR) {
                obtenerSiguienteToken()
                esArgumento()
            } else {
                return listaArgumentos
            }
        }
        return listaArgumentos
    }

    /**
     * <Argumento> ::= <Expresion>
    </Expresion></Argumento> */
    private fun esArgumento(): Argumento? {
        val expre: Expresion? = esExpresion()
        return if (expre != null) {
            Argumento(expre)
        } else null
    }

    /**
     * <TipoRetorno> ::= Entero | Real | Bool | Void | String | Char
    </TipoRetorno> */
    private fun esTipoRetorno(): Token? {
        return if (tokenActual.getCategoria() === Categoria.RESERVADA
                && (tokenActual.lexema == "Entero" || tokenActual.lexema == "Real" || tokenActual.lexema == "Bool" || tokenActual.lexema == "Void" || tokenActual.lexema == "String" || tokenActual.lexema == "Char")) {
            tokenActual
        } else null
    }

    /**
     * <ListaParametros> ::= <Parametro> | <Parametro> "," <ListaParametros>
    </ListaParametros></Parametro></Parametro></ListaParametros> */
    private fun esListaParametros(): ArrayList<Parametro> {
        val listaParametros: ArrayList<Parametro> = ArrayList<Parametro>()
        var parametro: Parametro? = esParametro()
        while (parametro != null) {
            listaParametros.add(parametro)
            obtenerSiguienteToken()
            parametro = if (tokenActual.getCategoria() === Categoria.SEPARADOR) {
                obtenerSiguienteToken()
                esParametro()
            } else {
                return listaParametros
            }
        }
        return listaParametros
    }

    /**
     * <Parametro> ::= <TipoDato> Identificador
    </TipoDato></Parametro> */
    private fun esParametro(): Parametro? {
        val tipoDato = esTipoDato()
        if (tipoDato != null) {
            obtenerSiguienteToken()
            if (tokenActual.getCategoria() === Categoria.IDENTIFICADOR) {
                return Parametro(tipoDato, tokenActual)
            }
        }
        return null
    }

    /**
     * <ExpresionLogica> ::= "(" <ExpresionLogica> ")" [<ZLogica>] |
     * OperadorLogicoUnario <ExpresionLogica>[<ZLogica>] | True [<Zlogica>] | False
     * [<Zlogica>] | <ExpresionRelacional> [<ZLogica>] | <InvocacionFuncion>
     * [<ZLogica>] | Identificador [<ZLogica>]
    </ZLogica></ZLogica></InvocacionFuncion></ZLogica></ExpresionRelacional></Zlogica></Zlogica></ZLogica></ExpresionLogica></ZLogica></ExpresionLogica></ExpresionLogica> */
    private fun esExpresionLogica(): ExpresionLogica? {
        val posBack = posActual
        val expRela: ExpresionRelacional? = esExpresionRelacional()
        if (expRela != null) {
            val zlogica: ZLogica? = esZLogica()
            return ExpresionLogica(null, null, expRela, null, null, null, zlogica)
        }
        posActual = posBack
        obtenerTokenActual()
        if (tokenActual.getCategoria() === Categoria.PARENTESIS_APERTURA) {
            obtenerSiguienteToken()
            val expLogic: ExpresionLogica? = esExpresionLogica()
            if (expLogic != null) {
                if (tokenActual.getCategoria() === Categoria.PARENTESIS_CIERRE) {
                    obtenerSiguienteToken()
                    val zlogica: ZLogica? = esZLogica()
                    return ExpresionLogica(expLogic, null, null, null, null, null, zlogica)
                }
            }
        }
        if (tokenActual.getCategoria() === Categoria.OPERADOR_LOGICO_UNARIO) {
            val opUnario = tokenActual
            obtenerSiguienteToken()
            val expLogic: ExpresionLogica? = esExpresionLogica()
            if (expLogic != null) {
                val zlogica: ZLogica? = esZLogica()
                return ExpresionLogica(expLogic, opUnario, null, null, null, null, zlogica)
            } else {
                reportarError("Falta una expresion logica valida")
            }
        }
        if ((tokenActual.getCategoria() === Categoria.RESERVADA
                        && (tokenActual.lexema == "True" || tokenActual.lexema == "False"))
                || tokenActual.getCategoria() === Categoria.IDENTIFICADOR) {
            val boolIdent = tokenActual
            obtenerSiguienteToken()
            val zlogica: ZLogica? = esZLogica()
            return if (boolIdent.getCategoria() === Categoria.IDENTIFICADOR) {
                ExpresionLogica(null, null, null, null, null, boolIdent, zlogica)
            } else {
                ExpresionLogica(null, null, null, null, boolIdent, null, zlogica)
            }
        }
        val invoFun: Sentencia? = esInvocacionFuncion()
        if (invoFun != null) {
            val zlogica: ZLogica? = esZLogica()
            return ExpresionLogica(null, null, null, invoFun, null, null, zlogica)
        }
        return null
    }

    /**
     * <ZLogica> ::= OpLogicoBinario <ExpresionLogica> [<ZLogica>]
    </ZLogica></ExpresionLogica></ZLogica> */
    private fun esZLogica(): ZLogica? {
        if (tokenActual.getCategoria() === Categoria.OPERADOR_LOGICO_BINARIO) {
            val opLogicoBinario = tokenActual
            obtenerSiguienteToken()
            val expLogica: ExpresionLogica? = esExpresionLogica()
            if (expLogica != null) {
                val zLogica: ZLogica? = esZLogica()
                return ZLogica(opLogicoBinario, expLogica, zLogica)
            } else {
                reportarError("Falta la expresion logica")
            }
        }
        return null
    }

    /**
     * <ExpCadena> ::= cadena[<zcadena>]
    </zcadena></ExpCadena> */
    private fun esExpresionCadena(): ExpresionCadena? {
        if (tokenActual.getCategoria() === Categoria.CADENA_CARACTERES) {
            val cadena = tokenActual
            obtenerSiguienteToken()
            val z: ZCadena? = esZCadena()
            return ExpresionCadena(cadena, z)
        }
        return null
    }

    /**
     * <ZCadena> ::= "+"<Expresion>[<ZCadena>]
    </ZCadena></Expresion></ZCadena> */
    private fun esZCadena(): ZCadena? {
        if (tokenActual.getCategoria() === Categoria.OPERADOR_ARITMETICO && tokenActual.lexema == "+") {
            obtenerSiguienteToken()
            val ex: Expresion? = esExpresion()
            if (ex != null) {
                val z: ZCadena? = esZCadena()
                return ZCadena(ex, z)
            } else {
                reportarError("Falta una expresion valida")
            }
        }
        return null
    }

    /**
     * <ExpresionRelacional> ::= <InvocacionMetodo> OperadorRelacional
     * <InvocacionMetodo> [<ZRelacional>] | "(" <ExpresionRelacional> ")"
     * [<ZRelacional>] | Identificador [<ZRelacional>] | <ExpresionAritmetica>
     * OperadorRelacional <ExpresionAritmetica> [<ZRelacional>]
    </ZRelacional></ExpresionAritmetica></ExpresionAritmetica></ZRelacional></ZRelacional></ExpresionRelacional></ZRelacional></InvocacionMetodo></InvocacionMetodo></ExpresionRelacional> */
    private fun esExpresionRelacional(): ExpresionRelacional? {
        val posBack = posActual
        val expresionIzquierda: ExpresionAritmetica? = esExpresionAritmetica()
        if (expresionIzquierda != null) {
            if (tokenActual.getCategoria() === Categoria.OPERADOR_RELACIONAL) {
                val operadorRelacional = tokenActual
                obtenerSiguienteToken()
                val expresionDerecha: ExpresionAritmetica? = esExpresionAritmetica()
                if (expresionDerecha != null) {
                    val z: ZRelacional? = esZRelacional()
                    return ExpresionRelacional(operadorRelacional, expresionIzquierda, expresionDerecha, z)
                } else {
                    reportarError("Falta expresion aritmetica derecha")
                }
            }
        }
        posActual = posBack
        obtenerTokenActual()
        if (tokenActual.getCategoria() === Categoria.IDENTIFICADOR) {
            val iden = tokenActual
            obtenerSiguienteToken()
            val z: ZRelacional? = esZRelacional()
            return ExpresionRelacional(iden, z)
        }
        if (tokenActual.getCategoria() === Categoria.PARENTESIS_APERTURA) {
            obtenerSiguienteToken()
            val expresion: ExpresionRelacional? = esExpresionRelacional()
            if (expresion != null) {
                if (tokenActual.getCategoria() === Categoria.PARENTESIS_CIERRE) {
                    obtenerSiguienteToken()
                    val z: ZRelacional? = esZRelacional()
                    return ExpresionRelacional(expresion, z)
                } else {
                    reportarError("Falta parentecis de cierre en relacional")
                }
            }
        }
        return null
    }

    /**
     * <ZRelacional> ::= OperadorRelacional <ExpresionRelacional> [<ZRelacional>]
    </ZRelacional></ExpresionRelacional></ZRelacional> */
    private fun esZRelacional(): ZRelacional? {
        if (tokenActual.getCategoria() === Categoria.OPERADOR_RELACIONAL) {
            val opRelacional = tokenActual
            obtenerSiguienteToken()
            val expresion: ExpresionRelacional? = esExpresionRelacional()
            if (expresion != null) {
                val z: ZRelacional? = esZRelacional()
                return ZRelacional(opRelacional, expresion, z)
            }
        }
        return null
    }

    /**
     * <ExpresionAritmetica> ::= <Numero>[<ZAritmetica></ZAritmetica>] |
     * "(ExpresionAritmetica")"[<ZAritmetica></ZAritmetica>] | Identificador [<ZAritmetica>] |
     * <InvocacionMetodo>[<ZAritmetica>]
    </ZAritmetica></InvocacionMetodo></ZAritmetica></Numero></ExpresionAritmetica> */
    private fun esExpresionAritmetica(): ExpresionAritmetica? {
        if (tokenActual.getCategoria() === Categoria.ENTERO || tokenActual.getCategoria() === Categoria.REAL) {
            val num = tokenActual
            obtenerSiguienteToken()
            val z: ZAritmetica? = esZAritmetica()
            return ExpresionAritmetica(num, z)
        }
        if (tokenActual.getCategoria() === Categoria.PARENTESIS_APERTURA) {
            obtenerSiguienteToken()
            val ex: ExpresionAritmetica? = esExpresionAritmetica()
            if (ex != null) {
                if (tokenActual.getCategoria() === Categoria.PARENTESIS_CIERRE) {
                    obtenerSiguienteToken()
                    val z: ZAritmetica? = esZAritmetica()
                    return ExpresionAritmetica(z, ex)
                }
            }
        }
        if (tokenActual.getCategoria() === Categoria.IDENTIFICADOR) {
            val iden = tokenActual
            obtenerSiguienteToken()
            val z: ZAritmetica? = esZAritmetica()
            return ExpresionAritmetica(z, iden)
        }
        val invoFuncion: InvocacionFuncion? = esInvocacionFuncion()
        if (invoFuncion != null) {
            val z: ZAritmetica? = esZAritmetica()
            return ExpresionAritmetica(z, invoFuncion)
        }
        return null
    }

    /**
     * <ZAritmetica> ::= OpAritmetica<ExpresionAritmetica>[<ZAritmetica>]
    </ZAritmetica></ExpresionAritmetica></ZAritmetica> */
    private fun esZAritmetica(): ZAritmetica? {
        if (tokenActual.getCategoria() === Categoria.OPERADOR_ARITMETICO) {
            val opAritmetico = tokenActual
            obtenerSiguienteToken()
            val expresion: ExpresionAritmetica? = esExpresionAritmetica()
            if (expresion != null) {
                val z: ZAritmetica? = esZAritmetica()
                return ZAritmetica(opAritmetico, expresion, z)
            } else {
                reportarError("Falta una expresion aritmetica")
            }
        }
        return null
    }

    /**
     * <Arreglos> ::= arreglo<TipoDato><Corchetes><Identificador>";"
    </Identificador></Corchetes></TipoDato></Arreglos> */
    private fun esArreglo(): Arreglo? {
        if (tokenActual.getCategoria() === Categoria.RESERVADA && tokenActual.lexema == "arreglo") {
            obtenerSiguienteToken()
            val dato = esTipoDato()
            if (dato != null) {
                obtenerSiguienteToken()
                val listaCorchetes: ArrayList<Corchetes> = esListaCorchetes()
                if (listaCorchetes.size > 0) {
                    if (tokenActual.getCategoria() === Categoria.IDENTIFICADOR) {
                        val iden = tokenActual
                        obtenerSiguienteToken()
                        if (tokenActual.getCategoria() === Categoria.TERMINAL) {
                            obtenerSiguienteToken()
                            return Arreglo(dato, iden, listaCorchetes, null)
                        } else if (tokenActual.getCategoria() === Categoria.OPERADOR_ASIGNACION
                                && tokenActual.lexema == "=") {
                            obtenerSiguienteToken()
                            if (tokenActual.getCategoria() === Categoria.LLAVE_APERTURA) {
                                obtenerSiguienteToken()
                                val argumentos: ArrayList<Argumento> = esListaArgumentos()
                                if (tokenActual.getCategoria() === Categoria.LLAVE_CIERRE) {
                                    obtenerSiguienteToken()
                                    if (tokenActual.getCategoria() === Categoria.TERMINAL) {
                                        obtenerSiguienteToken()
                                        return Arreglo(dato, iden, listaCorchetes, argumentos)
                                    } else {
                                        reportarError("Falta el terminal")
                                    }
                                } else {
                                    reportarError("Falta llave de cierre")
                                }
                            } else {
                                reportarError("Falta llave de apertura")
                            }
                        } else {
                            reportarError("falta terminal")
                        }
                    } else {
                        reportarError("Falta identificador")
                    }
                } else {
                    reportarError("Faltan corchetes")
                }
            } else {
                reportarError("falta el tipo del arreglo")
            }
        }
        return null
    }

    /**
     * <ListaCorchetes> ::= <corchete> [<listaCorchete>]
    </listaCorchete></corchete></ListaCorchetes> */
    private fun esListaCorchetes(): ArrayList<Corchetes> {
        val lista: ArrayList<Corchetes> = ArrayList<Corchetes>()
        var c: Corchetes? = esCorchetes()
        while (c != null) {
            lista.add(c)
            c = esCorchetes()
        }
        return lista
    }

    /**
     * <corchetes> ::= "[""]"
    </corchetes> */
    private fun esCorchetes(): Corchetes? {
        if (tokenActual.getCategoria() === Categoria.CORCHETE_APERTURA) {
            val apertura = tokenActual.lexema
            obtenerSiguienteToken()
            if (tokenActual.getCategoria() === Categoria.CORCHETE_CIERRE) {
                val cierre = tokenActual.lexema
                obtenerSiguienteToken()
                return Corchetes(apertura + cierre)
            } else {
                reportarError("Falta corchete de cierre")
            }
        }
        return null
    }

    /**
     * <IncrementoDecremento> ::= Identificador OpIncrementoDecremento ";"
    </IncrementoDecremento> */
    private fun esIncrementoDecremento(): Sentencia? {
        if (tokenActual.getCategoria() === Categoria.OPERADOR_INCREMENTO_DECREMENTO) {
            val op = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.getCategoria() === Categoria.IDENTIFICADOR) {
                val iden = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.getCategoria() === Categoria.TERMINAL) {
                    obtenerSiguienteToken()
                    return IncrementoDecremento(iden, op)
                }
            }
        }
        return null
    }

    /**
     * <Excepcion> ::= Excepcion <ExpresionCadena>
    </ExpresionCadena></Excepcion> */
    private fun esExcepcion(): Sentencia? {
        if (tokenActual.getCategoria() === Categoria.RESERVADA && tokenActual.lexema == "Excepcion") {
            obtenerSiguienteToken()
            if (tokenActual.getCategoria() === Categoria.PARENTESIS_APERTURA) {
                obtenerSiguienteToken()
                val expreCadena: ExpresionCadena? = esExpresionCadena()
                if (expreCadena != null) {
                    if (tokenActual.getCategoria() === Categoria.PARENTESIS_CIERRE) {
                        obtenerSiguienteToken()
                        if (tokenActual.getCategoria() === Categoria.TERMINAL) {
                            obtenerSiguienteToken()
                            return Excepcion(expreCadena)
                        } else {
                            reportarError("Falta terminal en la excepcion")
                        }
                    } else {
                        reportarError("Falta parentesis de cierre en la excepcion")
                    }
                } else {
                    reportarError("Falta una expresion cadena valida")
                }
            } else {
                reportarError("Falte parentesis de apertura en al excepcion")
            }
        }
        return null
    }

    /**
     * <TipoDato> ::= Entero | Real | Bool | String | Char
    </TipoDato> */
    private fun esTipoDato(): Token? {
        return if (tokenActual.getCategoria() === Categoria.RESERVADA && (tokenActual.lexema == "Entero" || tokenActual.lexema == "Real" || tokenActual.lexema == "Bool" || tokenActual.lexema == "String" || tokenActual.lexema == "Char")) {
            tokenActual
        } else null
    }

    init {
        tokenActual = listaTokens[posActual]
        listaErrores = ArrayList()
    }
}